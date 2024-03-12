package edu.gsu.eventplanner.event.infra;

import edu.gsu.eventplanner.event.domain.Event;
import edu.gsu.eventplanner.event.domain.repository.EventRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventJpaRepository extends JpaRepository<Event, Long>, EventRepository {
}
