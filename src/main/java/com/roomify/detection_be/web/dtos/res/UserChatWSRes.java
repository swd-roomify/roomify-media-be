package com.roomify.detection_be.web.dtos.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserChatWSRes {
    private String userId;
    private String username;
    private String message;
}
