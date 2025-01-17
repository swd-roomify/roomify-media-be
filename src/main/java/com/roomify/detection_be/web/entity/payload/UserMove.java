package com.roomify.detection_be.web.entity.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserMove {
  private String userId;
  private String username;
  private int positionX;
  private int positionY;
}
