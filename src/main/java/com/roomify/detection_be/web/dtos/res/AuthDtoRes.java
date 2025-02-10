package com.roomify.detection_be.web.dtos.res;

import com.roomify.detection_be.web.dtos.jwt.TokenDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthDtoRes {
    private TokenDTO token;
    private UserDtoRes user;

    public static AuthDtoRes toDto(TokenDTO token, UserDtoRes user) {
        return new AuthDtoRes(token, user);
    }
}
