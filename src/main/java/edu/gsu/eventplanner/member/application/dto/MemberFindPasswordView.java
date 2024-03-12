package edu.gsu.eventplanner.member.application.dto;

import edu.gsu.eventplanner.member.domain.Member;

public record MemberFindPasswordView(
        String password
) {
    public static MemberFindPasswordView from(Member member) {
        return new MemberFindPasswordView(member.getPassword());
    }
}
