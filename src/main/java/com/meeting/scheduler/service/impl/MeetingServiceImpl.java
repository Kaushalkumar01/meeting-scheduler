package com.meeting.scheduler.service.impl;

import com.meeting.scheduler.dto.MeetingRequestDTO;
import com.meeting.scheduler.entity.Meeting;
import com.meeting.scheduler.entity.Room;
import com.meeting.scheduler.entity.User;
import com.meeting.scheduler.exception.CollisionDetectedException;
import com.meeting.scheduler.exception.MeetingNotFoundException;
import com.meeting.scheduler.repository.MeetingRepository;
import com.meeting.scheduler.repository.RoomRepository;
import com.meeting.scheduler.repository.UserRepository;
import com.meeting.scheduler.service.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class MeetingServiceImpl implements MeetingService {
    @Autowired
    private MeetingRepository meetingRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Meeting> getAllMeetings() {
        return meetingRepository.findAll();
    }

    @Override
    public Meeting getMeetingById(Long id) throws MeetingNotFoundException {
        return meetingRepository.findById(id)
                .orElseThrow(() -> new MeetingNotFoundException(id));
    }

    @Override
    public Meeting createMeeting(MeetingRequestDTO meetingRequest) throws CollisionDetectedException {
        Room room = roomRepository.findById(meetingRequest.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid room ID"));

        User organizer = userRepository.findById(meetingRequest.getOrganizerId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid organizer ID"));

        List<User> participants = userRepository.findAllById(meetingRequest.getParticipantIds());

        Meeting meeting = new Meeting();
        meeting.setTitle(meetingRequest.getTitle());
        meeting.setStartTime(meetingRequest.getStartTime());
        meeting.setEndTime(meetingRequest.getEndTime());
        meeting.setRoom(room);
        meeting.setOrganizer(organizer);
        meeting.setParticipants(participants);
        checkRoomCollision(meeting);
        checkRoomCapacity(meeting);
        checkParticipantAvailability(meeting);
        return meetingRepository.save(meeting);
    }

    @Override
    public Meeting updateMeeting(Long id, MeetingRequestDTO meetingRequest) throws MeetingNotFoundException {
        Room room = roomRepository.findById(meetingRequest.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid room ID"));

        User organizer = userRepository.findById(meetingRequest.getOrganizerId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid organizer ID"));

        List<User> participants = userRepository.findAllById(meetingRequest.getParticipantIds());
        Meeting meeting = meetingRepository.findById(id)
                .orElseThrow(() -> new MeetingNotFoundException(id));
        List<Long> existingParticipantIds = meeting.getParticipants().stream()
                .map(User::getUserId)
                .toList();
        meeting.setTitle(meetingRequest.getTitle());
        meeting.setStartTime(meetingRequest.getStartTime());
        meeting.setEndTime(meetingRequest.getEndTime());
        meeting.setRoom(room);
        meeting.setOrganizer(organizer);
        meeting.setParticipants(participants);
        if(!Objects.equals(meetingRequest.getRoomId(), meeting.getRoom().getRoomId())) {
            checkRoomCollision(meeting);
        }
        checkRoomCapacity(meeting);
        if(!meetingRequest.getParticipantIds().equals(existingParticipantIds)){
            checkParticipantAvailability(meeting);
        }
        return meetingRepository.save(meeting);
    }

    @Override
    public void deleteMeeting(Long id) throws MeetingNotFoundException {
        Meeting meeting = meetingRepository.findById(id)
                .orElseThrow(() -> new MeetingNotFoundException(id));
        meetingRepository.delete(meeting);
    }

    private void checkRoomCollision(Meeting newMeeting) throws CollisionDetectedException {
        List<Meeting> existingMeetings = meetingRepository.findByRoomRoomId(newMeeting.getRoom().getRoomId());
        for (Meeting meeting : existingMeetings) {
            if (newMeeting.getStartTime().isBefore(meeting.getEndTime()) &&
                    newMeeting.getEndTime().isAfter(meeting.getStartTime())) {
                throw new CollisionDetectedException("Meeting collision detected in room " + newMeeting.getRoom().getRoomName() +
                        " from " + newMeeting.getStartTime() + " to " + newMeeting.getEndTime());
            }
        }
    }

    private void checkRoomCapacity(Meeting newMeeting) throws CollisionDetectedException {
        if (newMeeting.getParticipants().size() > newMeeting.getRoom().getCapacity()) {
            throw new CollisionDetectedException("Meeting room capacity exceeded. Room " + newMeeting.getRoom().getRoomName() +
                    " can only accommodate " + newMeeting.getRoom().getCapacity() + " participants.");
        }
    }

    private void checkParticipantAvailability(Meeting newMeeting) throws CollisionDetectedException {
        for (User participant : newMeeting.getParticipants()) {
            List<Meeting> participantMeetings = meetingRepository.findByParticipantsUserId(participant.getUserId());
            for (Meeting existingMeeting : participantMeetings) {
                if (newMeeting.getStartTime().isBefore(existingMeeting.getEndTime()) &&
                        newMeeting.getEndTime().isAfter(existingMeeting.getStartTime())) {
                    throw new CollisionDetectedException("Participant " + participant.getName() +
                            " is already booked for another meeting from " + existingMeeting.getStartTime() +
                            " to " + existingMeeting.getEndTime() + ". Please choose a different time.");
                }
            }
        }
    }

}
