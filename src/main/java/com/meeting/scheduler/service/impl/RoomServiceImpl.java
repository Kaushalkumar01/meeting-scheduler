package com.meeting.scheduler.service.impl;

import com.meeting.scheduler.entity.Room;
import com.meeting.scheduler.exception.RoomNotFoundException;
import com.meeting.scheduler.repository.RoomRepository;
import com.meeting.scheduler.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {
    @Autowired
    private RoomRepository roomRepository;

    @Override
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    @Override
    public Room getRoomById(Long id) throws RoomNotFoundException {
        return roomRepository.findById(id)
                .orElseThrow(() -> new RoomNotFoundException(id));
    }

    @Override
    public Room createRoom(Room room) {
        return roomRepository.save(room);
    }

    @Override
    public Room updateRoom(Long id, Room roomDetails) throws RoomNotFoundException {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RoomNotFoundException(id));
        room.setRoomName(roomDetails.getRoomName());
        room.setCapacity(roomDetails.getCapacity());
        return roomRepository.save(room);
    }

    @Override
    public void deleteRoom(Long id) throws RoomNotFoundException {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RoomNotFoundException(id));
        roomRepository.delete(room);
    }
}
