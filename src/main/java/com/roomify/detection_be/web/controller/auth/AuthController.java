package com.roomify.detection_be.web.controller.auth;

import com.roomify.detection_be.config.JwtConfig;
import com.roomify.detection_be.dto.BaseResponseDTO;
import com.roomify.detection_be.dto.UserDTO;
import com.roomify.detection_be.service.basicOauth.UserServiceOauth;
import com.roomify.detection_be.service.jwt.JwtService;
import com.roomify.detection_be.service.oauth2.security.OAuth02UserDetailsCustom;
import com.roomify.detection_be.web.entities.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserServiceOauth userServiceOauth;
    private final JwtConfig jwtConfig;
    private final JwtService jwtService;

    @PostMapping("/register-account")
    public ResponseEntity<BaseResponseDTO> registerAccount(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userServiceOauth.registerAccount(userDTO));
    }

    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo() {
        Optional<User> user = userServiceOauth.findCurrentUser();
        return ResponseEntity.ok(user);
    }

    @PutMapping("/update-password")
    public ResponseEntity<?> updatePassword(@RequestBody UserDTO userDTO) {
        userServiceOauth.updateAccount(userDTO);
        return ResponseEntity.ok("Reset password successfully");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String accessToken = request.getHeader(jwtConfig.getHeader());
        if (!ObjectUtils.isEmpty(accessToken) && accessToken.startsWith(jwtConfig.getPrefix() + " ")) {
            accessToken = accessToken.substring((jwtConfig.getPrefix() + " ").length());
            jwtService.revokeToken(accessToken);
            return ResponseEntity.ok("Logged out successfully");
        }

        return ResponseEntity.badRequest().body("Invalid token");
    }

    @GetMapping("/me/{username}")
    public ResponseEntity<User> getCurrentUser(@PathVariable String username) {
        return ResponseEntity.ok(userServiceOauth.getCurrentUser(username));
    }


}
