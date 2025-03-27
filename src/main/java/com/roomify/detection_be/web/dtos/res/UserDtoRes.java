package com.roomify.detection_be.web.dtos.res;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDtoRes {
  private String userId;
  private String username;
  private String email;
  private Instant createdAt;
  private String roleName;
  private boolean isEnabled;
}
