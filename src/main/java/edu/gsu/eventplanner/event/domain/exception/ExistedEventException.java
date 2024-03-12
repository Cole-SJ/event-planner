package edu.gsu.eventplanner.event.domain.exception;

public class ExistedEventException extends RuntimeException{
    public ExistedEventException(){
        super("이미 존재하는 이벤트 이름입니다.");
    }
}
