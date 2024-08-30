package com.meeting.scheduler.service;

import com.meeting.scheduler.entity.Room;
import com.meeting.scheduler.exception.RoomNotFoundException;

import java.util.List;

public interface RoomService {
    List<Room> getAllRooms();
    Room getRoomById(Long id) throws RoomNotFoundException;
    Room createRoom(Room room);
    Room updateRoom(Long id, Room roomDetails) throws RoomNotFoundException;
    void deleteRoom(Long id) throws RoomNotFoundException;
}
