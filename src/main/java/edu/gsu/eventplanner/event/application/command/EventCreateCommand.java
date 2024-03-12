package edu.gsu.eventplanner.event.application.command;

import edu.gsu.eventplanner.event.domain.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class EventCreateCommand {
    private String eventName;
    private String eventLocation;
    private LocalDateTime eventHeldAt;
    private Long maxJoinCount;
    private String eventContents;
    private String accessToken;

    public Event execute(Long createdBy) {
        return Event.createEvent(eventName, eventLocation, eventHeldAt, maxJoinCount, eventContents, createdBy);
    }
}
