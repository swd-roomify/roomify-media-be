package com.roomify.detection_be.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@Data
public class JwtConfig {

  @Value("${jwt.url}")
  private String url;

  @Value("${jwt.header}")
  private String header;

  @Value("${jwt.prefix}")
  private String prefix;

  @Value("${jwt.expiration}")
  private int expiration;

  @Value("${jwt.secret}")
  private String secret;
}
