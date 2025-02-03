package com.roomify.detection_be.web.service;


import com.roomify.detection_be.utility.SnowflakeGenerator;
import com.roomify.detection_be.web.constants.RedisKeyPrefix;
import com.roomify.detection_be.web.entity.req.UserJoinReq;
import com.roomify.detection_be.web.entity.res.UserGenerateRes;
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
    private final RedisTemplate<String, UserGenerateRes> userRedisTemplate;
    private final RedisTemplate<String, String> sessionRedisTemplate;
    private final String WS_USER_KEY_PREFIX = RedisKeyPrefix.USER_KEY_PREFIX;

    public ConnectionService(RedisTemplate<String, UserGenerateRes> userRedisTemplate,
                             RedisTemplate<String, String> sessionRedisTemplate) {
        this.userRedisTemplate = userRedisTemplate;
        this.sessionRedisTemplate = sessionRedisTemplate;
    }

    private String getUserKey(String username) {
        return WS_USER_KEY_PREFIX + username;
    }

    private String getSessionKey(String sessionId) {
        String WS_SESSION_KEY_PREFIX = RedisKeyPrefix.SESSION_KEY_PREFIX;
        return WS_SESSION_KEY_PREFIX + sessionId;
    }

    public String saveUserAndSession(UserJoinReq message, String sessionId) {
        String username = message.getUsername();
        String userId = message.getUserId();
        String character = message.getCharacter();
        sessionRedisTemplate.opsForValue().set(getSessionKey(sessionId), username);
        return userId;
    }

    public Map<String, UserGenerateRes> getAllUsers() {
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

        userRedisTemplate.delete(getUserKey(username));
        sessionRedisTemplate.delete(sessionKey);
        log.info("User {} removed from Redis", username);
    }

}