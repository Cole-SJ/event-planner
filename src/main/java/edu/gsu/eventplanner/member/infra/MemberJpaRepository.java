package edu.gsu.eventplanner.member.infra;

import edu.gsu.eventplanner.member.domain.Member;
import edu.gsu.eventplanner.member.domain.repository.MemberRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberJpaRepository extends JpaRepository<Member, Long>, MemberRepository {
}
