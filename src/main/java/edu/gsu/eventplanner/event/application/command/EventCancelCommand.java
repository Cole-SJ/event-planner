package edu.gsu.eventplanner.event.application.command;

import edu.gsu.eventplanner.event.domain.Event;
import edu.gsu.eventplanner.event.domain.exception.NotRegisteredEventException;
import edu.gsu.eventplanner.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class EventCancelCommand {
    private Long eventId;
    private String accessToken;


    public Event execute(Event event, Member member){
        if(!event.getParticipants().contains(member.getId())){
            throw new NotRegisteredEventException();
        }
        event.cancelEvent(member.getId());
        return event;
    }
}
