package com.roomify.detection_be.service.basicOauth;

import com.roomify.detection_be.dto.BaseResponseDTO;
import com.roomify.detection_be.dto.UserCreateDto;
import com.roomify.detection_be.dto.UserDTO;
import com.roomify.detection_be.web.entities.User;
import java.util.Optional;

public interface UserServiceOauth {

  BaseResponseDTO<User> registerAccount(UserCreateDto userDTO);

  void updateAccount(UserCreateDto userDTO);

  Optional<User> findCurrentUser();

  User getCurrentUser(String username);
}
