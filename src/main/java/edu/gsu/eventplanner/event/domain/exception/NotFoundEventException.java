package edu.gsu.eventplanner.event.domain.exception;

public class NotFoundEventException extends RuntimeException{
    public NotFoundEventException() {
        super("이벤트 정보가 없습니다.");
    }
}
