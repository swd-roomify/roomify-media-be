package com.roomify.detection_be.web.service;

import com.roomify.detection_be.utility.SnowflakeGenerator;
import com.roomify.detection_be.web.constants.RedisKeyPrefix;
import com.roomify.detection_be.web.controller.websocket.ConnectionWebSocket;
import com.roomify.detection_be.web.entity.req.UserGenerateReq;
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

    private String getUserKey(String userId) {
        String WS_USER_KEY_PREFIX = RedisKeyPrefix.USER_KEY_PREFIX;
        return WS_USER_KEY_PREFIX + userId;
    }

    public UserGenerateRes generateUser(UserGenerateReq user) {
        SnowflakeGenerator snowflake = new SnowflakeGenerator(1);
        String userId = String.valueOf(snowflake.nextId());

        UserGenerateRes newUserGenerateRes = UserGenerateRes.builder()
                .userId(userId)
                .username(user.getUsername())
                .positionX(200)
                .positionY(200)
                .character(user.getCharacter())
                .build();
        userRedisTemplate.opsForValue().set(getUserKey(userId), newUserGenerateRes);

        return newUserGenerateRes;
    }
}
