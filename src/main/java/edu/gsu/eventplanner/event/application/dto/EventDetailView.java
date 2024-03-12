package edu.gsu.eventplanner.event.application.dto;

import edu.gsu.eventplanner.event.domain.Event;

import java.time.LocalDateTime;

public record EventDetailView(
        Long id,
        String eventName,
        String eventLocation,
        LocalDateTime eventHeldAt,
        Long maxJoinCount,
        String eventContents
) {
    public static EventDetailView from(Event event) {
        return new EventDetailView(
                event.getId(),
                event.getEventName(),
                event.getEventLocation(),
                event.getEventHeldAt(),
                event.getMaxJoinCount(),
                event.getEventContents()
        );
    }
}
