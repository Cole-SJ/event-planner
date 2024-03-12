package edu.gsu.eventplanner.member.ui;


import edu.gsu.eventplanner.common.response.EventPlannerResponse;
import edu.gsu.eventplanner.member.application.MemberCommandHandler;
import edu.gsu.eventplanner.member.application.MemberQueryService;
import edu.gsu.eventplanner.member.application.command.MemberLogoutCommand;
import edu.gsu.eventplanner.member.application.dto.MemberDetailView;
import edu.gsu.eventplanner.member.application.dto.MemberFindEmailView;
import edu.gsu.eventplanner.member.application.dto.MemberFindPasswordView;
import edu.gsu.eventplanner.member.application.dto.MemberMyDetailView;
import edu.gsu.eventplanner.member.ui.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberQueryService queryService;
    private final MemberCommandHandler commandHandler;


    @PostMapping("/api/v1/members")
    @ResponseStatus(HttpStatus.CREATED)
    public EventPlannerResponse<String> signup(@RequestBody @Valid MemberSignupRequest request) {
        var member = commandHandler.handle(request.toCommand());
        return new EventPlannerResponse<>(member.getEmail(), null);
    }

    @PostMapping("/api/v1/members/login")
    public EventPlannerResponse<String> login(@RequestBody @Valid MemberLoginRequest request) {
        var member = commandHandler.handle(request.toCommand());
        return new EventPlannerResponse<>(member.getAccessToken(), null);
    }

    @PostMapping("/api/v1/members/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(@RequestHeader("Authorization") String accessToken) {
        commandHandler.handle(new MemberLogoutCommand(accessToken));
    }

    @GetMapping("/api/v1/members")
    public EventPlannerResponse<MemberMyDetailView> findMyDetail(@RequestHeader("Authorization") String accessToken) {
        var myDetailView = queryService.findMyDetail(accessToken);
        return new EventPlannerResponse<>(myDetailView, null);
    }

    @GetMapping("/api/v1/members/email")
    public EventPlannerResponse<MemberFindEmailView> findMyLoginEmail(@ModelAttribute @Valid MemberFindEmailRequest request) {
        var findMyEmailView = queryService.findMyEmail(request.contactNumber());
        return new EventPlannerResponse<>(findMyEmailView, null);
    }

    @GetMapping("/api/v1/members/password")
    public EventPlannerResponse<MemberFindPasswordView> findMyPassword(@ModelAttribute @Valid MemberFindPasswordRequest request) {
        var findMyPasswordView = queryService.findMyPassword(request.email(), request.username(), request.contactNumber());
        return new EventPlannerResponse<>(findMyPasswordView, null);
    }

    @GetMapping("/api/v1/members/member-detail")
    public EventPlannerResponse<MemberDetailView> findByEmail(@RequestHeader("Authorization") String accessToken,
                                                              @ModelAttribute @Valid MemberFindMemberRequest request) {
        var memberDetailView = queryService.findByEmail(accessToken, request.email());
        return new EventPlannerResponse<>(memberDetailView, null);
    }
}
