
User Activity Reports
Описание проекта
Приложение на Spring Boot для импорта событий пользователей из CSV-файла в базу данных PostgreSQL, а также генерации отчетов о действиях пользователей через веб-интерфейс с использованием Thymeleaf.

Основные функции:
Импорт CSV-файла с событиями пользователей при старте приложения (если задан параметр).

Сохранение событий в таблицу events базы данных.

Генерация 3 отчетов:

Пользователи и формы за последний час.

Пользователи, которые не завершили заполнение форм.

Топ-5 наиболее используемых форм.

Стек технологий:
Java 17+

Spring Boot

Spring Data JPA

PostgreSQL

OpenCSV

Thymeleaf

JUnit 5 + Mockito для тестирования

Как запустить проект
Убедитесь, что у вас установлен PostgreSQL.

Создайте базу данных и пользователя:

sql
Копировать
Редактировать
CREATE DATABASE dbForTest;
CREATE USER UserForTest WITH PASSWORD '123';
GRANT ALL PRIVILEGES ON DATABASE dbForTest TO UserForTest;
Проверьте настройки подключения к БД в application.properties:

ini
Копировать
Редактировать
spring.datasource.url=jdbc:postgresql://localhost:5432/dbForTest
spring.datasource.username=UserForTest
spring.datasource.password=123
Убедитесь, что в папке resources находится файл test_case.csv со следующими данными:

cs
Копировать
Редактировать
ssoid,ts,grp,type,subtype,url,orgid,formid,ltpa,sudirresponse,ymdh
user123,2025-04-26 12:34:56,dszn_example,form_submission,step1,https://example.com,org1,form123,ltpa_value,sudir_response,2025-04-26
user456,2025-04-26 12:35:12,dszn_example,form_submission,step2,https://example.com,org2,form124,ltpa_value,sudir_response,2025-04-26
При необходимости включите импорт CSV на старте приложения:

pgsql
Копировать
Редактировать
app.import-data-on-start=true
Запустите приложение:

bash
Копировать
Редактировать
./mvnw spring-boot:run
Перейдите в браузере по следующим адресам для просмотра отчетов:

http://localhost:8080/reports/last-hour — Пользователи за последний час

http://localhost:8080/reports/incomplete-forms — Незавершенные формы

http://localhost:8080/reports/top-froms — Топ-5 форм

Структура проекта
controller/ReportController.java — контроллер для отображения отчетов.

model/Event.java — сущность события пользователя.

repository/EventRepository.java — репозиторий с запросами к БД.

service/

CsvImporterService.java — сервис для импорта CSV.

ReportService.java — сервис для генерации отчетов.

DataInitializer.java — загрузчик CSV при старте приложения.

templates/ — шаблоны Thymeleaf для отчетов.

Тестирование
Есть тесты на сервис импорта CSV:

bash
Копировать
Редактировать
./mvnw test
Проверяются:

Сохранение валидных строк в базу.

Игнорирование невалидных строк.

Возможные ошибки
Файл test_case.csv не найден — убедитесь, что файл находится в папке resources.

Ошибка подключения к базе данных — проверьте URL, имя пользователя и пароль.

Будущие улучшения
Улучшить обработку ошибок при импорте данных.

Добавить пагинацию на страницы отчетов.

Сделать загрузку CSV через веб-интерфейс.