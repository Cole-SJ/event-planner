package edu.gsu.eventplanner.event.application;

import edu.gsu.eventplanner.event.application.dto.EventDetailView;
import edu.gsu.eventplanner.event.application.dto.EventListView;
import edu.gsu.eventplanner.event.domain.exception.NotFoundEventException;
import edu.gsu.eventplanner.event.domain.repository.EventRepository;
import edu.gsu.eventplanner.member.domain.exception.NotAuthorizedException;
import edu.gsu.eventplanner.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventQueryService {
    private final EventRepository eventRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public List<EventListView> searchEvents(String keyword) {
        return (keyword == null ? eventRepository.findAll() : eventRepository.searchByEventName(keyword)).stream().map(EventListView::from).toList();
    }

    @Transactional(readOnly = true)
    public EventDetailView getEventDetail(Long eventId) {
        var event = eventRepository.findById(eventId).orElseThrow(NotFoundEventException::new);
        return EventDetailView.from(event);
    }

    @Transactional(readOnly = true)
    public List<EventListView> getMyEvents(String accessToken) {
        var member = memberRepository.findByAccessToken(accessToken).orElseThrow(NotAuthorizedException::new);
        return eventRepository.findAllByCreatedBy(member.getId()).stream()
                .map(EventListView::from)
                .toList();
    }
}
