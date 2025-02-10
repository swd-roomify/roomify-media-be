package com.roomify.detection_be.config;

import com.roomify.detection_be.web.constants.RedisKeyPrefix;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
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
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(UserWSRes.class));

        return template;
    }

    @Bean
    public HashOperations<String, String, UserWSRes> roomUserHashOperations(
            RedisTemplate<String, UserWSRes> roomUserRedisTemplate) {
        return roomUserRedisTemplate.opsForHash();
    }

    @Bean
    public RedisTemplate<String, String> roomSessionRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new StringRedisSerializer());

        return template;
    }

    @Bean
    public HashOperations<String, String, String> roomSessionHashOperations(
            RedisTemplate<String, String> roomSessionRedisTemplate) {
        return roomSessionRedisTemplate.opsForHash();
    }

    @Bean
    public CommandLineRunner startupCleaner(RedisTemplate<String, UserWSRes> roomUserRedisTemplate,
                                            RedisTemplate<String, String> roomSessionRedisTemplate) {
        return args -> {
            clearRedis(roomUserRedisTemplate, roomSessionRedisTemplate);
        };
    }

    @Bean
    public Thread shutdownHook(RedisTemplate<String, UserWSRes> roomUserRedisTemplate,
                               RedisTemplate<String, String> roomSessionRedisTemplate) {
        Thread shutdownThread = new Thread(() -> {
            clearRedis(roomUserRedisTemplate, roomSessionRedisTemplate);
        });

        Runtime.getRuntime().addShutdownHook(shutdownThread);
        return shutdownThread;
    }

    private void clearRedis(RedisTemplate<String, UserWSRes> roomUserRedisTemplate,
                            RedisTemplate<String, String> roomSessionRedisTemplate) {
        try {
            Set<String> userKeys = roomUserRedisTemplate.keys(RedisKeyPrefix.USER_KEY_PREFIX + "*");
            if (userKeys != null && !userKeys.isEmpty()) {
                roomUserRedisTemplate.delete(userKeys);
            }

            Set<String> sessionKeys = roomSessionRedisTemplate.keys(RedisKeyPrefix.SESSION_KEY_PREFIX + "*");
            if (sessionKeys != null && !sessionKeys.isEmpty()) {
                roomSessionRedisTemplate.delete(sessionKeys);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}