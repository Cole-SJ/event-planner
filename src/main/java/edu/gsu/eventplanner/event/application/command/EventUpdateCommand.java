package edu.gsu.eventplanner.event.application.command;

import edu.gsu.eventplanner.event.domain.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Set;

@AllArgsConstructor
@Getter
public class EventUpdateCommand {
    private Long eventId;
    private String eventName;
    private String eventLocation;
    private LocalDateTime eventHeldAt;
    private Long maxJoinCount;
    private String eventContents;
    private Set<Long> participant;
    private String accessToken;

    public Event execute(Event event) {
        event.updateEvent(eventName, eventLocation, eventHeldAt, maxJoinCount, eventContents, participant);
        return event;
    }
}
