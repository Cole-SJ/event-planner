package edu.gsu.eventplanner.member.application.command;

import edu.gsu.eventplanner.member.domain.Member;
import edu.gsu.eventplanner.member.domain.exception.InvalidPasswordException;
import edu.gsu.eventplanner.member.domain.exception.NotFoundMemberException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

@AllArgsConstructor
@Getter
public class MemberLoginCommand {
    private String inputLoginEmail;
    private String inputPassword;


    public Member execute(Optional<Member> optionalMember) {
        if (optionalMember.isEmpty()) {
            throw new NotFoundMemberException();
        }

        Member member = optionalMember.get();

        if (!member.isValidPassword(inputPassword)) {
            throw new InvalidPasswordException();
        }

        member.login();

        return member;
    }
}
