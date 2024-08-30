package com.meeting.scheduler.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
@Getter
@Setter
public class MeetingRequestDTO {
    private String title;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long roomId;
    private Long organizerId;
    private List<Long> participantIds;
}
