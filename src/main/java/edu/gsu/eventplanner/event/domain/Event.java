package edu.gsu.eventplanner.event.domain;

import edu.gsu.eventplanner.event.domain.converter.ParticipantsConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_name", columnDefinition = "VARCHAR(50) COMMENT '이벤트 이름'")
    private String eventName;

    @Column(name = "event_location", columnDefinition = "VARCHAR(150) COMMENT '이벤트 위치'")
    private String eventLocation;

    @Column(name = "event_held_at", columnDefinition = "DATETIME(6) COMMENT '이벤트 일시'")
    private LocalDateTime eventHeldAt;

    @Column(name = "max_join_count", columnDefinition = "BIGINT COMMENT '최대 모임 인원'")
    private Long maxJoinCount;

    @Column(name = "event_contents", columnDefinition = "VARCHAR(255) COMMENT '이벤트 소개'")
    private String eventContents;

    @Column(name = "created_by", columnDefinition = "BIGINT COMMENT '생성자'")
    private Long createdBy;

    @Convert(converter = ParticipantsConverter.class)
    @Column(name = "participants", columnDefinition = "LONGTEXT COMMENT '현재 이벤트 신청자'")
    private Set<Long> participants = new HashSet<>();

    @Column(name = "created_at", columnDefinition = "DATETIME(6) COMMENT '생성일시'")
    private LocalDateTime createdAt;


    public static Event createEvent(String eventName,
                                    String eventLocation,
                                    LocalDateTime eventHeldAt,
                                    Long maxJoinCount,
                                    String eventContents,
                                    Long createdBy) {
        var event = new Event();
        event.eventName = eventName;
        event.eventLocation = eventLocation;
        event.eventHeldAt = eventHeldAt;
        event.maxJoinCount = maxJoinCount;
        event.eventContents = eventContents;
        event.createdBy = createdBy;
        event.participants = new HashSet<>();
        event.createdAt = LocalDateTime.now();
        return event;
    }

    public void updateEvent(String eventName,
                            String eventLocation,
                            LocalDateTime eventHeldAt,
                            Long maxJoinCount,
                            String eventContents,
                            Set<Long> participants) {

        this.eventName= eventName;
        this.eventLocation = eventLocation;
        this.eventHeldAt = eventHeldAt;
        this.maxJoinCount = maxJoinCount;
        this.eventContents =eventContents;
        this.participants = participants;
    }

    public void cancelEvent(Long myId){
       this.participants.remove(myId);
    }

    public void registerEvent(Long myId){
        this.participants.add(myId);
    }

}
