package com.roomify.detection_be.web.dtos.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomCreateDtoReq {
    private String roomName;
    private String hostId;
}
