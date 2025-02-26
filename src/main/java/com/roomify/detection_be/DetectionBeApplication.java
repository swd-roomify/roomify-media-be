package com.roomify.detection_be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class DetectionBeApplication {
  public static void main(String[] args) {
    SpringApplication.run(DetectionBeApplication.class, args);
  }
}

