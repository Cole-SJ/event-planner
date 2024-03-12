package edu.gsu.eventplanner.member.application.dto;

import edu.gsu.eventplanner.member.domain.Member;

public record MemberFindEmailView(
        String email
) {
    public static MemberFindEmailView from(Member member) {
        return new MemberFindEmailView(member.getEmail());
    }
}
