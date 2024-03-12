package edu.gsu.eventplanner.member.domain.repository;

import edu.gsu.eventplanner.member.domain.Member;

import java.util.Optional;

public interface MemberRepository {

    Member save(Member member);

    Optional<Member> findByEmail(String email);

    Optional<Member> findByAccessToken(String accessToken);

    Optional<Member> findByContactNumber(String contactNumber);

    Optional<Member> findById(Long id);
}
