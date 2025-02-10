package com.roomify.detection_be.web.service.database;

import com.roomify.detection_be.repository.RoomRepository;
import com.roomify.detection_be.repository.UserRepository;
import com.roomify.detection_be.utility.SnowflakeGenerator;
import com.roomify.detection_be.web.dtos.req.RoomCreateDtoReq;
import com.roomify.detection_be.web.dtos.res.RoomDtoRes;
import com.roomify.detection_be.web.entities.Room;
import com.roomify.detection_be.web.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class RoomService {
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private UserRepository userRepository;
    SnowflakeGenerator snowflake = new SnowflakeGenerator(1);

    public RoomDtoRes CreateRoom(RoomCreateDtoReq roomCreateDtoReq) {
        Room room = new Room();
        room.setRoomId(String.valueOf(snowflake.nextId()));
        room.setRoomCode(String.valueOf(snowflake.nextId()));
        room.setRoomName(roomCreateDtoReq.getRoomName());

        User user = userRepository.findById(roomCreateDtoReq.getHostId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatusCode.valueOf(404), "User not found"));

        room.setHostId(user);
        Room savedRoom = roomRepository.save(room);

        return new RoomDtoRes(savedRoom.getRoomId(), savedRoom.getRoomName(), savedRoom.getRoomCode(), savedRoom.getHostId().getUserId(), savedRoom.getCreatedAt());
    }

    public RoomDtoRes GetRoomById(String roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatusCode.valueOf(404), "Room not found"));
        return new RoomDtoRes(room.getRoomId(), room.getRoomName(), room.getRoomCode(), room.getHostId().getUserId(), room.getCreatedAt());
    }

    public RoomDtoRes GetRoomByCode(String roomCode) {
        Room room = roomRepository.findByRoomCode(roomCode)
                .orElseThrow(() -> new ResponseStatusException(HttpStatusCode.valueOf(404), "Room not found"));
        return new RoomDtoRes(room.getRoomId(), room.getRoomName(), room.getRoomCode(), room.getHostId().getUserId(), room.getCreatedAt());
    }
}
