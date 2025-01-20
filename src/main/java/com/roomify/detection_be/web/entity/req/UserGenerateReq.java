package com.roomify.detection_be.web.entity.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserGenerateReq {
    @JsonProperty("username")
    private String username;
    @JsonProperty("character")
    private String character;
}
