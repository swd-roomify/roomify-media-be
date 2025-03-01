package com.roomify.detection_be.dto;

import lombok.Data;

@Data
public class UserRequest {
  private String username;
  private String password;
}
