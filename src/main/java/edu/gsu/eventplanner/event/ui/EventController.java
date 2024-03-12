package edu.gsu.eventplanner.event.ui;

import edu.gsu.eventplanner.common.response.EventPlannerResponse;
import edu.gsu.eventplanner.event.application.EventCommandHandler;
import edu.gsu.eventplanner.event.application.EventQueryService;
import edu.gsu.eventplanner.event.application.command.EventCancelCommand;
import edu.gsu.eventplanner.event.application.command.EventDeleteCommand;
import edu.gsu.eventplanner.event.application.command.EventRegisterCommand;
import edu.gsu.eventplanner.event.application.dto.EventDetailView;
import edu.gsu.eventplanner.event.application.dto.EventListView;
import edu.gsu.eventplanner.event.ui.dto.EventCreateRequest;
import edu.gsu.eventplanner.event.ui.dto.EventUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class EventController {

    private final EventQueryService queryService;
    private final EventCommandHandler commandHandler;

    @PostMapping("/api/v1/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventPlannerResponse<Long> createEvent(@RequestHeader("Authorization") String accessToken,
                                                  @RequestBody @Valid EventCreateRequest request) {
        var event = commandHandler.handle(request.toCommand(accessToken));
        return new EventPlannerResponse<>(event.getId(), null);
    }

    @PutMapping("/api/v1/events/{eventId}")
    public EventPlannerResponse<Long> updateEvent(@RequestHeader("Authorization") String accessToken,
                                                  @PathVariable("eventId") Long eventId,
                                                  @RequestBody @Valid EventUpdateRequest request) {
        var event = commandHandler.handle(request.toCommand(accessToken, eventId));
        return new EventPlannerResponse<>(event.getId(), null);
    }

    @DeleteMapping("/api/v1/events/{eventId}")
    public EventPlannerResponse<Long> deleteEvent(@RequestHeader("Authorization") String accessToken,
                                                  @PathVariable("eventId") Long eventId) {
        var event = commandHandler.handle(new EventDeleteCommand(eventId, accessToken));
        return new EventPlannerResponse<>(event.getId(), null);
    }

    @PostMapping("/api/v1/events/{eventId}/cancel")
    public EventPlannerResponse<Long> cancelEvent(@RequestHeader("Authorization") String accessToken,
                                                  @PathVariable("eventId") Long eventId) {
        var event = commandHandler.handle(new EventCancelCommand(eventId, accessToken));
        return new EventPlannerResponse<>(event.getId(), null);
    }

    @PostMapping("/api/v1/events/{eventId}/register")
    public EventPlannerResponse<Long> registerEvent(@RequestHeader("Authorization") String accessToken,
                                                    @PathVariable("eventId") Long eventId) {
        var event = commandHandler.handle(new EventRegisterCommand(eventId, accessToken));
        return new EventPlannerResponse<>(event.getId(), null);
    }

    @GetMapping("/api/v1/events")
    public EventPlannerResponse<List<EventListView>> getEventList(String keyword) {
        var eventList = queryService.searchEvents(keyword);
        return new EventPlannerResponse<>(eventList, null);
    }

    @GetMapping("/api/vi/events/{eventId}")
    public EventPlannerResponse<EventDetailView> getEventDetail(@PathVariable("eventId") Long eventId) {
        var eventDetail = queryService.getEventDetail(eventId);
        return new EventPlannerResponse<>(eventDetail, null);
    }

    @GetMapping("/api/v1/events/my-events")
    public EventPlannerResponse<List<EventListView>> getMyEventList(@RequestHeader("Authorization") String accessToken) {
        var myEventList = queryService.getMyEvents(accessToken);
        return new EventPlannerResponse<>(myEventList, null);
    }


}
