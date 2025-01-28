package com.roomify.detection_be.config;

import com.roomify.detection_be.web.constants.RedisKeyPrefix;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import com.roomify.detection_be.web.entity.res.UserGenerateRes;

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
    public RedisTemplate<String, UserGenerateRes> userRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, UserGenerateRes> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(UserGenerateRes.class));
        template.setKeySerializer(new StringRedisSerializer());

        return template;
    }

    @Bean
    public RedisTemplate<String, String> sessionRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());

        return template;
    }
    @Bean
    public CommandLineRunner startupCleaner(RedisTemplate<String, UserGenerateRes> userRedisTemplate,
                                            RedisTemplate<String, String> sessionRedisTemplate) {
        return args -> {
            clearRedis(userRedisTemplate, sessionRedisTemplate);
        };
    }

    @Bean
    public Thread shutdownHook(RedisTemplate<String, UserGenerateRes> userRedisTemplate,
                               RedisTemplate<String, String> sessionRedisTemplate) {
        Thread shutdownThread = new Thread(() -> {
            clearRedis(userRedisTemplate, sessionRedisTemplate);
        });

        Runtime.getRuntime().addShutdownHook(shutdownThread);
        return shutdownThread;
    }

    private void clearRedis(RedisTemplate<String, UserGenerateRes> userRedisTemplate,
                            RedisTemplate<String, String> sessionRedisTemplate) {
        try {
            Set<String> userKeys = userRedisTemplate.keys(RedisKeyPrefix.USER_KEY_PREFIX + "*");
            if (userKeys != null && !userKeys.isEmpty()) {
                userRedisTemplate.delete(userKeys);
            }

            Set<String> sessionKeys = sessionRedisTemplate.keys(RedisKeyPrefix.SESSION_KEY_PREFIX + "*");
            if (sessionKeys != null && !sessionKeys.isEmpty()) {
                sessionRedisTemplate.delete(sessionKeys);
            }
        } catch (Exception e) {
        }
    }
}