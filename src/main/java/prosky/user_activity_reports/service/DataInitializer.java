package prosky.user_activity_reports.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
public class DataInitializer {

    private final CsvImporterService csvImporterService;

    @Value("${app.import-data-on-start=false}")
    private boolean shouldImportDataOnStart;

    public DataInitializer(CsvImporterService csvImporterService) {
        this.csvImporterService = csvImporterService;
    }

    @PostConstruct
    public void init() {
        if (shouldImportDataOnStart) {
            try {
                InputStream inputStream = getClass().getClassLoader().getResourceAsStream("test_case.csv");
                if (inputStream != null) {
                    csvImporterService.importCsv(inputStream);
                    System.out.println("CSV импортирован успешно!");
                } else {
                    System.err.println("Файл test_case.csv не найден в ресурсах!");
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Ошибка при импорте CSV: " + e.getMessage());
            }
        }
    }
}