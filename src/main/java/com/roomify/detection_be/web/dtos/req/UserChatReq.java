package com.roomify.detection_be.web.dtos.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserChatReq {
  @JsonProperty("userId")
  private String userId;

  @JsonProperty("message")
  private String message;
}
