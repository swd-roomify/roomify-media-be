package com.roomify.detection_be.web.service;

import com.roomify.detection_be.Repository.FriendShipRepository;
import com.roomify.detection_be.Repository.UserRepository;
import com.roomify.detection_be.service.basicOauth.UserServiceOauth;
import com.roomify.detection_be.utility.SnowflakeGenerator;
import com.roomify.detection_be.web.controller.websocket.ConnectionWebSocket;
import com.roomify.detection_be.web.entity.Friendship;
import com.roomify.detection_be.web.entity.Users.User;
import com.roomify.detection_be.web.entity.req.UserGenerateReq;
import com.roomify.detection_be.web.entity.res.UserGenerateRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService {
    private final RedisTemplate<String, UserGenerateRes> userRedisTemplate;
    private static final Logger log = LoggerFactory.getLogger(ConnectionWebSocket.class);
    private final UserServiceOauth userServiceOauth;
    private final UserRepository userRepository;
    private final FriendShipRepository friendShipRepository;

    public UserService(RedisTemplate<String, UserGenerateRes> userRedisTemplate, UserServiceOauth userServiceOauth, UserRepository userRepository, FriendShipRepository friendShipRepository) {
        this.userRedisTemplate = userRedisTemplate;
        this.userServiceOauth = userServiceOauth;
        this.userRepository = userRepository;
        this.friendShipRepository = friendShipRepository;
    }

    public UserGenerateRes generateUser(UserGenerateReq user) {
        SnowflakeGenerator snowflake = new SnowflakeGenerator(1);
        String userId = String.valueOf(snowflake.nextId());

        UserGenerateRes newUserGenerateRes =
                UserGenerateRes.builder()
                        .userId(userId)
                        .username(user.getUsername())
                        .positionX(200)
                        .positionY(200)
                        .character(user.getCharacter())
                        .build();
        userRedisTemplate.opsForValue().set("USER_" + user, newUserGenerateRes);

        return newUserGenerateRes;
    }

    public ResponseEntity<String> generateFriendShip(String userIdUser2) {
        Friendship friendship = new Friendship();
        Optional<User> user = userServiceOauth.findCurrentUser();
        if (user.isPresent()) {
            friendship.setUser1(userRepository.findById(user.get().getUserId()).orElseThrow());
            friendship.setUser2(userRepository.findById(userIdUser2).orElseThrow());
            friendship.setStatus("PENDING");
            friendShipRepository.save(friendship);
        }
        return ResponseEntity.ok("Friend request sent.");

    }

    public ResponseEntity<String> removeFriendShip(String userIdUser2) {
        Optional<User> user1 = userServiceOauth.findCurrentUser();
        if (user1.isPresent()) {
            Friendship friendship = friendShipRepository.findByUser1AndUser2(user1, userRepository.findById(userIdUser2))
                    .orElseThrow(() -> new RuntimeException("Friendship not found"));
            friendShipRepository.delete(friendship);
        }

        return ResponseEntity.ok("Unfriended successfully.");
    }
}
