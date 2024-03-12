package edu.gsu.eventplanner.member.application.command;

import edu.gsu.eventplanner.member.domain.Member;
import edu.gsu.eventplanner.member.domain.exception.NotAuthorizedException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

@AllArgsConstructor
@Getter
public class MemberLogoutCommand {
    private String accessToken;


    public Member execute(Optional<Member> optionalMember){
        if(optionalMember.isEmpty()){
            throw new NotAuthorizedException();
        }

        Member member = optionalMember.get();
        member.logout();

        return member;
    }
}
