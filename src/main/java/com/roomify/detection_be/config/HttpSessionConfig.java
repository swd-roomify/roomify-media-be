package com.roomify.detection_be.config;

import java.util.Map;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Configuration
public class HttpSessionConfig {
  private static final Logger log = LoggerFactory.getLogger(HttpSessionConfig.class);

  @Bean
  public HttpSessionHandshakeInterceptor corsInterceptor() {
    return new HttpSessionHandshakeInterceptor() {
      @Override
      public boolean beforeHandshake(
          @NonNull ServerHttpRequest request,
          @NonNull ServerHttpResponse response,
          @NonNull WebSocketHandler wsHandler,
          @NonNull Map<String, Object> attributes)
          throws Exception {

        String origin = request.getHeaders().getOrigin();
        log.info("Request Origin: {}", origin);

        if ("http://pog.threemusketeer.click:5173".equals(origin) ||
                "http://localhost:5173".equals(origin)) {
          response.getHeaders().add("Access-Control-Allow-Origin", origin);
          response.getHeaders().add("Access-Control-Allow-Credentials", "true");
          log.info("Set Access-Control-Allow-Origin: {}", origin);
        } else {
          log.warn("Origin not allowed: {}", origin);
        }

        return super.beforeHandshake(request, response, wsHandler, attributes);
      }
    };
  }
}
