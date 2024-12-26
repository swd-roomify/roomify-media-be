package com.roomify.detection_be.web.entity.payload;

import lombok.Getter;

@Getter
public class UserMove {
  private String username;
  private int positionX;
  private int positionY;
}
