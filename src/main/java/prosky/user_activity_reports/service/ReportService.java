package prosky.user_activity_reports.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import prosky.user_activity_reports.model.Event;
import prosky.user_activity_reports.repository.EventRepository;

import java.awt.print.Pageable;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ReportService {

    private final EventRepository eventRepository;

    public ReportService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<Object[]> getUsersAndFormsLastHour() {
        Timestamp oneHourAgo = Timestamp.from(Instant.now().minus(1, ChronoUnit.HOURS));
        return eventRepository.findUsersAndFormsLastHour(oneHourAgo);
    }

    public List<Event> getFromSteps() {
        return eventRepository.findFormSteps();
    }

    public List<Object[]> getTop5Froms() {
        return eventRepository.findTop5Forms((Pageable) PageRequest.of(0, 5));
    }
}
