<<<<<<< HEAD:src/main/java/com/roomify/detection_be/Repository/RoomParticipantRepository.java
package com.roomify.detection_be.Repository;
=======
package com.roomify.detection_be.web.repository;
>>>>>>> develop:src/main/java/com/roomify/detection_be/web/repository/RoomParticipantRepository.java

import com.roomify.detection_be.web.entities.RoomParticipant;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomParticipantRepository extends JpaRepository<RoomParticipant, String> {
  void deleteByRoomIdAndUserUserId(String roomId, String userId);

  List<RoomParticipant> findUsersByRoomId(String roomId);
}
