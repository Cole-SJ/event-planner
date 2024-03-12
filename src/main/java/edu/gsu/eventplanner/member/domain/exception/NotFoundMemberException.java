package edu.gsu.eventplanner.member.domain.exception;

public class NotFoundMemberException extends RuntimeException {
    public NotFoundMemberException() {
        super("회원정보가 없습니다.");
    }
}
