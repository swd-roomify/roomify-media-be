package com.roomify.detection_be.service.jwt;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenBlacklistService {
  private final StringRedisTemplate redisTemplate;
  private static final String BLACKLIST_PREFIX = "blacklist:";

  public void blacklistToken(String token, long expirationTime) {
    redisTemplate
        .opsForValue()
        .set(BLACKLIST_PREFIX + token, "blacklisted", Duration.ofMillis(expirationTime));
  }

  public boolean isTokenBlacklisted(String token) {
    return redisTemplate.hasKey(BLACKLIST_PREFIX + token);
  }
}
