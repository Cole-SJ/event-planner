package edu.gsu.eventplanner.event.application.command;

import edu.gsu.eventplanner.event.domain.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class EventDeleteCommand {
    private Long eventId;
    private String accessToken;


    public Event execute(Event event){
        return event;
    }
}
