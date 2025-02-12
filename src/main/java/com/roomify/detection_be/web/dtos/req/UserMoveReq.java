package com.roomify.detection_be.web.dtos.req;

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
    @JsonProperty("positionX")
    private int positionX;
    @JsonProperty("positionY")
    private int positionY;
}
