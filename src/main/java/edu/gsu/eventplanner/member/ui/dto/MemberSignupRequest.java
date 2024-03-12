package edu.gsu.eventplanner.member.ui.dto;

import edu.gsu.eventplanner.member.application.command.MemberSignupCommand;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public record MemberSignupRequest(
        @Length(max = 50)
        String username,
        @Length(max = 50)
        String nickname,
        @Length(max = 30)
        String password,
        @Email
        @Length(max = 50)
        String email,
        @Length(max = 20)
        @Pattern(regexp = "^\\d{3}-\\d{3,4}-\\d{4}$")
        String contactNumber,
        @Length(max = 255)
        String bio
) {
    public MemberSignupCommand toCommand() {
        return new MemberSignupCommand(username, nickname, password, email, contactNumber, bio);
    }
}
