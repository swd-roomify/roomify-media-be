package com.roomify.detection_be.Repository;

import com.roomify.detection_be.web.entity.Room;
import com.roomify.detection_be.web.entity.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomTypeRepository extends JpaRepository<RoomType, String> {}
