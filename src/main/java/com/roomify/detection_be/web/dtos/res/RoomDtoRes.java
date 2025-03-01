package com.roomify.detection_be.web.dtos.res;

import java.time.Instant;
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
  private String hostId;
  private Instant createdAt;
}
