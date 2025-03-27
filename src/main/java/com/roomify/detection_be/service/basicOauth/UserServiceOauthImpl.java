package com.roomify.detection_be.service.basicOauth;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.roomify.detection_be.Repository.RoleRepository;
import com.roomify.detection_be.Repository.UserRepository;
import com.roomify.detection_be.dto.BaseResponseDTO;
import com.roomify.detection_be.dto.NewPasswordDto;
import com.roomify.detection_be.dto.UserCreateDto;
import com.roomify.detection_be.dto.UserDTO;
import com.roomify.detection_be.exception.ApplicationErrorCode;
import com.roomify.detection_be.exception.ApplicationException;
import com.roomify.detection_be.exception.BaseException;
import com.roomify.detection_be.web.entities.Provider;
import com.roomify.detection_be.web.entities.Role;
import com.roomify.detection_be.web.entities.User;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserServiceOauthImpl implements UserServiceOauth {

  private final RoleRepository roleRepository;
  private final UserRepository userRepository;
  private final BCryptPasswordEncoder passwordEncoder;

  @Override
  public BaseResponseDTO<User> registerAccount(UserCreateDto userDTO) {
    validateAccount(userDTO);
    User user = insertUser(userDTO);
    userRepository.save(user);

    BaseResponseDTO<User> response = new BaseResponseDTO<>();
    response.setCode(String.valueOf(HttpStatus.CREATED.value()));
    response.setData(user);
    response.setMessage("Register account successfully!!!");
    return response;
  }

  @Override
  public void updateAccount(NewPasswordDto passwordDto) {
    User user = userRepository.findById(passwordDto.getUserId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    if(user.getPassword() != null && passwordDto.getOldPassword() == null){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect login method, password is already initialized");
    }
    if(user.getPassword() == null || BCrypt.verifyer().verify(passwordDto.getOldPassword().toCharArray(), user.getPassword()).verified) {
        user.setPassword(BCrypt.withDefaults().hashToString(12, passwordDto.getNewPassword().toCharArray()));
    }
    else{
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect password");
    }
    userRepository.save(user);
  }

  @Override
  public Optional<User> findCurrentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String username = "";
    if (authentication != null
        && authentication.isAuthenticated()
        && !(authentication instanceof AnonymousAuthenticationToken)) {
      username = (String) authentication.getPrincipal();
    }
    return userRepository.findByUsername(username);
  }

  public User getCurrentUser(String username) {
    return userRepository
        .findByUsername(username)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
  }

  private User updateUser(UserCreateDto userDTO, String userId) {
      User user = userRepository.findById(userId).orElse(null);
      if (user != null) {
          user.setEmail(userDTO.getEmail());
          user.setUsername(userDTO.getUsername());
          user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
      }
      return user;
  }
  private User insertUser(UserCreateDto userDTO) {
    Optional<Role> userRole = roleRepository.findByName("USER");

    return User.builder()
        .email(userDTO.getEmail())
        .username(userDTO.getUsername())
        .password(passwordEncoder.encode(userDTO.getPassword()))
        .role(userRole.orElse(null))
        .isEnabled(true)
        .accountNonExpired(true)
        .accountNonLocked(true)
        .credentialsNonExpired(true)
        .providedId(Provider.local.name())
        .build();
  }

  private void validateAccount(UserCreateDto userDTO) {
    if (ObjectUtils.isEmpty(userDTO)) {
      throw new BaseException(
          String.valueOf(HttpStatus.BAD_REQUEST.value()), "Request data not found!");
    }

    try {
      if (!ObjectUtils.isEmpty(userDTO.checkProperties())) {
        throw new BaseException(
            String.valueOf(HttpStatus.BAD_REQUEST.value()), "Request data not found!");
      }
    } catch (IllegalAccessException e) {
      throw new BaseException(
          String.valueOf(HttpStatus.SERVICE_UNAVAILABLE.value()), "Service Unavailable");
    }

    List<String> roles = roleRepository.findAll().stream().map(Role::getName).toList();

    Optional<User> userFindByUsername = userRepository.findByUsername(userDTO.getUsername());

    if (!ObjectUtils.isEmpty(userFindByUsername)) {
      throw new BaseException(
          String.valueOf(HttpStatus.BAD_REQUEST.value()), "User had existed with this username!!!");
    }
    Optional<User> userFindByEmail = userRepository.findByEmail(userDTO.getEmail());
    if (!ObjectUtils.isEmpty(userFindByEmail)) {
      throw new BaseException(
          String.valueOf(HttpStatus.BAD_REQUEST.value()), "User had existed with this email!!!");
    }
  }
}
