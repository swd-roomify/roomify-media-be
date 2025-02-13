package com.roomify.detection_be.web.controller.api;

import com.roomify.detection_be.web.controller.websocket.ConnectionWebSocket;
import com.roomify.detection_be.web.entity.req.UserGenerateReq;
import com.roomify.detection_be.web.entity.res.UserGenerateRes;
import com.roomify.detection_be.web.service.UserService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    private static final Logger log = LoggerFactory.getLogger(ConnectionWebSocket.class);

    @PostMapping("/generate")
    public UserGenerateRes generateUser(@RequestBody UserGenerateReq user) {
        return userService.generateUser(user);
    }

    @PostMapping("/request")
    public ResponseEntity<String> sendFriendRequest(@RequestParam String userIdUser2) {
        return userService.generateFriendShip(userIdUser2);

    }

    @DeleteMapping("/unfriend")
    public ResponseEntity<String> unfriend(@RequestParam String userIdUser2) {
        return userService.removeFriendShip(userIdUser2);
    }
}
