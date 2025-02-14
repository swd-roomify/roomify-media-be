package com.roomify.detection_be.service;

import com.roomify.detection_be.Repository.*;
import com.roomify.detection_be.dto.RoomImplementDTO;
import com.roomify.detection_be.service.basicOauth.UserServiceOauth;
import com.roomify.detection_be.service.basicOauth.UserServiceOauthImpl;
import com.roomify.detection_be.web.entity.Room;
import com.roomify.detection_be.web.entity.RoomAccessHistory;
import com.roomify.detection_be.web.entity.RoomParticipant;
import com.roomify.detection_be.web.entity.RoomType;
import com.roomify.detection_be.web.entity.Users.User;
import com.roomify.detection_be.web.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final RoomParticipantRepository participantRepository;
    private final UserRepository userRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final RoomAccessHistoryRepository accessHistoryRepository;
    private final UserServiceOauth userServiceOauth;

    public String requestJoinRoom(String roomId, String userId) {
        Optional<Room> room = roomRepository.findById(roomId);
        if (room.isEmpty()) {
            throw new RuntimeException("Room not found");
        }
        return "Request sent to host for approval";
    }

    public String acceptJoinRequest(String roomId, String userId, String hostId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        if (!room.getHost().getUserId().equals(hostId)) {
            throw new RuntimeException("Only host can accept join requests");
        }
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            RoomParticipant participant = new RoomParticipant();
            participant.setRoom(room);
            participant.setUser(user.get());
            participant.setJoinTime(Instant.from(LocalDateTime.now()));
            participantRepository.save(participant);
        }


        return "User has been added to the room";
    }

    public String createRoom(RoomImplementDTO request) {
        User host = userRepository.findById(request.getHostId())
                .orElseThrow(() -> new RuntimeException("Host not found"));

        RoomType roomType = roomTypeRepository.findById(request.getRoomTypeId())
                .orElseThrow(() -> new RuntimeException("Room type not found"));

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

    public void leaveRoom(String roomId, String userId) {
        participantRepository.deleteByRoomIdAndUserUserId(roomId, userId);
    }

    public List<RoomParticipant> getParticipants(String roomId) {
        return participantRepository.findUsersByRoomId(roomId);
    }


    public RoomAccessHistory logRoomAccess(String roomId) {
        RoomAccessHistory history = new RoomAccessHistory();
        Optional<User> user = userServiceOauth.findCurrentUser();
        history.setUser(user.get());
        history.setRoom(roomRepository.findById(roomId).get());
        accessHistoryRepository.save(history);
        return history;
    }
}


