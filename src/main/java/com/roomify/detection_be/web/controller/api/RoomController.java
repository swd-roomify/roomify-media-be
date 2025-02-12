package com.roomify.detection_be.web.controller.api;

import com.roomify.detection_be.web.dtos.req.RoomCreateDtoReq;
import com.roomify.detection_be.web.dtos.res.RoomDtoRes;
import com.roomify.detection_be.web.service.database.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
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
        return ResponseEntity.ok(roomService.GetRoomByCode(roomCode));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RoomDtoRes>> GetRoomByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(roomService.GetRoomsByUserId(userId));
    }
}