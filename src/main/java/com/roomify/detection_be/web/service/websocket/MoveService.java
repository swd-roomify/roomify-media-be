package com.roomify.detection_be.web.service.websocket;


import com.roomify.detection_be.web.constants.RedisKeyPrefix;
import com.roomify.detection_be.web.controller.websocket.ConnectionWebSocket;
import com.roomify.detection_be.web.dtos.res.UserWSRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class MoveService {
    private static final Logger log = LoggerFactory.getLogger(ConnectionWebSocket.class);
    private final RedisTemplate<String, UserWSRes> userRedisTemplate;
    private final String WS_USER_KEY_PREFIX = RedisKeyPrefix.USER_KEY_PREFIX;

    public MoveService(RedisTemplate<String, UserWSRes> userRedisTemplate) {
        this.userRedisTemplate = userRedisTemplate;
    }

    private String getUserKey(String userId) {
        return WS_USER_KEY_PREFIX + userId;
    }

    public void saveUserPosition(UserWSRes userWSRes) {
        UserWSRes existingUser = userRedisTemplate.opsForValue().get(getUserKey(userWSRes.getUserId()));
        log.info(existingUser.toString());
        existingUser.setPositionX(userWSRes.getPositionX());
        existingUser.setPositionY(userWSRes.getPositionY());
        userRedisTemplate.opsForValue().set(getUserKey(userWSRes.getUserId()), existingUser);
    }

    public Map<String, UserWSRes> getAllUsers() {
        return Objects.requireNonNull(userRedisTemplate.keys(WS_USER_KEY_PREFIX + "*"))
                .stream()
                .collect(Collectors.toMap(
                        key -> key.substring(WS_USER_KEY_PREFIX.length()),
                        key -> userRedisTemplate.opsForValue().get(key)
                ));
    }
}
