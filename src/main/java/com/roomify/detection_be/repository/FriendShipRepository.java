package com.roomify.detection_be.repository;

import com.roomify.detection_be.web.entities.Friendship;
import com.roomify.detection_be.web.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FriendShipRepository extends JpaRepository<Friendship,String> {
    Optional<Friendship> findByUser1AndUser2(Optional<User> user1, Optional<User> byId);
}
