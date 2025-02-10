package com.roomify.detection_be.web.controller.api;


import com.roomify.detection_be.web.controller.websocket.ConnectionWebSocket;
import com.roomify.detection_be.web.dtos.req.UserCreateDtoReq;
import com.roomify.detection_be.web.dtos.req.UserCredentialReq;
import com.roomify.detection_be.web.dtos.res.AuthDtoRes;
import com.roomify.detection_be.web.dtos.res.UserDtoRes;
import com.roomify.detection_be.web.service.database.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;
    private static final Logger log = LoggerFactory.getLogger(ConnectionWebSocket.class);

    @PostMapping("/register")
    public ResponseEntity<UserDtoRes> CreateUser(@RequestBody UserCreateDtoReq user) {
        return ResponseEntity.ok(userService.CreateUser(user));
    }
    @PostMapping("/login")
    public ResponseEntity<AuthDtoRes> GenerateUser(@RequestBody UserCredentialReq user) {
        return ResponseEntity.ok(userService.GetUserAuthorize(user));
    }

}
