package edu.gsu.eventplanner.member.application.dto;

import edu.gsu.eventplanner.member.domain.Member;

public record MemberMyDetailView(
        String username,
        String nickname,
        String email,
        String contactNumber,
        String bio
) {
    public static MemberMyDetailView from(Member member) {
        return new MemberMyDetailView(
                member.getUsername(),
                member.getNickname(),
                member.getEmail(),
                member.getContactNumber(),
                member.getBio()
        );
    }
}
