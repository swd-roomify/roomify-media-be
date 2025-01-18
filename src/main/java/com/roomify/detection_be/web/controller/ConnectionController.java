package com.roomify.detection_be.web.controller;

import com.roomify.detection_be.utils.SnowflakeGenerator;
import com.roomify.detection_be.web.constants.RedisKeyPrefix;
import com.roomify.detection_be.web.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
public class ConnectionController {
    private static final Logger log = LoggerFactory.getLogger(ConnectionController.class);
    private final RedisTemplate<String, User> userRedisTemplate;
    private final RedisTemplate<String, String> sessionRedisTemplate;
    private final SimpMessagingTemplate messagingTemplate;
    private final String WS_USER_KEY_PREFIX = RedisKeyPrefix.USER_KEY_PREFIX;

    public ConnectionController(
            RedisTemplate<String, User> userRedisTemplate,
            RedisTemplate<String, String> sessionRedisTemplate,
            SimpMessagingTemplate messagingTemplate) {
        this.userRedisTemplate = userRedisTemplate;
        this.sessionRedisTemplate = sessionRedisTemplate;
        this.messagingTemplate = messagingTemplate;
    }

    private String getUserKey(String username) {
        return WS_USER_KEY_PREFIX + username;
    }

    private String getSessionKey(String sessionId) {
        String WS_SESSION_KEY_PREFIX = RedisKeyPrefix.SESSION_KEY_PREFIX;
        return WS_SESSION_KEY_PREFIX + sessionId;
    }

    private Map<String, User> convertUserRedisToMap() {
        return Objects.requireNonNull(userRedisTemplate.keys(WS_USER_KEY_PREFIX + "*"))
                .stream()
                .collect(Collectors.toMap(
                        key -> key.substring(WS_USER_KEY_PREFIX.length()),
                        key -> userRedisTemplate.opsForValue().get(key)
                ));
    }

    public void join(User message, SimpMessageHeaderAccessor headerAccessor) {
        String username = message.getUsername();
        String sessionId = headerAccessor.getSessionId();
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
        messagingTemplate.convertAndSend(Path.TOPIC_POSITION, convertUserRedisToMap());

        log.info("User {} joined session {} with ID {}", username, sessionId, userId);
    }

    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();
        String sessionKey = getSessionKey(sessionId);

        String username = sessionRedisTemplate.opsForValue().get(sessionKey);
        if (username != null) {
            userRedisTemplate.delete(getUserKey(username));
            sessionRedisTemplate.delete(sessionKey);

            messagingTemplate.convertAndSend(Path.TOPIC_POSITION, convertUserRedisToMap());
        }
        log.info("User {} disconnected", username);
    }
}