package com.roomify.detection_be.config;

import com.roomify.detection_be.web.controller.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.security.Principal;
import java.util.Map;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
  private static final Logger log = LoggerFactory.getLogger(WebSocketConfig.class);
  private final HttpSessionHandshakeInterceptor corsInterceptor;

  public WebSocketConfig(HttpSessionHandshakeInterceptor corsInterceptor) {
    this.corsInterceptor = corsInterceptor;
  }

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry
        .addEndpoint(Path.WEBSOCKET_ENDPOINT)
        .setAllowedOriginPatterns("http://pog.threemusketeer.click:5173", "http://localhost:5173")
        .addInterceptors(corsInterceptor)
        .setHandshakeHandler(new DefaultHandshakeHandler() {
          @Override
          protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
            HttpHeaders headers = request.getHeaders();
            log.debug("Request Headers: {}", headers);
            return super.determineUser(request, wsHandler, attributes);
          }
        })
        .withSockJS();
  }

  @Override
  public void configureMessageBroker(MessageBrokerRegistry registry) {
    registry.setApplicationDestinationPrefixes(Path.APP);
    registry.enableSimpleBroker(Path.TOPIC);
  }
}
