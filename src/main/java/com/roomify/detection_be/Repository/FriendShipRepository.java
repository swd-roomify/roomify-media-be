<<<<<<< HEAD:src/main/java/com/roomify/detection_be/Repository/FriendShipRepository.java
package com.roomify.detection_be.Repository;
=======
package com.roomify.detection_be.web.repository;
>>>>>>> develop:src/main/java/com/roomify/detection_be/web/repository/FriendShipRepository.java

import com.roomify.detection_be.web.entities.Friendship;
import com.roomify.detection_be.web.entities.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
<<<<<<< HEAD:src/main/java/com/roomify/detection_be/Repository/FriendShipRepository.java
import org.springframework.stereotype.Repository;

@Repository
=======

>>>>>>> develop:src/main/java/com/roomify/detection_be/web/repository/FriendShipRepository.java
public interface FriendShipRepository extends JpaRepository<Friendship, String> {
  Optional<Friendship> findByUser1AndUser2(Optional<User> user1, Optional<User> byId);
}
