package edu.gsu.eventplanner.event.ui.dto;

import edu.gsu.eventplanner.event.application.command.EventCreateCommand;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class EventCreateRequest {
    @Length(max = 50, message = "행사 이름은 최대 50글자 입니다.")
    @NotBlank(message = "행사 이름은 빈 값일 수 없습니다.")
    String eventName;
    @Length(max = 150, message = "행사 위치는 최대 150자 입니다.")
    @NotBlank(message = "행사 위치는 빈 값일 수 없습니다.")
    String eventLocation;
    @NotNull(message = "행사 일시는 필수값입니다.")
    LocalDateTime eventHeldAt;
    @NotNull(message = "행사참여인원은 필수값입니다.")
    @Max(value = 10, message = "행사 참여인원은 최대 10명입니다.")
    @Positive(message = "행사참여인원은 양수만 입력가능합니다.")
    Long maxJoinCount;
    @Length(max = 255, message = "행사내용은 최대 255글자 입니다.")
    @NotBlank(message = "행사 내용은 빈 값일 수 없습니다.")
    String eventContents;

    @AssertTrue(message = "현재 보다 이전 일정의 이벤트를 생성할 수 없습니다.")
    public boolean isValidEventHeldAt(){
        if(eventHeldAt.isBefore(LocalDateTime.now())){
            return false;
        }
        return true;
    }

    public EventCreateCommand toCommand(String accessToken) {
        return new EventCreateCommand(
                eventName,
                eventLocation,
                eventHeldAt,
                maxJoinCount,
                eventContents,
                accessToken
        );
    }
}
