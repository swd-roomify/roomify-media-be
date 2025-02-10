package com.roomify.detection_be.web.dtos.res;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserWSRes {
  private String userId;
  private String username;
  private String character;
  private int positionX;
  private int positionY;
}
