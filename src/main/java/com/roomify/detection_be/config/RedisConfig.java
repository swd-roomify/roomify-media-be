package com.roomify.detection_be.config;

import com.roomify.detection_be.constants.RedisKeyPrefix;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import com.roomify.detection_be.web.dtos.res.UserWSRes;


import java.util.Set;

@Configuration
public class RedisConfig {
    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(redisHost, redisPort);
    }

    @Bean
    public RedisTemplate<String, UserWSRes> roomUserRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, UserWSRes> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        return template;
    }

    @Bean
    public HashOperations<String, String, UserWSRes> roomUserHashOperations(
            RedisTemplate<String, UserWSRes> roomUserRedisTemplate) {
        return roomUserRedisTemplate.opsForHash();
    }

    @Bean
    public CommandLineRunner startupCleaner(RedisTemplate<String, UserWSRes> roomUserRedisTemplate) {
        return args -> {
            clearRedis(roomUserRedisTemplate);
        };
    }

    @Bean
    public Thread shutdownHook(RedisTemplate<String, UserWSRes> roomUserRedisTemplate) {
        Thread shutdownThread = new Thread(() -> {
            clearRedis(roomUserRedisTemplate);
        });

        Runtime.getRuntime().addShutdownHook(shutdownThread);
        return shutdownThread;
    }

    private void clearRedis(RedisTemplate<String, UserWSRes> roomUserRedisTemplate) {
        try {
            Set<String> sessionKeys = roomUserRedisTemplate.keys(RedisKeyPrefix.SESSION_KEY_PREFIX + "*");
            if (sessionKeys != null && !sessionKeys.isEmpty()) {
                roomUserRedisTemplate.delete(sessionKeys);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
