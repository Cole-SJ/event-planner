package edu.gsu.eventplanner.web;

import com.epages.restdocs.apispec.ResourceDocumentation;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import edu.gsu.eventplanner.annotation.RestDocsTest;
import edu.gsu.eventplanner.config.MockMvcFactory;
import edu.gsu.eventplanner.member.application.MemberCommandHandler;
import edu.gsu.eventplanner.member.application.MemberQueryService;
import edu.gsu.eventplanner.member.application.command.MemberLoginCommand;
import edu.gsu.eventplanner.member.application.command.MemberLogoutCommand;
import edu.gsu.eventplanner.member.application.command.MemberSignupCommand;
import edu.gsu.eventplanner.member.application.dto.MemberDetailView;
import edu.gsu.eventplanner.member.application.dto.MemberFindEmailView;
import edu.gsu.eventplanner.member.application.dto.MemberFindPasswordView;
import edu.gsu.eventplanner.member.application.dto.MemberMyDetailView;
import edu.gsu.eventplanner.member.domain.Member;
import edu.gsu.eventplanner.member.ui.MemberController;
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
import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;

import java.util.UUID;

import static edu.gsu.eventplanner.config.DocumentUtils.getDocumentRequest;
import static edu.gsu.eventplanner.config.DocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;

@RestDocsTest
public class MemberControllerTest {

    @InjectMocks
    private MemberController memberController;

    @Mock
    private MemberQueryService queryService;
    @Mock
    private MemberCommandHandler commandHandler;


    @DisplayName("member sign up")
    @Test
    void signupRestDocsTest(RestDocumentationContextProvider contextProvider) throws Exception {

        var requestJson = """
                {
                    "username": "test",
                    "nickname": "test1",
                    "password": "qwert12345",
                    "email": "test@gmail.com",
                    "contactNumber": "010-1111-1111",
                    "bio": "test account"
                }
                """;
        var requestFieldDescriptions =
                new FieldDescriptor[]{
                        fieldWithPath("username").type(STRING).description("username for new Member"),
                        fieldWithPath("nickname").type(STRING).description("nickname for new Member"),
                        fieldWithPath("password").type(STRING).description("password for new Member"),
                        fieldWithPath("email").type(STRING).description("email for new Member"),
                        fieldWithPath("contactNumber").type(STRING).description("contactNumber for new Member"),
                        fieldWithPath("bio").type(STRING).description("bio for new Member"),
                };
        var responseFieldDescriptor = new FieldDescriptor[]{
                fieldWithPath("data").type(STRING).description("email of new Member"),
                fieldWithPath("meta").type(NULL).description("meta data"),
        };
        var operationIdentifier = "api-post-signup";
        var member = Member.signup(null, null, null, "test@gmail.com", null, null);
        Mockito.when(commandHandler.handle(any(MemberSignupCommand.class))).thenReturn(member);

        MockMvcFactory.getRestDocsMockMvc(contextProvider, memberController)
                .perform(
                        RestDocumentationRequestBuilders.post("/api/v1/members")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)

                )
                //.header(HttpHeaders.AUTHORIZATION, ADMIN_TEST_AUTHORIZATION_KEY)
                //..header(HttpHeaders.ORIGIN, ORIGIN_3P_INTERNAL))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document(
                        operationIdentifier,
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(requestFieldDescriptions),
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
    @DisplayName("member login")
    @Test
    void loginRestDocsTest(RestDocumentationContextProvider contextProvider) throws Exception {

        var requestJson = """
                {
                    "email": "test@gmail.com",
                    "password": "qwert12345"
                }
                """;
        var requestFieldDescriptions =
                new FieldDescriptor[]{
                        fieldWithPath("email").type(STRING).description("email for login"),
                        fieldWithPath("password").type(STRING).description("password for login"),
                };
        var responseFieldDescriptor = new FieldDescriptor[]{
                fieldWithPath("data").type(STRING).description("accessToken of member"),
                fieldWithPath("meta").type(NULL).description("meta data"),
        };
        var operationIdentifier = "api-post-login";
        var member = Member.signup(null, null, null, "test@gmail.com", null, null);
        member.login();
        Mockito.when(commandHandler.handle(any(MemberLoginCommand.class))).thenReturn(member);

        MockMvcFactory.getRestDocsMockMvc(contextProvider, memberController)
                .perform(
                        RestDocumentationRequestBuilders.post("/api/v1/members/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)

                )
                //.header(HttpHeaders.AUTHORIZATION, ADMIN_TEST_AUTHORIZATION_KEY)
                //..header(HttpHeaders.ORIGIN, ORIGIN_3P_INTERNAL))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document(
                        operationIdentifier,
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(requestFieldDescriptions),
                        responseFields(responseFieldDescriptor)))
                .andDo(
                        MockMvcRestDocumentationWrapper.document(
                                operationIdentifier,
                                getDocumentRequest(),
                                getDocumentResponse(),
                                ResourceDocumentation.resource(
                                        ResourceSnippetParameters.builder()
                                                .requestFields(requestFieldDescriptions)
                                                .responseFields(responseFieldDescriptor)
                                                .build()))
                );

    }
    @DisplayName("member logout")
    @Test
    void logoutRestDocsTest(RestDocumentationContextProvider contextProvider) throws Exception {

        var accessToken = UUID.randomUUID().toString();

        var operationIdentifier = "api-post-logout";
        var member = Member.signup(null, null, null, "test@gmail.com", null, null);
        var headerDescriptions = new HeaderDescriptor[]{
                HeaderDocumentation.headerWithName(HttpHeaders.AUTHORIZATION).description("accessToken"),
        };
        Mockito.when(commandHandler.handle(any(MemberLogoutCommand.class))).thenReturn(member);

        MockMvcFactory.getRestDocsMockMvc(contextProvider, memberController)
                .perform(RestDocumentationRequestBuilders.post("/api/v1/members/logout")
                .header(HttpHeaders.AUTHORIZATION, accessToken))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document(
                        operationIdentifier,
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestHeaders(headerDescriptions)
                       ))
                .andDo(
                        MockMvcRestDocumentationWrapper.document(
                                operationIdentifier,
                                getDocumentRequest(),
                                getDocumentResponse(),
                                ResourceDocumentation.resource(
                                        ResourceSnippetParameters.builder()
                                                .requestHeaders(headerDescriptions)
                                                .build()))
                );
    }
    @DisplayName("get my detail view")
    @Test
    void getMyDetailRestDocsTest(RestDocumentationContextProvider contextProvider) throws Exception {

        var accessToken = UUID.randomUUID().toString();

        var operationIdentifier = "api-get-my-detail";
        var headerDescriptions = new HeaderDescriptor[]{
                HeaderDocumentation.headerWithName(HttpHeaders.AUTHORIZATION).description("accessToken"),
        };
        var member = Member.signup("test", "test1", "qwert12345", "test@gmail.com", "010-1111-1111", "test account");
        var responseFieldDescriptor = new FieldDescriptor[]{
                fieldWithPath("data").type(OBJECT).description("my detail view"),
                fieldWithPath("data.username").type(STRING).description("username"),
                fieldWithPath("data.nickname").type(STRING).description("nickname"),
                fieldWithPath("data.email").type(STRING).description("email"),
                fieldWithPath("data.contactNumber").type(STRING).description("contactNumber"),
                fieldWithPath("data.bio").type(STRING).description("bio"),
                fieldWithPath("meta").type(NULL).description("meta data"),
        };
        Mockito.when(queryService.findMyDetail(accessToken)).thenReturn(MemberMyDetailView.from(member));

        MockMvcFactory.getRestDocsMockMvc(contextProvider, memberController)
                .perform(RestDocumentationRequestBuilders.get("/api/v1/members")
                        .header(HttpHeaders.AUTHORIZATION, accessToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document(
                        operationIdentifier,
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestHeaders(headerDescriptions),
                        responseFields(responseFieldDescriptor)
                ))
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

    @DisplayName("find login email")
    @Test
    void findMyLoginEmailRestDocsTest(RestDocumentationContextProvider contextProvider) throws Exception {


        var parameterDescriptors =
                new ParameterDescriptor[]{
                        parameterWithName("contactNumber").description("contactNumber"),
                };
        var responseFieldDescriptor = new FieldDescriptor[]{
                fieldWithPath("data").type(OBJECT).description("find login email view"),
                fieldWithPath("data.email").type(STRING).description("email"),
                fieldWithPath("meta").type(NULL).description("meta data"),
        };
        var operationIdentifier = "api-get-login-email";
        Mockito.when(queryService.findMyEmail(any(String.class))).thenReturn(new MemberFindEmailView("test@gmai.com"));

        MockMvcFactory.getRestDocsMockMvc(contextProvider, memberController)
                .perform(
                        RestDocumentationRequestBuilders.get("/api/v1/members/email")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("contactNumber", "010-1111-1111")

                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document(
                        operationIdentifier,
                        getDocumentRequest(),
                        getDocumentResponse(),
                        queryParameters(parameterDescriptors),
                        responseFields(responseFieldDescriptor)))
                .andDo(
                        MockMvcRestDocumentationWrapper.document(
                                operationIdentifier,
                                getDocumentRequest(),
                                getDocumentResponse(),
                                ResourceDocumentation.resource(
                                        ResourceSnippetParameters.builder()
                                                .queryParameters(parameterDescriptors)
                                                .responseFields(responseFieldDescriptor)
                                                .build()))
                );

    }

    @DisplayName("find login password")
    @Test
    void findMyLoginPasswordRestDocsTest(RestDocumentationContextProvider contextProvider) throws Exception {


        var parameterDescriptors =
                new ParameterDescriptor[]{
                        parameterWithName("email").description("email"),
                        parameterWithName("username").description("username"),
                        parameterWithName("contactNumber").description("contactNumber"),

                };
        var responseFieldDescriptor = new FieldDescriptor[]{
                fieldWithPath("data").type(OBJECT).description("find login password view"),
                fieldWithPath("data.password").type(STRING).description("password"),
                fieldWithPath("meta").type(NULL).description("meta data"),
        };
        var operationIdentifier = "api-get-login-password";
        Mockito.when(queryService.findMyPassword(any(String.class),any(String.class),any(String.class))).thenReturn(new MemberFindPasswordView("qwert12345!"));

        MockMvcFactory.getRestDocsMockMvc(contextProvider, memberController)
                .perform(
                        RestDocumentationRequestBuilders.get("/api/v1/members/password")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("contactNumber", "010-1111-1111")
                                .param("username", "testUser")
                                .param("email", "test@gmail.com")

                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document(
                        operationIdentifier,
                        getDocumentRequest(),
                        getDocumentResponse(),
                        queryParameters(parameterDescriptors),
                        responseFields(responseFieldDescriptor)))
                .andDo(
                        MockMvcRestDocumentationWrapper.document(
                                operationIdentifier,
                                getDocumentRequest(),
                                getDocumentResponse(),
                                ResourceDocumentation.resource(
                                        ResourceSnippetParameters.builder()
                                                .queryParameters(parameterDescriptors)
                                                .responseFields(responseFieldDescriptor)
                                                .build()))
                );

    }

    @DisplayName("find member detail")
    @Test
    void findMemberDetailRestDocsTest(RestDocumentationContextProvider contextProvider) throws Exception {
        var accessToken = UUID.randomUUID().toString();
        var headerDescriptions = new HeaderDescriptor[]{
                HeaderDocumentation.headerWithName(HttpHeaders.AUTHORIZATION).description("accessToken"),
        };

        var parameterDescriptors =
                new ParameterDescriptor[]{
                        parameterWithName("email").description("email"),

                };
        var responseFieldDescriptor = new FieldDescriptor[]{
                fieldWithPath("data").type(OBJECT).description("find other member detail view"),
                fieldWithPath("data.nickname").type(STRING).description("nickname"),
                fieldWithPath("data.email").type(STRING).description("email"),
                fieldWithPath("data.bio").type(STRING).description("bio"),
                fieldWithPath("meta").type(NULL).description("meta data"),
        };
        var operationIdentifier = "api-get-other-member-details";
        Mockito.when(queryService.findByEmail(any(String.class),any(String.class))).thenReturn(new MemberDetailView("test","test@gmail.com","test account"));

        MockMvcFactory.getRestDocsMockMvc(contextProvider, memberController)
                .perform(
                        RestDocumentationRequestBuilders.get("/api/v1/members/member-detail")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("email", "myEmail@gmail.com")
                                .header(HttpHeaders.AUTHORIZATION, accessToken)

                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document(
                        operationIdentifier,
                        getDocumentRequest(),
                        getDocumentResponse(),
                        queryParameters(parameterDescriptors),
                        requestHeaders(headerDescriptions),
                        responseFields(responseFieldDescriptor)))
                .andDo(
                        MockMvcRestDocumentationWrapper.document(
                                operationIdentifier,
                                getDocumentRequest(),
                                getDocumentResponse(),
                                ResourceDocumentation.resource(
                                        ResourceSnippetParameters.builder()
                                                .queryParameters(parameterDescriptors)
                                                .requestHeaders(headerDescriptions)
                                                .responseFields(responseFieldDescriptor)
                                                .build()))
                );

    }
}