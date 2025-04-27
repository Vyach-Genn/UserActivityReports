package prosky.user_activity_reports.service;

import com.opencsv.CSVReader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import prosky.user_activity_reports.model.Event;
import prosky.user_activity_reports.repository.EventRepository;

import java.io.ByteArrayInputStream;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CsvImporterServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Spy
    @InjectMocks
    private CsvImporterService csvImporterService;

    @Test
    void testImportCsv_ValidData() throws Exception {
        String[] validRow = {"user123", "2025-04-26 12:34:56", "dszn_example", "form_submission", "step1",
                "https://example.com", "org1", "form123", "ltpa_value", "sudir_response", "2025-04-26"};

        CSVReader mockCsvReader = mock(CSVReader.class);
        when(mockCsvReader.readNext())
                .thenReturn(new String[]{"header"})
                .thenReturn(validRow)
                .thenReturn(null);

        doReturn(mockCsvReader).when(csvImporterService).createCsvReader(any());

        csvImporterService.importCsv(new ByteArrayInputStream(new byte[0]));

        verify(eventRepository, times(1)).save(any(Event.class));
    }

    @Test
    void testImportCsv_InvalidData() throws Exception {
        String[] invalidRow = {"user123", "invalid_timestamp", "dszn_example", "form_submission", "step1",
                "https://example.com", "org1", "form123", "ltpa_value", "sudir_response", "2025-04-26"};

        CSVReader mockCsvReader = mock(CSVReader.class);
        when(mockCsvReader.readNext())
                .thenReturn(new String[]{"header"})
                .thenReturn(invalidRow)
                .thenReturn(null);

        doReturn(mockCsvReader).when(csvImporterService).createCsvReader(any());

        csvImporterService.importCsv(new ByteArrayInputStream(new byte[0]));

        verify(eventRepository, never()).save(any(Event.class));
    }
}