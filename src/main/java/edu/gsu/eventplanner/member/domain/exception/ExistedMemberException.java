package edu.gsu.eventplanner.member.domain.exception;

public class ExistedMemberException extends RuntimeException{
    public ExistedMemberException() {
        super("이미 가입된 회원입니다.");
    }
}
