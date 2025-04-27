package prosky.user_activity_reports.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.stereotype.Service;
import prosky.user_activity_reports.model.Event;
import prosky.user_activity_reports.repository.EventRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;


@Service
public class CsvImporterService {

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private final EventRepository eventRepository;

    public CsvImporterService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public void importCsv(InputStream inputStream) {
        try (CSVReader csvReader = createCsvReader(inputStream)) {
            skipHeader(csvReader);
            processRows(csvReader);
        } catch (CsvValidationException e) {
            handleCsvValidationException(e);
        } catch (IOException e) {
            handleIOException(e);
        }
    }

    private void processRows(CSVReader csvReader) throws IOException, CsvValidationException {
        String[] nextLine;
        while ((nextLine = csvReader.readNext()) != null) {
            if (isValidRow(nextLine)) {
                processValidRow(nextLine);
            } else {
                logInvalidRow(nextLine);
            }
        }
    }

    private void processValidRow(String[] row) {
        Event event = mapToEvent(row);
        if (isValidEvent(event)) {
            eventRepository.save(event);
        } else {
            logInvalidRow(row);
        }
    }

    private boolean isValidEvent(Event event) {
        return event.getTs() != null && isValidTimestamp(event.getTs());
    }

    private boolean isValidRow(String[] row) {
        return row.length >= 11;
    }


    protected CSVReader createCsvReader(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        return new CSVReader(reader);
    }

    private void skipHeader(CSVReader csvReader) throws CsvValidationException, IOException {
        csvReader.readNext();  // Пропускаем заголовок
    }


    private Event mapToEvent(String[] row) {
        Event event = new Event();
        event.setSsoid(row[0]);
        event.setTs(parseTimestamp(row[1]));  // Парсим timestamp
        event.setGrp(row[2]);
        event.setType(row[3]);
        event.setSubtype(row[4]);
        event.setUrl(row[5]);
        event.setOrgid(row[6]);
        event.setFormid(row[7]);
        event.setLtpa(row[8]);
        event.setSudirresponse(row[9]);
        event.setYmdh(row[10]);
        return event;
    }

    private Timestamp parseTimestamp(String timestampString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
            return new Timestamp(sdf.parse(timestampString.trim()).getTime());
        } catch (ParseException e) {
            logDateParseError(timestampString);
            return null;  // Если не удалось парсить, возвращаем null
        }
    }

    private boolean isValidTimestamp(Timestamp timestamp) {
        // Проверяем, что timestamp не null и что дата соответствует формату
        if (timestamp == null) {
            return false;
        }

        // Проверим, является ли дата корректной
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        try {
            String formattedDate = sdf.format(timestamp);
            return sdf.parse(formattedDate) != null;
        } catch (ParseException e) {
            return false;  // Если парсинг не удался, дата невалидна
        }
    }

    private void logInvalidRow(String[] row) {
        System.err.println("Неверное количество данных в строке: " + String.join(", ", row));
    }

    private void logDateParseError(String timestampString) {
        System.err.println("Ошибка формата даты: " + timestampString);
    }

    private void handleCsvValidationException(CsvValidationException e) {
        System.err.println("Ошибка при чтении CSV: " + e.getMessage());
        throw new RuntimeException("Ошибка при чтении CSV", e);
    }

    private void handleIOException(IOException e) {
        System.err.println("Ошибка ввода-вывода: " + e.getMessage());
        throw new RuntimeException("Ошибка ввода-вывода", e);
    }
}



