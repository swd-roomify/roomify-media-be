package com.roomify.detection_be.config;

import java.util.Map;
import lombok.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Configuration
public class HttpSessionConfig {
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
        response.getHeaders().add("Access-Control-Allow-Origin", "*");
        response.getHeaders().add("Access-Control-Allow-Credentials", "true");

        return super.beforeHandshake(request, response, wsHandler, attributes);
      }
    };
  }
}
