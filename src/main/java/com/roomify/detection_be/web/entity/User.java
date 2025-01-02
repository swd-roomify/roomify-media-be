package com.roomify.detection_be.web.entity;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class User {
  private String username;
  private int positionX;
  private int positionY;
}
