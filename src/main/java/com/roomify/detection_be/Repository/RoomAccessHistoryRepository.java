package com.roomify.detection_be.Repository;

import com.roomify.detection_be.web.entity.RoomAccessHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RoomAccessHistoryRepository extends JpaRepository<RoomAccessHistory, Integer> {
    List<RoomAccessHistory> findByRoomId(String roomId);
}
