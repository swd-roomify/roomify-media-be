package com.roomify.detection_be.web.dtos.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomDtoRes {
    private String roomId;
    private String roomName;
    private String roomCode;
    private String hostId;
    private Instant createdAt;
}
