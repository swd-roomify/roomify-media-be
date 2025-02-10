package com.roomify.detection_be.repository;

import com.roomify.detection_be.web.entities.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, String> {
}
