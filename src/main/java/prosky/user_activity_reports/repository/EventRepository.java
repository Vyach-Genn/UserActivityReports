package prosky.user_activity_reports.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import prosky.user_activity_reports.model.Event;

import java.awt.print.Pageable;
import java.sql.Timestamp;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("SELECT e.ssoid, e.formid FROM Event e WHERE e.ts > :time ORDER BY e.ts DESC")
    List<Object[]> findUsersAndFormsLastHour(@Param("time") Timestamp time);

    @Query("SELECT e.ssoid, e.formid, MAX(e.ts) FROM Event e " +
            "WHERE e.grp LIKE 'dszn_%' AND (e.type = 'start' " +
            "OR e.type = 'send') GROUP BY e.ssoid, e.formid")
    List<Event> findFormSteps();

    @Query("SELECT e.formid, COUNT(e) FROM Event e GROUP BY e.formid ORDER BY COUNT(e) DESC")
    List<Object[]> findTop5Forms(Pageable pageable);
}

