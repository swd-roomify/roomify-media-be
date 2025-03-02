package com.roomify.detection_be.web.controller.api;

import com.roomify.detection_be.web.dtos.req.RoomCreateDtoReq;
import com.roomify.detection_be.web.dtos.res.RoomDtoRes;
import com.roomify.detection_be.web.entities.Room;
import com.roomify.detection_be.web.entities.RoomAccessHistory;
import com.roomify.detection_be.web.entities.RoomParticipant;
import com.roomify.detection_be.web.service.database.RoomService;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {
  private final RoomService roomService;

  @PostMapping
  public ResponseEntity<?> createRoom(@RequestBody RoomCreateDtoReq room) {
    return ResponseEntity.ok(roomService.createRoom(room));
  }

  @GetMapping("/room/{roomId}")
  public ResponseEntity<?> getRoomById(@PathVariable String roomId) {
    return ResponseEntity.ok(roomService.getRoomById(roomId));
  }

  @GetMapping("/code/{roomCode}")
  public ResponseEntity<?> getRoomByCode(@PathVariable String roomCode) {
    return ResponseEntity.ok(roomService.getRoomByCode(roomCode));
  }

  @GetMapping("/user/{userId}")
  public ResponseEntity<?> getRoomByUserId(@PathVariable String userId) {
    return ResponseEntity.ok(roomService.getRoomsByUserId(userId));
  }

  @DeleteMapping("/delete/{roomId}")
  public ResponseEntity<?> deleteRoom(@PathVariable String roomId) {
    roomService.deleteRoom(roomId);
    return ResponseEntity.ok("Room deleted successfully");
  }

  @GetMapping("/list")
  public ResponseEntity<?> getAllRooms() {
    List<Room> rooms = roomService.getRooms();
    return ResponseEntity.ok(rooms);
  }

  @GetMapping("/participants/{roomId}")
  public ResponseEntity<?> getParticipants(@PathVariable String roomId) {
    List<RoomParticipant> participants = roomService.getParticipants(roomId);
    return ResponseEntity.ok(participants);
  }

  @GetMapping("/history/{roomId}")
  public ResponseEntity<?> getRoomHistory(@PathVariable String roomId) {
    List<RoomAccessHistory> history = roomService.logRoomAccess(roomId);
    return ResponseEntity.ok(history);
  }
}
