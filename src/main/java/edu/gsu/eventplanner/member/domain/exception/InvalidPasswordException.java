package edu.gsu.eventplanner.member.domain.exception;

public class InvalidPasswordException extends RuntimeException{
    public InvalidPasswordException() {
        super("비밀번호가 틀립니다.");
    }
}
