package com.roomify.detection_be.utility.commandLineRunner;

import com.roomify.detection_be.Repository.RoleRepository;
import com.roomify.detection_be.web.entities.Role;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class RoleInitializer implements CommandLineRunner {

  private final RoleRepository roleRepository;

  public RoleInitializer(RoleRepository roleRepository) {
    this.roleRepository = roleRepository;
  }

  @Override
  @Transactional
  public void run(String... args) {
    List<String> defaultRoles = List.of("USER", "ADMIN");

    for (String roleName : defaultRoles) {
      if (!roleRepository.existsByName(roleName)) {
        Role role = new Role();
        role.setName(roleName);
        roleRepository.save(role);
      }
    }
  }
}
