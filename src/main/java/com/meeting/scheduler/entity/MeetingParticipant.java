package com.meeting.scheduler.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "meeting_participants")
@IdClass(MeetingParticipantId.class)
public class MeetingParticipant {
    @Id
    @ManyToOne
    @JoinColumn(name = "meeting_id")
    private Meeting meeting;

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}

