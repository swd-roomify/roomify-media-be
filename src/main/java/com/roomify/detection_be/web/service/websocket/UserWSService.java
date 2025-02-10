package com.roomify.detection_be.web.service.websocket;

import com.roomify.detection_be.utility.SnowflakeGenerator;
import com.roomify.detection_be.web.constants.RedisKeyPrefix;
import com.roomify.detection_be.web.controller.websocket.ConnectionWebSocket;
import com.roomify.detection_be.web.dtos.req.UserGenerateReq;
import com.roomify.detection_be.web.dtos.res.UserWSRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserWSService {
    private final RedisTemplate<String, UserWSRes> userRedisTemplate;
    private static final Logger log = LoggerFactory.getLogger(ConnectionWebSocket.class);

    public UserWSService(RedisTemplate<String, UserWSRes> userRedisTemplate) {
        this.userRedisTemplate = userRedisTemplate;
    }

    private String getUserKey(String userId) {
        String WS_USER_KEY_PREFIX = RedisKeyPrefix.USER_KEY_PREFIX;
        return WS_USER_KEY_PREFIX + userId;
    }

    public UserWSRes generateUser(UserGenerateReq user) {
        SnowflakeGenerator snowflake = new SnowflakeGenerator(1);
        String userId = String.valueOf(snowflake.nextId());

        UserWSRes newUserWSRes = UserWSRes.builder()
                .userId(userId)
                .username(user.getUsername())
                .positionX(200)
                .positionY(200)
                .character(user.getCharacter())
                .build();
        userRedisTemplate.opsForValue().set(getUserKey(userId), newUserWSRes);

        return newUserWSRes;
    }
}
