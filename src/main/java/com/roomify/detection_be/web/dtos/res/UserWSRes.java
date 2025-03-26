package com.roomify.detection_be.web.dtos.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserWSRes implements Serializable {
  @JsonProperty("user_id")
  private String userId;

  @JsonProperty("username")
  private String username;

  @JsonProperty("character")
  private String character;

  @JsonProperty("position_x")
  private int positionX;

  @JsonProperty("position_y")
  private int positionY;
}
