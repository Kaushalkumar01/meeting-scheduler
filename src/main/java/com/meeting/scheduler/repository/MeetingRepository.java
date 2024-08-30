package com.meeting.scheduler.repository;

import com.meeting.scheduler.entity.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Long> {
    List<Meeting> findByRoomRoomId(Long roomId);

    List<Meeting> findByParticipantsUserId(Long userId);
}
