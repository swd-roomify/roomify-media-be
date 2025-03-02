package com.roomify.detection_be.web.controller.api;

import com.roomify.detection_be.web.dtos.req.UserCredentialReq;
import com.roomify.detection_be.web.dtos.req.UserGenerateReq;
import com.roomify.detection_be.web.service.database.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user/v1")
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;

  @PostMapping("/signin")
  public ResponseEntity<?> authorizeUser(@RequestBody UserCredentialReq user) {
    return ResponseEntity.ok(userService.getUserAuthorize(user));
  }

  @PostMapping("/generate")
  public ResponseEntity<?> generatePlayableCharacter(@RequestBody UserGenerateReq user) {
    return ResponseEntity.ok(userService.generateCharacter(user));
  }

  @PostMapping("/request")
  public ResponseEntity<?> sendFriendRequest(@RequestParam String userIdUser2) {
    return userService.generateFriendShip(userIdUser2);
  }

  @DeleteMapping("/unfriend")
  public ResponseEntity<?> unfriend(@RequestParam String userIdUser2) {
    return userService.removeFriendShip(userIdUser2);
  }
}
