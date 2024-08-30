package com.meeting.scheduler.controller;

import com.meeting.scheduler.dto.MeetingRequestDTO;
import com.meeting.scheduler.entity.Meeting;
import com.meeting.scheduler.service.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/meetings")
public class MeetingController {

    @Autowired
    private MeetingService meetingService;

    @GetMapping
    public List<Meeting> getAllMeetings() {
        return meetingService.getAllMeetings();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMeetingById(@PathVariable Long id) {
        return ResponseEntity.ok(meetingService.getMeetingById(id));
    }

    @PostMapping
    public ResponseEntity<?> createMeeting(@RequestBody MeetingRequestDTO meetingRequest) {
            return ResponseEntity.ok(meetingService.createMeeting(meetingRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMeeting(@PathVariable Long id, @RequestBody MeetingRequestDTO meetingRequest) {
        return ResponseEntity.ok(meetingService.updateMeeting(id, meetingRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMeeting(@PathVariable Long id) {
        return ResponseEntity.ok("Meeting deleted successfully.");
    }
}
