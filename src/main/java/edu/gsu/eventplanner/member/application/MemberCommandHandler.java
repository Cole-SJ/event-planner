package edu.gsu.eventplanner.member.application;

import edu.gsu.eventplanner.member.application.command.MemberLoginCommand;
import edu.gsu.eventplanner.member.application.command.MemberLogoutCommand;
import edu.gsu.eventplanner.member.application.command.MemberSignupCommand;
import edu.gsu.eventplanner.member.application.command.MemberUpdateDetailCommand;
import edu.gsu.eventplanner.member.domain.Member;
import edu.gsu.eventplanner.member.domain.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberCommandHandler {

    private final MemberRepository memberRepository;

    public MemberCommandHandler(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    //1. 회원가입
    @Transactional
    public Member handle(MemberSignupCommand command) {
        var findByEmailMember = memberRepository.findByEmail(command.getEmail());
        var findByContactNumberMember = memberRepository.findByContactNumber(command.getContactNumber());
        return memberRepository.save(command.execute(findByEmailMember, findByContactNumberMember));
    }

    //2. 로그인
    @Transactional
    public Member handle(MemberLoginCommand command) {
        var member = memberRepository.findByEmail(command.getInputLoginEmail());
        return memberRepository.save(command.execute(member));
    }

    //3. 로그아웃
    @Transactional
    public Member handle(MemberLogoutCommand command) {
        var member = memberRepository.findByAccessToken(command.getAccessToken());
        return memberRepository.save(command.execute(member));
    }

    //4. 내 정보 수정
    @Transactional
    public Member handle(MemberUpdateDetailCommand command) {
        var member = memberRepository.findByAccessToken(command.getAccessToken());
        return memberRepository.save(command.execute(member));
    }
}
