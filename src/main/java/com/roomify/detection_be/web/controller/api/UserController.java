package com.roomify.detection_be.web.controller.api;


import com.roomify.detection_be.web.controller.websocket.ConnectionWebSocket;
import com.roomify.detection_be.web.entity.req.UserGenerateReq;
import com.roomify.detection_be.web.entity.res.UserGenerateRes;
import com.roomify.detection_be.web.service.UserService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private final UserService userService;
    private static final Logger log = LoggerFactory.getLogger(ConnectionWebSocket.class);
    @PostMapping("/generate")
    public UserGenerateRes generateUser(@RequestBody UserGenerateReq user) {
        return userService.generateUser(user);
    }
}
