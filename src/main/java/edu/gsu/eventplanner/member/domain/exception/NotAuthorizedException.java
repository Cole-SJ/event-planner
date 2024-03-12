package edu.gsu.eventplanner.member.domain.exception;

public class NotAuthorizedException extends RuntimeException {
    public NotAuthorizedException() {
        super("로그인 정보가 존재하지 않습니다.");
    }
}
