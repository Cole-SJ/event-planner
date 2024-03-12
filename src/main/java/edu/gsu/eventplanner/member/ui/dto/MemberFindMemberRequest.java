package edu.gsu.eventplanner.member.ui.dto;

import jakarta.validation.constraints.Email;
import org.hibernate.validator.constraints.Length;

public record MemberFindMemberRequest(
        @Email
        @Length(max = 50)
        String email
) {
}
