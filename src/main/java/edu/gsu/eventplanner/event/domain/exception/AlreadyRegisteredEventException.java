package edu.gsu.eventplanner.event.domain.exception;

public class AlreadyRegisteredEventException extends RuntimeException{
    public AlreadyRegisteredEventException(){
        super("이미 참가 신청을 완료한 이벤트입니다.");
    }
}
