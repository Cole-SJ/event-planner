package edu.gsu.eventplanner.event.domain.exception;

public class NotMyEventException extends RuntimeException {
    public NotMyEventException() {
        super("내가 생성한 이벤트가 아닙니다.");
    }
}
