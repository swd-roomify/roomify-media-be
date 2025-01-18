package com.roomify.detection_be.web.controller;

import com.roomify.detection_be.web.constants.RedisKeyPrefix;
import com.roomify.detection_be.web.entity.User;
import com.roomify.detection_be.web.entity.payload.UserMove;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
public class MoveController {
    private static final Logger log = LoggerFactory.getLogger(MoveController.class);
    private final RedisTemplate<String, User> userRedisTemplate;
    private final SimpMessagingTemplate messagingTemplate;
    private final String WS_USER_KEY_PREFIX = RedisKeyPrefix.USER_KEY_PREFIX;

    public MoveController(
            RedisTemplate<String, User> userRedisTemplate,
            SimpMessagingTemplate messagingTemplate) {
        this.userRedisTemplate = userRedisTemplate;
        this.messagingTemplate = messagingTemplate;
    }

    private String getUserKey(String username) {
        return WS_USER_KEY_PREFIX + username;
    }

    private Map<String, User> convertUserRedisToMap() {
        return Objects.requireNonNull(userRedisTemplate.keys(WS_USER_KEY_PREFIX + "*"))
                .stream()
                .collect(Collectors.toMap(
                        key -> key.substring(WS_USER_KEY_PREFIX.length()),
                        key -> userRedisTemplate.opsForValue().get(key)
                ));
    }

    @MessageMapping(Path.PATH)
    public void move(@Payload UserMove message) {
        User user = User.builder()
                .username(message.getUsername())
                .positionX(message.getPositionX())
                .positionY(message.getPositionY())
                .build();

        userRedisTemplate.opsForValue().set(getUserKey(message.getUsername()), user);
        messagingTemplate.convertAndSend(Path.TOPIC_POSITION, convertUserRedisToMap());
    }
}
