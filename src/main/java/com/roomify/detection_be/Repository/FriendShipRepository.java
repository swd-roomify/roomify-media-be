package com.roomify.detection_be.Repository;

import com.roomify.detection_be.web.entity.Friendship;
import com.roomify.detection_be.web.entity.Users.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FriendShipRepository extends JpaRepository<Friendship,String> {
    Optional<Friendship> findByUser1AndUser2(Optional<User> user1, Optional<User> byId);
}
