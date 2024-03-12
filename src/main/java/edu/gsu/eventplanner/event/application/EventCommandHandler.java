package edu.gsu.eventplanner.event.application;

import edu.gsu.eventplanner.event.application.command.*;
import edu.gsu.eventplanner.event.domain.Event;
import edu.gsu.eventplanner.event.domain.exception.ExistedEventException;
import edu.gsu.eventplanner.event.domain.exception.NotFoundEventException;
import edu.gsu.eventplanner.event.domain.exception.NotMyEventException;
import edu.gsu.eventplanner.event.domain.repository.EventRepository;
import edu.gsu.eventplanner.member.domain.exception.NotAuthorizedException;
import edu.gsu.eventplanner.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class EventCommandHandler {

    private final EventRepository eventRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Event handle(EventCreateCommand command) {
        //이벤트 이름으로 이벤트를 찾았을 때, 이벤트가 있으면 안됨...
        var member = memberRepository.findByAccessToken(command.getAccessToken())
                .orElseThrow(NotAuthorizedException::new);
        eventRepository.findByEventName(command.getEventName())
                .ifPresent((event) -> {
                    throw new ExistedEventException();
                });
        return eventRepository.save(command.execute(member.getId()));
    }

    @Transactional
    public Event handle(EventUpdateCommand command) {
        var member = memberRepository.findByAccessToken(command.getAccessToken())
                .orElseThrow(NotAuthorizedException::new);
        var event = eventRepository.findById(command.getEventId())
                .orElseThrow(NotFoundEventException::new);

        if (event.getCreatedBy() != member.getId()) {
            throw new NotMyEventException();
        }
        eventRepository.findByEventName(command.getEventName())
                .ifPresent((existedEvent) -> {
                    throw new ExistedEventException();
                });
        return eventRepository.save(command.execute(event));
    }

    @Transactional
    public Event handle(EventDeleteCommand command) {
        var member = memberRepository.findByAccessToken(command.getAccessToken())
                .orElseThrow(NotAuthorizedException::new);
        var event = eventRepository.findById(command.getEventId())
                .orElseThrow(NotFoundEventException::new);
        if (event.getCreatedBy() != member.getId()) {
            throw new NotMyEventException();
        }
        eventRepository.delete(command.execute(event));
        return event;
    }

    @Transactional
    public Event handle(EventCancelCommand command) {
        var member = memberRepository.findByAccessToken(command.getAccessToken())
                .orElseThrow(NotAuthorizedException::new);
        var event = eventRepository.findById(command.getEventId())
                .orElseThrow(NotFoundEventException::new);
        return eventRepository.save(command.execute(event, member));
    }

    @Transactional
    public Event handle(EventRegisterCommand command) {
        var member = memberRepository.findByAccessToken(command.getAccessToken())
                .orElseThrow(NotAuthorizedException::new);
        var event = eventRepository.findById(command.getEventId())
                .orElseThrow(NotFoundEventException::new);
        return eventRepository.save(command.execute(event, member));
    }

}
