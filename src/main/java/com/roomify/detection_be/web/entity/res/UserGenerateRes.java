package com.roomify.detection_be.web.entity.res;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserGenerateRes {
  private String userId;
  private String username;
  private String character;
  private int positionX;
  private int positionY;
}
