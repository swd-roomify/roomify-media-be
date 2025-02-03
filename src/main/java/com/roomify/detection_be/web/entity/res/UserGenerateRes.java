package com.roomify.detection_be.web.entity.res;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.roomify.detection_be.web.entity.Role;
import jakarta.persistence.*;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserGenerateRes {
    @JsonProperty("userId")
    private String userId;
    @JsonProperty("username")
    private String username;
    @JsonProperty("character")
    private String character;
    private int positionX;
    private int positionY;

}
