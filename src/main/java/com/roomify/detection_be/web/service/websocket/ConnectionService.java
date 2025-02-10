package com.roomify.detection_be.web.service.websocket;


import com.roomify.detection_be.web.constants.RedisKeyPrefix;
import com.roomify.detection_be.web.dtos.req.UserJoinReq;
import com.roomify.detection_be.web.dtos.res.UserWSRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class ConnectionService {
    private static final Logger log = LoggerFactory.getLogger(ConnectionService.class);
    private final RedisTemplate<String, UserWSRes> userRedisTemplate;
    private final RedisTemplate<String, String> sessionRedisTemplate;
    private final String WS_USER_KEY_PREFIX = RedisKeyPrefix.USER_KEY_PREFIX;

    public ConnectionService(RedisTemplate<String, UserWSRes> userRedisTemplate,
                             RedisTemplate<String, String> sessionRedisTemplate) {
        this.userRedisTemplate = userRedisTemplate;
        this.sessionRedisTemplate = sessionRedisTemplate;
    }

    private String getUserKey(String userId) {
        return WS_USER_KEY_PREFIX + userId;
    }

    private String getSessionKey(String sessionId) {
        String WS_SESSION_KEY_PREFIX = RedisKeyPrefix.SESSION_KEY_PREFIX;
        return WS_SESSION_KEY_PREFIX + sessionId;
    }

    public String saveUserAndSession(UserJoinReq message, String sessionId) {
        String username = message.getUsername();
        String userId = message.getUserId();
        String character = message.getCharacter();
        sessionRedisTemplate.opsForValue().set(getSessionKey(sessionId), userId);
        return userId;
    }

    public Map<String, UserWSRes> getAllUsers() {
        return Objects.requireNonNull(userRedisTemplate.keys(WS_USER_KEY_PREFIX + "*"))
                .stream()
                .collect(Collectors.toMap(
                        key -> key.substring(WS_USER_KEY_PREFIX.length()),
                        key -> userRedisTemplate.opsForValue().get(key)
                ));
    }

    public void removeUserBySession(String sessionId) {
        String sessionKey = getSessionKey(sessionId);
        String userId = sessionRedisTemplate.opsForValue().get(sessionKey);

        userRedisTemplate.delete(getUserKey(userId));
        sessionRedisTemplate.delete(sessionKey);
        log.info("User {} removed from Redis", userId);
    }
}