<<<<<<< HEAD:src/main/java/com/roomify/detection_be/Repository/RoleRepository.java
package com.roomify.detection_be.Repository;
=======
package com.roomify.detection_be.web.repository;
>>>>>>> develop:src/main/java/com/roomify/detection_be/web/repository/RoleRepository.java

import com.roomify.detection_be.web.entities.Role;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
  Optional<Role> findByName(String name);

  boolean existsByName(String name);
}
