package com.roomify.detection_be.web.controller.api;

import com.roomify.detection_be.web.dtos.req.RoomCreateDtoReq;
import com.roomify.detection_be.web.dtos.res.RoomDtoRes;
import com.roomify.detection_be.web.entities.Room;
import com.roomify.detection_be.web.entities.RoomAccessHistory;
import com.roomify.detection_be.web.entities.RoomParticipant;
import com.roomify.detection_be.web.service.database.RoomService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {
  @Autowired
  private RoomService roomService;

  @PostMapping
  public ResponseEntity<RoomDtoRes> CreateRoom(@RequestBody RoomCreateDtoReq room) {
    return ResponseEntity.ok(roomService.CreateRoom(room));
  }

  @GetMapping("/room/{roomId}")
  public ResponseEntity<RoomDtoRes> GetRoomById(@PathVariable String roomId) {
    return ResponseEntity.ok(roomService.GetRoomById(roomId));
  }

  @GetMapping("/code/{roomCode}")
  public ResponseEntity<RoomDtoRes> GetRoomByCode(@PathVariable String roomCode) {
    return ResponseEntity.ok(roomService.getRoomByCode(roomCode));
  }

  @GetMapping("/user/{userId}")
  public ResponseEntity<List<RoomDtoRes>> GetRoomByUserId(@PathVariable String userId) {
    return ResponseEntity.ok(roomService.GetRoomsByUserId(userId));
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
