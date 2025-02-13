package com.roomify.detection_be.Repository;

import com.roomify.detection_be.web.entity.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendShipRepository extends JpaRepository<Friendship,String> {
}
