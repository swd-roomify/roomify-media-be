package com.roomify.detection_be.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;

@Configuration
public class HttpSessionConfig {
  @Bean
  public HttpSessionHandshakeInterceptor corsInterceptor() {
    return new HttpSessionHandshakeInterceptor() {
      @Override
      public boolean beforeHandshake(
              ServerHttpRequest request,
              ServerHttpResponse response,
              WebSocketHandler wsHandler,
              Map<String, Object> attributes) throws Exception {
        // Set CORS headers directly on the ServerHttpResponse
        response.getHeaders().add("Access-Control-Allow-Origin", "http://pog.threemusketeer.click:5173");
        response.getHeaders().add("Access-Control-Allow-Credentials", "true");

        return super.beforeHandshake(request, response, wsHandler, attributes);
      }
    };
  }
}

