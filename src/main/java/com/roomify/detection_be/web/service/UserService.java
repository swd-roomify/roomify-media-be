package com.roomify.detection_be.web.service;

import com.roomify.detection_be.utility.SnowflakeGenerator;
import com.roomify.detection_be.web.controller.websocket.ConnectionWebSocket;
import com.roomify.detection_be.web.entity.res.UserGenerateRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final RedisTemplate<String, UserGenerateRes> userRedisTemplate;
    private static final Logger log = LoggerFactory.getLogger(ConnectionWebSocket.class);
    public UserService(RedisTemplate<String, UserGenerateRes> userRedisTemplate) {
        this.userRedisTemplate = userRedisTemplate;
    }

    public UserGenerateRes generateUser(String username) {
        SnowflakeGenerator snowflake = new SnowflakeGenerator(1);
        String userId = String.valueOf(snowflake.nextId());

        UserGenerateRes newUserGenerateRes = UserGenerateRes.builder()
                .userId(userId)
                .username(username)
                .positionX(200)
                .positionY(200)
                .build();
        log.info("User {} generated with ID {}", username, userId);
        userRedisTemplate.opsForValue().set("USER_" + username, newUserGenerateRes);

        return newUserGenerateRes;
    }
}
