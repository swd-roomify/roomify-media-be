package com.roomify.detection_be.web.service;


import com.roomify.detection_be.utility.SnowflakeGenerator;
import com.roomify.detection_be.web.constants.RedisKeyPrefix;
import com.roomify.detection_be.web.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ConnectionService {
    private static final Logger log = LoggerFactory.getLogger(ConnectionService.class);
    private final RedisTemplate<String, User> userRedisTemplate;
    private final RedisTemplate<String, String> sessionRedisTemplate;
    private final String WS_USER_KEY_PREFIX = RedisKeyPrefix.USER_KEY_PREFIX;
    private final String WS_SESSION_KEY_PREFIX = RedisKeyPrefix.SESSION_KEY_PREFIX;

    public ConnectionService(RedisTemplate<String, User> userRedisTemplate,
                             RedisTemplate<String, String> sessionRedisTemplate) {
        this.userRedisTemplate = userRedisTemplate;
        this.sessionRedisTemplate = sessionRedisTemplate;
    }

    private String getUserKey(String username) {
        return WS_USER_KEY_PREFIX + username;
    }

    private String getSessionKey(String sessionId) {
        return WS_SESSION_KEY_PREFIX + sessionId;
    }

    public String saveUserAndSession(User message, String sessionId) {
        String username = message.getUsername();
        String userId;

        User existingUser = userRedisTemplate.opsForValue().get(getUserKey(username));
        if (existingUser != null) {
            log.info("User {} already exists in Redis", username);
            userId = existingUser.getUserId();
        } else {
            SnowflakeGenerator snowflake = new SnowflakeGenerator(1); // Node ID = 1
            userId = String.valueOf(snowflake.nextId());
            userRedisTemplate.opsForValue().setIfAbsent(
                    getUserKey(username),
                    User.builder()
                            .userId(userId)
                            .username(username)
                            .positionX(0)
                            .positionY(0)
                            .build()
            );
            log.info("New user {} with ID {} created in Redis", username, userId);
        }

        sessionRedisTemplate.opsForValue().set(getSessionKey(sessionId), username);
        return userId;
    }

    public Map<String, User> getAllUsers() {
        return Objects.requireNonNull(userRedisTemplate.keys(WS_USER_KEY_PREFIX + "*"))
                .stream()
                .collect(Collectors.toMap(
                        key -> key.substring(WS_USER_KEY_PREFIX.length()),
                        key -> userRedisTemplate.opsForValue().get(key)
                ));
    }

    public void removeUserBySession(String sessionId) {
        String sessionKey = getSessionKey(sessionId);
        String username = sessionRedisTemplate.opsForValue().get(sessionKey);

        if (username != null) {
            userRedisTemplate.delete(getUserKey(username));
            sessionRedisTemplate.delete(sessionKey);
            log.info("User {} removed from Redis", username);
        }
    }
}