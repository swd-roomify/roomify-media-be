package com.roomify.detection_be.web.service;

import com.roomify.detection_be.web.constants.RedisKeyPrefix;
import com.roomify.detection_be.web.entity.res.UserGenerateRes;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class MoveService {
    private final RedisTemplate<String, UserGenerateRes> userRedisTemplate;
    private final String WS_USER_KEY_PREFIX = RedisKeyPrefix.USER_KEY_PREFIX;

    public MoveService(RedisTemplate<String, UserGenerateRes> userRedisTemplate) {
        this.userRedisTemplate = userRedisTemplate;
    }

    private String getUserKey(String username) {
        return WS_USER_KEY_PREFIX + username;
    }

    public void saveUserPosition(UserGenerateRes userGenerateRes) {
        userRedisTemplate.opsForValue().set(getUserKey(userGenerateRes.getUsername()), userGenerateRes);
    }

    public Map<String, UserGenerateRes> getAllUsers() {
        return Objects.requireNonNull(userRedisTemplate.keys(WS_USER_KEY_PREFIX + "*")).stream()
                .collect(
                        Collectors.toMap(
                                key -> key.substring(WS_USER_KEY_PREFIX.length()),
                                key -> userRedisTemplate.opsForValue().get(key)));
    }
}
