package edu.gsu.eventplanner.web;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceDocumentation;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import edu.gsu.eventplanner.annotation.RestDocsTest;
import edu.gsu.eventplanner.config.MockMvcFactory;
import edu.gsu.eventplanner.event.application.EventCommandHandler;
import edu.gsu.eventplanner.event.application.EventQueryService;
import edu.gsu.eventplanner.event.application.command.*;
import edu.gsu.eventplanner.event.application.dto.EventDetailView;
import edu.gsu.eventplanner.event.application.dto.EventListView;
import edu.gsu.eventplanner.event.domain.Event;
import edu.gsu.eventplanner.event.ui.EventController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.headers.HeaderDescriptor;
import org.springframework.restdocs.headers.HeaderDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.stream.Stream;

import static edu.gsu.eventplanner.config.DocumentUtils.getDocumentRequest;
import static edu.gsu.eventplanner.config.DocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;

@RestDocsTest
public class EventControllerTest {

    @InjectMocks
    private EventController eventController;

    @Mock
    private EventQueryService queryService;
    @Mock
    private EventCommandHandler commandHandler;


    @DisplayName("event create")
    @Test
    void createEventRestDocsTest(RestDocumentationContextProvider contextProvider) throws Exception {

        var requestJson = """
                {
                    "eventName": "test event name",
                    "eventLocation": "test event location",
                    "eventHeldAt": "2024-04-01T13:00:00",
                    "maxJoinCount": 10,
                    "eventContents": "test event contents"
                }
                """;

        var accessToken = UUID.randomUUID().toString();

        var event = Event.createEvent("test event name",
                "test event location",
                LocalDateTime.parse("2024-04-01T13:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                10L,
                "test event contents",
                1L);
        event.setId(1L);
        var headerDescriptions = new HeaderDescriptor[]{
                HeaderDocumentation.headerWithName(HttpHeaders.AUTHORIZATION).description("accessToken"),
        };
        var requestFieldDescriptions =
                new FieldDescriptor[]{
                        fieldWithPath("eventName").type(STRING).description("eventName"),
                        fieldWithPath("eventLocation").type(STRING).description("eventLocation"),
                        fieldWithPath("eventHeldAt").type(STRING).description("eventHeldAt"),
                        fieldWithPath("maxJoinCount").type(NUMBER).description("maxJoinCount"),
                        fieldWithPath("eventContents").type(STRING).description("eventContents"),
                };
        var responseFieldDescriptor = new FieldDescriptor[]{
                fieldWithPath("data").type(NUMBER).description("id of new event"),
                fieldWithPath("meta").type(NULL).description("meta data"),
        };
        var operationIdentifier = "api-post-event";
        Mockito.when(commandHandler.handle(any(EventCreateCommand.class))).thenReturn(event);

        MockMvcFactory.getRestDocsMockMvc(contextProvider, eventController)
                .perform(
                        RestDocumentationRequestBuilders.post("/api/v1/events")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.AUTHORIZATION, accessToken)
                                .content(requestJson)

                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document(
                        operationIdentifier,
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestHeaders(headerDescriptions),
                        requestFields(requestFieldDescriptions),
                        responseFields(responseFieldDescriptor)))
                .andDo(
                        MockMvcRestDocumentationWrapper.document(
                                operationIdentifier,
                                getDocumentRequest(),
                                getDocumentResponse(),
                                ResourceDocumentation.resource(
                                        ResourceSnippetParameters.builder()
                                                .requestHeaders(headerDescriptions)
                                                .requestFields(requestFieldDescriptions)
                                                .responseFields(responseFieldDescriptor)
                                                .build()))
                );

    }

    @DisplayName("update event")
    @Test
    void updateEventRestDocsTest(RestDocumentationContextProvider contextProvider) throws Exception {

        var requestJson = """
                {
                    "eventName": "test event name",
                    "eventLocation": "test event location",
                    "eventHeldAt": "2024-04-01T13:00:00",
                    "maxJoinCount": 10,
                    "eventContents": "test event contents",
                    "participants" : [2,3,4]
                }
                """;

        var accessToken = UUID.randomUUID().toString();

        var event = Event.createEvent("test event name",
                "test event location",
                LocalDateTime.parse("2024-04-01T13:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                10L,
                "test event contents",
                1L);
        event.setId(1L);
        var headerDescriptions = new HeaderDescriptor[]{
                HeaderDocumentation.headerWithName(HttpHeaders.AUTHORIZATION).description("accessToken"),
        };
        var requestFieldDescriptions =
                new FieldDescriptor[]{
                        fieldWithPath("eventName").type(STRING).description("eventName"),
                        fieldWithPath("eventLocation").type(STRING).description("eventLocation"),
                        fieldWithPath("eventHeldAt").type(STRING).description("eventHeldAt"),
                        fieldWithPath("maxJoinCount").type(NUMBER).description("maxJoinCount"),
                        fieldWithPath("eventContents").type(STRING).description("eventContents"),
                        fieldWithPath("participants").type(ARRAY).description("Ids of Participants"),
                };
        var responseFieldDescriptor = new FieldDescriptor[]{
                fieldWithPath("data").type(NUMBER).description("id of updated event"),
                fieldWithPath("meta").type(NULL).description("meta data"),
        };
        var operationIdentifier = "api-put-event";
        Mockito.when(commandHandler.handle(any(EventUpdateCommand.class))).thenReturn(event);

        MockMvcFactory.getRestDocsMockMvc(contextProvider, eventController)
                .perform(
                        RestDocumentationRequestBuilders.put("/api/v1/events/{eventId}", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.AUTHORIZATION, accessToken)
                                .content(requestJson)

                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document(
                        operationIdentifier,
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestHeaders(headerDescriptions),
                        requestFields(requestFieldDescriptions),
                        responseFields(responseFieldDescriptor)))
                .andDo(
                        MockMvcRestDocumentationWrapper.document(
                                operationIdentifier,
                                getDocumentRequest(),
                                getDocumentResponse(),
                                ResourceDocumentation.resource(
                                        ResourceSnippetParameters.builder()
                                                .requestHeaders(headerDescriptions)
                                                .requestFields(requestFieldDescriptions)
                                                .responseFields(responseFieldDescriptor)
                                                .build()))
                );

    }


    @DisplayName("delete event")
    @Test
    void deleteEventRestDocsTest(RestDocumentationContextProvider contextProvider) throws Exception {


        var accessToken = UUID.randomUUID().toString();

        var event = Event.createEvent("test event name",
                "test event location",
                LocalDateTime.parse("2024-04-01T13:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                10L,
                "test event contents",
                1L);
        event.setId(1L);
        var headerDescriptions = new HeaderDescriptor[]{
                HeaderDocumentation.headerWithName(HttpHeaders.AUTHORIZATION).description("accessToken"),
        };

        var responseFieldDescriptor = new FieldDescriptor[]{
                fieldWithPath("data").type(NUMBER).description("id of deleted event"),
                fieldWithPath("meta").type(NULL).description("meta data"),
        };

        var operationIdentifier = "api-deleted-event";
        Mockito.when(commandHandler.handle(any(EventDeleteCommand.class))).thenReturn(event);

        MockMvcFactory.getRestDocsMockMvc(contextProvider, eventController)
                .perform(
                        RestDocumentationRequestBuilders.delete("/api/v1/events/{eventId}", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.AUTHORIZATION, accessToken)

                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document(
                        operationIdentifier,
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestHeaders(headerDescriptions),
                        responseFields(responseFieldDescriptor)))
                .andDo(
                        MockMvcRestDocumentationWrapper.document(
                                operationIdentifier,
                                getDocumentRequest(),
                                getDocumentResponse(),
                                ResourceDocumentation.resource(
                                        ResourceSnippetParameters.builder()
                                                .requestHeaders(headerDescriptions)
                                                .responseFields(responseFieldDescriptor)
                                                .build()))
                );

    }

    @DisplayName("cancel event")
    @Test
    void cancelEventRestDocsTest(RestDocumentationContextProvider contextProvider) throws Exception {


        var accessToken = UUID.randomUUID().toString();

        var event = Event.createEvent("test event name",
                "test event location",
                LocalDateTime.parse("2024-04-01T13:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                10L,
                "test event contents",
                1L);
        event.setId(1L);
        var headerDescriptions = new HeaderDescriptor[]{
                HeaderDocumentation.headerWithName(HttpHeaders.AUTHORIZATION).description("accessToken"),
        };

        var responseFieldDescriptor = new FieldDescriptor[]{
                fieldWithPath("data").type(NUMBER).description("id of canceled event"),
                fieldWithPath("meta").type(NULL).description("meta data"),
        };
        var operationIdentifier = "api-post-cancel-event";
        Mockito.when(commandHandler.handle(any(EventCancelCommand.class))).thenReturn(event);

        MockMvcFactory.getRestDocsMockMvc(contextProvider, eventController)
                .perform(
                        RestDocumentationRequestBuilders.post("/api/v1/events/{eventId}/cancel", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.AUTHORIZATION, accessToken)

                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document(
                        operationIdentifier,
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestHeaders(headerDescriptions),
                        responseFields(responseFieldDescriptor)))
                .andDo(
                        MockMvcRestDocumentationWrapper.document(
                                operationIdentifier,
                                getDocumentRequest(),
                                getDocumentResponse(),
                                ResourceDocumentation.resource(
                                        ResourceSnippetParameters.builder()
                                                .requestHeaders(headerDescriptions)
                                                .responseFields(responseFieldDescriptor)
                                                .build()))
                );

    }

    @DisplayName("register event")
    @Test
    void registerEventRestDocsTest(RestDocumentationContextProvider contextProvider) throws Exception {


        var accessToken = UUID.randomUUID().toString();

        var event = Event.createEvent("test event name",
                "test event location",
                LocalDateTime.parse("2024-04-01T13:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                10L,
                "test event contents",
                1L);
        event.setId(1L);
        var headerDescriptions = new HeaderDescriptor[]{
                HeaderDocumentation.headerWithName(HttpHeaders.AUTHORIZATION).description("accessToken"),
        };

        var responseFieldDescriptor = new FieldDescriptor[]{
                fieldWithPath("data").type(NUMBER).description("id of registered event"),
                fieldWithPath("meta").type(NULL).description("meta data"),
        };
        var operationIdentifier = "api-post-register-event";
        Mockito.when(commandHandler.handle(any(EventRegisterCommand.class))).thenReturn(event);

        MockMvcFactory.getRestDocsMockMvc(contextProvider, eventController)
                .perform(
                        RestDocumentationRequestBuilders.post("/api/v1/events/{eventId}/register", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.AUTHORIZATION, accessToken)

                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document(
                        operationIdentifier,
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestHeaders(headerDescriptions),
                        responseFields(responseFieldDescriptor)))
                .andDo(
                        MockMvcRestDocumentationWrapper.document(
                                operationIdentifier,
                                getDocumentRequest(),
                                getDocumentResponse(),
                                ResourceDocumentation.resource(
                                        ResourceSnippetParameters.builder()
                                                .requestHeaders(headerDescriptions)
                                                .responseFields(responseFieldDescriptor)
                                                .build()))
                );

    }

    @DisplayName("search event")
    @Test
    void searchEventRestDocsTest(RestDocumentationContextProvider contextProvider) throws Exception {


        var event1 = Event.createEvent("test event1 name",
                "test event1 location",
                LocalDateTime.parse("2024-04-01T13:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                10L,
                "test event1 contents",
                1L);
        event1.setId(1L);

        var event2 = Event.createEvent("test event2 name",
                "test event2 location",
                LocalDateTime.parse("2024-04-01T15:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                15L,
                "test event2 contents",
                2L);
        event2.setId(1L);


        var queryParameterDescriptor= new ParameterDescriptor[]{
                parameterWithName("keyword").description("search keyword")
        };
        var responseFieldDescriptor = new FieldDescriptor[]{
                fieldWithPath("data").type(ARRAY).description("events"),
                fieldWithPath("data[].id").type(NUMBER).description("id of event"),
                fieldWithPath("data[].eventName").type(STRING).description("eventName of  event"),
                fieldWithPath("data[].eventLocation").type(STRING).description("eventLocation of  event"),
                fieldWithPath("data[].eventHeldAt").type(STRING).description("eventHeldAt of  event"),
                fieldWithPath("data[].maxJoinCount").type(NUMBER).description("maxJoinCount of  event"),
                fieldWithPath("meta").type(NULL).description("meta data"),
        };
        var operationIdentifier = "api-get-search-event";
        Mockito.when(queryService.searchEvents(any(String.class))).thenReturn(Stream.of(event1,event2).map(EventListView::from).toList());

        MockMvcFactory.getRestDocsMockMvc(contextProvider, eventController)
                .perform(
                        RestDocumentationRequestBuilders.get("/api/v1/events")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("keyword", "test")

                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document(
                        operationIdentifier,
                        getDocumentRequest(),
                        getDocumentResponse(),
                        queryParameters(queryParameterDescriptor),
                        responseFields(responseFieldDescriptor)))
                .andDo(
                        MockMvcRestDocumentationWrapper.document(
                                operationIdentifier,
                                getDocumentRequest(),
                                getDocumentResponse(),
                                ResourceDocumentation.resource(
                                        ResourceSnippetParameters.builder()
                                                .queryParameters(queryParameterDescriptor)
                                                .responseFields(responseFieldDescriptor)
                                                .build()))
                );

    }

    @DisplayName("get event detail")
    @Test
    void getEventDetailRestDocsTest(RestDocumentationContextProvider contextProvider) throws Exception {


        var event2 = Event.createEvent("test event2 name",
                "test event2 location",
                LocalDateTime.parse("2024-04-01T15:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                15L,
                "test event2 contents",
                2L);
        event2.setId(1L);


        var responseFieldDescriptor = new FieldDescriptor[]{
                fieldWithPath("data").type(OBJECT).description("event detail view"),
                fieldWithPath("data.id").type(NUMBER).description("id of event"),
                fieldWithPath("data.eventName").type(STRING).description("eventName of  event"),
                fieldWithPath("data.eventLocation").type(STRING).description("eventLocation of  event"),
                fieldWithPath("data.eventHeldAt").type(STRING).description("eventHeldAt of  event"),
                fieldWithPath("data.maxJoinCount").type(NUMBER).description("maxJoinCount of  event"),
                fieldWithPath("data.eventContents").type(STRING).description("eventContents of  event"),
                fieldWithPath("meta").type(NULL).description("meta data"),
        };
        var operationIdentifier = "api-get-event-detail";
        Mockito.when(queryService.getEventDetail(any(Long.class))).thenReturn(EventDetailView.from(event2));

        MockMvcFactory.getRestDocsMockMvc(contextProvider, eventController)
                .perform(
                        RestDocumentationRequestBuilders.get("/api/vi/events/{eventId}",1L)
                                .contentType(MediaType.APPLICATION_JSON)

                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document(
                        operationIdentifier,
                        getDocumentRequest(),
                        getDocumentResponse(),
                        responseFields(responseFieldDescriptor)))
                .andDo(
                        MockMvcRestDocumentationWrapper.document(
                                operationIdentifier,
                                getDocumentRequest(),
                                getDocumentResponse(),
                                ResourceDocumentation.resource(
                                        ResourceSnippetParameters.builder()
                                                .responseFields(responseFieldDescriptor)
                                                .build()))
                );

    }

    @DisplayName("get my event")
    @Test
    void getMyEventsRestDocsTest(RestDocumentationContextProvider contextProvider) throws Exception {
        var accessToken = UUID.randomUUID().toString();


        var headerDescriptions = new HeaderDescriptor[]{
                HeaderDocumentation.headerWithName(HttpHeaders.AUTHORIZATION).description("accessToken"),
        };

        var event1 = Event.createEvent("test event1 name",
                "test event1 location",
                LocalDateTime.parse("2024-04-01T13:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                10L,
                "test event1 contents",
                1L);
        event1.setId(1L);

        var event2 = Event.createEvent("test event2 name",
                "test event2 location",
                LocalDateTime.parse("2024-04-01T15:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                15L,
                "test event2 contents",
                2L);
        event2.setId(1L);


        var responseFieldDescriptor = new FieldDescriptor[]{
                fieldWithPath("data").type(ARRAY).description("events"),
                fieldWithPath("data[].id").type(NUMBER).description("id of event"),
                fieldWithPath("data[].eventName").type(STRING).description("eventName of  event"),
                fieldWithPath("data[].eventLocation").type(STRING).description("eventLocation of  event"),
                fieldWithPath("data[].eventHeldAt").type(STRING).description("eventHeldAt of  event"),
                fieldWithPath("data[].maxJoinCount").type(NUMBER).description("maxJoinCount of  event"),
                fieldWithPath("meta").type(NULL).description("meta data"),
        };
        var operationIdentifier = "api-get-my-events";
        Mockito.when(queryService.getMyEvents(any(String.class))).thenReturn(Stream.of(event1,event2).map(EventListView::from).toList());

        MockMvcFactory.getRestDocsMockMvc(contextProvider, eventController)
                .perform(
                        RestDocumentationRequestBuilders.get("/api/v1/events/my-events")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.AUTHORIZATION, accessToken)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document(
                        operationIdentifier,
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestHeaders(headerDescriptions),
                        responseFields(responseFieldDescriptor)))
                .andDo(
                        MockMvcRestDocumentationWrapper.document(
                                operationIdentifier,
                                getDocumentRequest(),
                                getDocumentResponse(),
                                ResourceDocumentation.resource(
                                        ResourceSnippetParameters.builder()
                                                .requestHeaders(headerDescriptions)
                                                .responseFields(responseFieldDescriptor)
                                                .build()))
                );

    }
}