package edu.gsu.eventplanner.member.domain.exception;

public class NotMatchInputParameterException extends RuntimeException {
    public NotMatchInputParameterException() {
        super("입력 정보가 일치하지 않습니다.");
    }
}
