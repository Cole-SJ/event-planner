package edu.gsu.eventplanner.member.application;

import edu.gsu.eventplanner.member.application.dto.MemberDetailView;
import edu.gsu.eventplanner.member.application.dto.MemberFindEmailView;
import edu.gsu.eventplanner.member.application.dto.MemberFindPasswordView;
import edu.gsu.eventplanner.member.application.dto.MemberMyDetailView;
import edu.gsu.eventplanner.member.domain.exception.NotAuthorizedException;
import edu.gsu.eventplanner.member.domain.exception.NotFoundMemberException;
import edu.gsu.eventplanner.member.domain.exception.NotMatchInputParameterException;
import edu.gsu.eventplanner.member.domain.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class MemberQueryService {

    private final MemberRepository memberRepository;

    public MemberQueryService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    //1.내 정보 상세 조회
    @Transactional(readOnly = true)
    public MemberMyDetailView findMyDetail(String accessToken) {
        var optionalMember = memberRepository.findByAccessToken(accessToken);
        if (optionalMember.isEmpty()) {
            throw new NotAuthorizedException();
        }
        return MemberMyDetailView.from(optionalMember.get());
    }

    //2.다른 사람 정보 조회
    @Transactional(readOnly = true)
    public MemberDetailView findByEmail(String accessToken, String email) {
        memberRepository.findByAccessToken(accessToken).orElseThrow(NotAuthorizedException::new);
        var optionalMember = memberRepository.findByEmail(email);
        if (optionalMember.isEmpty()) {
            throw new NotFoundMemberException();
        }
        return MemberDetailView.from(optionalMember.get());
    }

    //3. 내 이메일 찾기
    @Transactional(readOnly = true)
    public MemberFindEmailView findMyEmail(String contactNumber) {
        var optionalMember = memberRepository.findByContactNumber(contactNumber);
        if (optionalMember.isEmpty()) {
            throw new NotFoundMemberException();
        }
        return MemberFindEmailView.from(optionalMember.get());
    }

    //4. 내 비밀번호 찾기
    @Transactional(readOnly = true)
    public MemberFindPasswordView findMyPassword(String email, String username, String contactNumber) {
        var optionalMember = memberRepository.findByEmail(email);

        if (optionalMember.isEmpty()) {
            throw new NotFoundMemberException();
        }

        var member = optionalMember.get();

        if (!member.getUsername().equals(username)) {
            throw new NotMatchInputParameterException();
        }

        if (!member.getContactNumber().equals(contactNumber)) {
            throw new NotMatchInputParameterException();
        }

        return MemberFindPasswordView.from(member);

    }
}
