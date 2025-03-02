package com.roomify.detection_be.web.service;

import com.roomify.detection_be.web.repository.UserRepository;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UsernameGenerator {

  private final UserRepository userRepository;

  private static final String CHAR_POOL =
      "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

  public String generateUniqueUsername() {
    Random random = new Random();
    String newUsername;
    do {
      newUsername = generateRandomString(random, 6);
    } while (userRepository.existsByUsername(newUsername));
    return newUsername;
  }

  private String generateRandomString(Random random, int length) {
    StringBuilder sb = new StringBuilder(length);
    for (int i = 0; i < length; i++) {
      sb.append(CHAR_POOL.charAt(random.nextInt(CHAR_POOL.length())));
    }
    return sb.toString();
  }
}
