package com.roomify.detection_be.web.entity.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserMove {
  private String username;
  private int positionX;
  private int positionY;
}
