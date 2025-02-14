package com.roomify.detection_be.dto;

import lombok.Data;

@Data
public class JoinRoomRequest {
    private String userId;
    private String roomId;
}
