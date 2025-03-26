package com.roomify.detection_be.web.controller.api;
import com.roomify.detection_be.web.dtos.req.UserCreateDtoReq;
import com.roomify.detection_be.web.dtos.req.UserCredentialReq;
import com.roomify.detection_be.web.dtos.req.UserGenerateReq;
import com.roomify.detection_be.web.dtos.res.AuthDtoRes;
import com.roomify.detection_be.web.dtos.res.UserDtoRes;
import com.roomify.detection_be.web.dtos.res.UserWSRes;
import com.roomify.detection_be.web.service.database.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
  @Autowired
  private UserService userService;
  private static final Logger log = LoggerFactory.getLogger(UserController.class);

//  @PostMapping("/register")
//  public ResponseEntity<UserDtoRes> CreateUser(@RequestBody UserCreateDtoReq user) {
//    return ResponseEntity.ok(userService.createUser(user));
//  }

  @PostMapping("/signin")
  public ResponseEntity<AuthDtoRes> AuthorizeUser(@RequestBody UserCredentialReq user) {
    return ResponseEntity.ok(userService.GetUserAuthorize(user));
  }

  @PostMapping("/generate")
  public ResponseEntity<UserWSRes> GeneratePlayableCharacter(@RequestBody UserGenerateReq user) {
    return ResponseEntity.ok(userService.GenerateCharacter(user));
  }

  @PostMapping("/request")
  public ResponseEntity<String> sendFriendRequest(@RequestParam String userIdUser2) {
    return ResponseEntity.ok(userService.generateFriendShip(userIdUser2));
  }
}
