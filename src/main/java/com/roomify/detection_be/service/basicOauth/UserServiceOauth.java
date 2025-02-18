package com.roomify.detection_be.service.basicOauth;

import com.roomify.detection_be.dto.BaseResponseDTO;
import com.roomify.detection_be.dto.UserDTO;
import com.roomify.detection_be.web.entities.Users.User;
import java.util.Optional;

public interface UserServiceOauth {

    BaseResponseDTO<User> registerAccount(UserDTO userDTO);

    void updateAccount(UserDTO userDTO);

    Optional<User> findCurrentUser();
}
