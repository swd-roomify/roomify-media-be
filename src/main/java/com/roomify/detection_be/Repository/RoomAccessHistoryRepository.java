package com.roomify.detection_be.Repository;

import com.roomify.detection_be.web.entity.RoomAccessHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomAccessHistoryRepository extends JpaRepository<RoomAccessHistory, Integer> {
}
