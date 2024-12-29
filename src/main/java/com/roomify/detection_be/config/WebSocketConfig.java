package com.roomify.detection_be.config;

import com.roomify.detection_be.web.controller.Path;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
  private final HttpSessionHandshakeInterceptor corsInterceptor;

  public WebSocketConfig(HttpSessionHandshakeInterceptor corsInterceptor) {
    this.corsInterceptor = corsInterceptor;
  }

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry
        .addEndpoint(Path.WEBSOCKET_ENDPOINT)
        .setAllowedOriginPatterns("*")
        .addInterceptors(corsInterceptor)
        .withSockJS();
  }

  @Override
  public void configureMessageBroker(MessageBrokerRegistry registry) {
    registry.setApplicationDestinationPrefixes(Path.APP);
    registry.enableSimpleBroker(Path.TOPIC);
  }
}
