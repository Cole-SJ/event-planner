package edu.gsu.eventplanner.event.domain.exception;

public class NotRegisteredEventException extends RuntimeException{
    public NotRegisteredEventException(){
        super("참가 신청한 기록이 없는 이벤트입니다.");
    }
}
