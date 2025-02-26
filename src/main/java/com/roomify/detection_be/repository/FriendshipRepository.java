package com.roomify.detection_be.Repository;

import com.roomify.detection_be.web.entities.Friendship;
import com.roomify.detection_be.web.entities.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendShipRepository extends JpaRepository<Friendship, String> {
    Optional<Friendship> findByUser1AndUser2(Optional<User> user1, Optional<User> byId);
}