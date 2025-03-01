package com.roomify.detection_be.web.dtos.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserMoveReq {
  @JsonProperty("user_id")
  private String userId;

  @JsonProperty("position_x")
  private int positionX;

  @JsonProperty("position_y")
  private int positionY;
}
