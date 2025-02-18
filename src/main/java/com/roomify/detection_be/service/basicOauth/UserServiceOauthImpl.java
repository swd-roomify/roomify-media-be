package com.roomify.detection_be.service.basicOauth;

import com.roomify.detection_be.Repository.RoleRepository;
import com.roomify.detection_be.Repository.UserRepository;
import com.roomify.detection_be.dto.BaseResponseDTO;
import com.roomify.detection_be.dto.UserDTO;
import com.roomify.detection_be.exception.ApplicationErrorCode;
import com.roomify.detection_be.exception.ApplicationException;
import com.roomify.detection_be.exception.BaseException;
import com.roomify.detection_be.web.entity.Provider;
import com.roomify.detection_be.web.entity.Role;
import com.roomify.detection_be.web.entity.Users.User;

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

@Service
@RequiredArgsConstructor
public class UserServiceOauthImpl implements UserServiceOauth {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public BaseResponseDTO<User> registerAccount(UserDTO userDTO) {
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
    public void updateAccount(UserDTO userDTO) {
        validateAccount(userDTO);
        User currentUser = findCurrentUser()
                .orElseThrow(() -> new ApplicationException(
                        ApplicationErrorCode.USER_NOT_FOUND,
                        "Current user not found"
                ));

        User user = updateUser(userDTO, currentUser.getUserId());
        userRepository.save(user);

        BaseResponseDTO<User> response = new BaseResponseDTO<>();
        response.setCode(String.valueOf(HttpStatus.OK.value()));
        response.setData(user);
        response.setMessage("Register account successfully!!!");
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

    private User updateUser(UserDTO userDTO, String userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            user.setEmail(userDTO.getEmail());
            user.setUsername(userDTO.getUsername());
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        return user;
    }

    private User insertUser(UserDTO userDTO) {
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

    private void validateAccount(UserDTO userDTO) {
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

        if (!roles.contains(userDTO.getRole())) {
            throw new BaseException(String.valueOf(HttpStatus.BAD_REQUEST.value()), "Invalid role");
        }

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
