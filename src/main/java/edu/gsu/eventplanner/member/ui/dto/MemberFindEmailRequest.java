package edu.gsu.eventplanner.member.ui.dto;

import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public record MemberFindEmailRequest(
        @Length(max = 20)
        @Pattern(regexp = "^\\d{3}-\\d{3,4}-\\d{4}$")
        String contactNumber
) {
}
