package com.roomify.detection_be.web.controller.api;

import com.roomify.detection_be.dto.JoinRoomRequest;
import com.roomify.detection_be.dto.RoomImplementDTO;
import com.roomify.detection_be.service.RoomService;
import com.roomify.detection_be.web.entity.Room;
import com.roomify.detection_be.web.entity.RoomAccessHistory;
import com.roomify.detection_be.web.entity.RoomParticipant;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping("/request-join")
    public ResponseEntity<String> requestJoinRoom(@RequestBody JoinRoomRequest request) {
        String response = roomService.requestJoinRoom(request.getRoomId(), request.getUserId());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/accept-join")
    public ResponseEntity<String> acceptJoinRequest(
            @RequestParam String roomId,
            @RequestParam String userId,
            @RequestParam String hostId) {
        String response = roomService.acceptJoinRequest(roomId, userId, hostId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    public ResponseEntity<String> createRoom(@RequestBody RoomImplementDTO request) {
        String roomId = roomService.createRoom(request);
        return ResponseEntity.ok("Room created with ID: " + roomId);
    }

    @DeleteMapping("/delete/{roomId}")
    public ResponseEntity<String> deleteRoom(@PathVariable String roomId) {
        roomService.deleteRoom(roomId);
        return ResponseEntity.ok("Room deleted successfully");
    }

    @GetMapping("/list")
    public ResponseEntity<List<Room>> getAllRooms() {
        List<Room> rooms = roomService.getRooms();
        return ResponseEntity.ok(rooms);
    }

    @DeleteMapping("/leave/{roomId}/{userId}")
    public ResponseEntity<String> leaveRoom(@PathVariable String roomId) {
        roomService.leaveRoom(roomId);
        return ResponseEntity.ok("User has left the room");
    }

    @GetMapping("/participants/{roomId}")
    public ResponseEntity<List<RoomParticipant>> getParticipants(@PathVariable String roomId) {
        List<RoomParticipant> participants = roomService.getParticipants(roomId);
        return ResponseEntity.ok(participants);
    }

    @GetMapping("/history/{roomId}")
    public ResponseEntity<List<RoomAccessHistory>> getRoomHistory(@PathVariable String roomId) {
        List<RoomAccessHistory> history = roomService.logRoomAccess(roomId);
        return ResponseEntity.ok(history);
    }
}
