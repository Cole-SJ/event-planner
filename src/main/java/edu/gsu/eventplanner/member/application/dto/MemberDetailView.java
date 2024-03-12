package edu.gsu.eventplanner.member.application.dto;

import edu.gsu.eventplanner.member.domain.Member;

public record MemberDetailView(
        String nickname,
        String email,
        String bio
) {
    public static MemberDetailView from(Member member) {
        return new MemberDetailView(
                member.getNickname(),
                member.getEmail(),
                member.getBio()
        );
    }
}
