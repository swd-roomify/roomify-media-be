package com.roomify.detection_be.web.entity.res;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserGenerateRes {
  private String userId;
  private String username;
  private int positionX;
  private int positionY;
}
