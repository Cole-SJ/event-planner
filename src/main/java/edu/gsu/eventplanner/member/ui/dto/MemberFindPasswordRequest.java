package edu.gsu.eventplanner.member.ui.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public record MemberFindPasswordRequest(
        @Email
        @Length(max = 50)
        String email,
        @Length(max = 50)
        String username,
        @Length(max = 20)
        @Pattern(regexp = "^\\d{3}-\\d{3,4}-\\d{4}$")
        String contactNumber
) {
}
