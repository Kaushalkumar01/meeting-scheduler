package com.meeting.scheduler.service;

import com.meeting.scheduler.dto.MeetingRequestDTO;
import com.meeting.scheduler.entity.Meeting;
import com.meeting.scheduler.exception.CollisionDetectedException;
import com.meeting.scheduler.exception.MeetingNotFoundException;

import java.util.List;

public interface MeetingService {
    List<Meeting> getAllMeetings();
    Meeting getMeetingById(Long id) throws MeetingNotFoundException;
    Meeting createMeeting(MeetingRequestDTO meetingRequest) throws CollisionDetectedException;
    Meeting updateMeeting(Long id, MeetingRequestDTO meetingRequest) throws MeetingNotFoundException;
    void deleteMeeting(Long id) throws MeetingNotFoundException;
}
