package com.roomify.detection_be.web.dtos.res;

import java.time.Instant;

import com.roomify.detection_be.web.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomDtoRes {
  private String roomId;
  private String roomName;
  private String roomCode;
  private User host;
  private Instant createdAt;
}
