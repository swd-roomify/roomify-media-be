package com.roomify.detection_be.web.entity.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserMoveReq {
    @JsonProperty("userId")
    private String userId;
    @JsonProperty("username")
    private String username;
    private int positionX;
    private int positionY;
}
