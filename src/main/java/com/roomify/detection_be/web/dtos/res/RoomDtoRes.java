package com.roomify.detection_be.web.dtos.res;

import com.roomify.detection_be.web.entities.User;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomDtoRes {
    private String roomId;
    private String roomName;
    private String roomCode;
    private String hostId;
    private LocalDateTime createdAt;
}
