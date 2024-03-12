package edu.gsu.eventplanner.event.domain.repository;

import edu.gsu.eventplanner.event.domain.Event;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EventRepository {
    Event save(Event event);

    Optional<Event> findById(Long id);

    Optional<Event> findByEventName(String eventName);

    List<Event> findAll();

    List<Event> findAllByCreatedBy(Long memberId);

    void delete(Event event);


    @Query("""
            select E from Event E where E.eventName like %:eventName% 
            """)
    List<Event> searchByEventName(@Param("eventName") String eventName);
}
