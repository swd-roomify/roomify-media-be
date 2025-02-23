package com.roomify.detection_be.web.service.database;

import com.roomify.detection_be.dto.RoomImplementDTO;
import com.roomify.detection_be.repository.*;
import com.roomify.detection_be.service.basicOauth.UserServiceOauth;
import com.roomify.detection_be.utility.SnowflakeGenerator;
import com.roomify.detection_be.web.dtos.req.RoomCreateDtoReq;
import com.roomify.detection_be.web.dtos.res.RoomDtoRes;
import com.roomify.detection_be.web.entities.Room;
import com.roomify.detection_be.web.entities.RoomAccessHistory;
import com.roomify.detection_be.web.entities.RoomParticipant;
import com.roomify.detection_be.web.entities.RoomType;
import com.roomify.detection_be.web.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RoomService {
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private RoomParticipantRepository participantRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoomTypeRepository roomTypeRepository;
    @Autowired
    private RoomAccessHistoryRepository accessHistoryRepository;
    @Autowired
    private UserServiceOauth userServiceOauth;
    @Autowired
    private RoomAccessHistoryRepository roomAccessHistoryRepository;
    SnowflakeGenerator snowflake = new SnowflakeGenerator(1);

    public RoomDtoRes CreateRoom(RoomCreateDtoReq roomCreateDtoReq) {
        Room room = new Room();
        room.setId(String.valueOf(snowflake.nextId()));
        room.setRoomCode(String.valueOf(snowflake.nextId()));
        room.setName(roomCreateDtoReq.getRoomName());

        User user = userRepository.findById(roomCreateDtoReq.getHostId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatusCode.valueOf(404), "User not found"));

        room.setHost(user);
        Room savedRoom = roomRepository.save(room);

        return new RoomDtoRes(savedRoom.getId(), savedRoom.getName(), savedRoom.getRoomCode(), savedRoom.getHost().getUserId(), savedRoom.getCreatedAt());
    }

    public RoomDtoRes GetRoomById(String roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatusCode.valueOf(404), "Room not found"));
        return new RoomDtoRes(room.getId(), room.getName(), room.getRoomCode(), room.getHost().getUserId(), room.getCreatedAt());
    }

    public RoomDtoRes GetRoomByCode(String roomCode) {
        Room room = roomRepository.findByRoomCode(roomCode)
                .orElseThrow(() -> new ResponseStatusException(HttpStatusCode.valueOf(404), "Room not found"));
        return new RoomDtoRes(room.getId(), room.getName(), room.getRoomCode(), room.getHost().getUserId(), room.getCreatedAt());
    }

    public List<RoomDtoRes> GetRoomsByUserId(String userId) {
        List<Room> rooms = roomRepository.findByHost_UserId(userId);
        return rooms.stream().map(room -> new RoomDtoRes(room.getId(), room.getName(), room.getRoomCode(), room.getHost().getUserId(), room.getCreatedAt())).toList();
    }

    public String requestJoinRoom(String roomId, String userId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatusCode.valueOf(404), ("Room not found")));
        return "Request sent to host for approval";
    }

    public String createRoom(RoomImplementDTO request) {
        User host = userRepository.findById(request.getHostId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatusCode.valueOf(404), "Host not found"));

        RoomType roomType = roomTypeRepository.findById(request.getRoomTypeId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatusCode.valueOf(404), "Room type not found"));

        Room room = new Room();
        room.setRoomCode(request.getRoomId());
        room.setName(request.getName());
        room.setHost(host);
        room.setRoomType(roomType);
        room.setIsActive(true);
        roomRepository.save(room);

        return room.getId();
    }

    public void deleteRoom(String roomId) {
        roomRepository.deleteById(roomId);
    }

    public List<Room> getRooms() {
        return
                roomRepository.findAll();
    }


    public List<RoomParticipant> getParticipants(String roomId) {
        return participantRepository.findUsersByRoomId(roomId);
    }

    public List<RoomParticipant> AddParticipantToRoom(String roomId, String userId, String hostId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatusCode.valueOf(404), ("Room not found")));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatusCode.valueOf(404), ("User not found")));
        RoomParticipant participant = new RoomParticipant();
        participant.setRoom(room);
        participant.setUser(user);
        participant.setJoinTime(Instant.from(LocalDateTime.now()));
        participantRepository.save(participant);
        return participantRepository.findUsersByRoomId(roomId);
    }

    public List<RoomParticipant> RemoveParticipantFromRoom(String roomId, String userId) {
        participantRepository.deleteByRoomIdAndUserUserId(roomId, userId);
        return participantRepository.findUsersByRoomId(roomId);
    }

    public List<RoomAccessHistory> logRoomAccess(String roomId) {
        return roomAccessHistoryRepository.findByRoomId(roomId);
    }
}
