package com.roomify.detection_be.web.dtos.res;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDtoRes {
    private String userId;
    private String username;
    private String email;
    private LocalDateTime createdAt;
}
