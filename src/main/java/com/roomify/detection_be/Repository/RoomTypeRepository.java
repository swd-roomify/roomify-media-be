<<<<<<< HEAD:src/main/java/com/roomify/detection_be/Repository/RoomTypeRepository.java
package com.roomify.detection_be.Repository;
=======
package com.roomify.detection_be.web.repository;
>>>>>>> develop:src/main/java/com/roomify/detection_be/web/repository/RoomTypeRepository.java

import com.roomify.detection_be.web.entities.RoomType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomTypeRepository extends JpaRepository<RoomType, String> {
  boolean existsByName(String name);

  Optional<RoomType> findByName(String name);
}
