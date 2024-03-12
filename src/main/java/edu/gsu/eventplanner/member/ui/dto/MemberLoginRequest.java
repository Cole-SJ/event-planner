package edu.gsu.eventplanner.member.ui.dto;

import edu.gsu.eventplanner.member.application.command.MemberLoginCommand;
import jakarta.validation.constraints.Email;
import org.hibernate.validator.constraints.Length;

public record MemberLoginRequest(
        @Email
        @Length(max = 50)
        String email,
        @Length(max = 30)
        String password
) {
    public MemberLoginCommand toCommand() {
        return new MemberLoginCommand(email, password);
    }
}
