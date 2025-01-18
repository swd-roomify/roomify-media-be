package com.roomify.detection_be.web.controller.api;


import com.roomify.detection_be.web.entity.req.UserGenerateReq;
import com.roomify.detection_be.web.entity.res.UserGenerateRes;
import com.roomify.detection_be.web.service.UserService;
import lombok.AllArgsConstructor;
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

    @PostMapping("/generate")
    public UserGenerateRes generateUser(@RequestBody UserGenerateReq user) {
        return userService.generateUser(user.getUsername());
    }
}
