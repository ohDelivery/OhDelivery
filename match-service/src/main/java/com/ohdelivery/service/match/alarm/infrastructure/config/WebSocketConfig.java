package com.ohdelivery.service.match.alarm.infrastructure.config;

import com.ohdelivery.service.match.alarm.application.websocket.CustomHandshakeHandler;
import com.ohdelivery.service.match.alarm.application.websocket.CustomHandshakeInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

  private final CustomHandshakeInterceptor customHandshakeInterceptor;
  private final CustomHandshakeHandler customHandshakeHandler;

  @Override
  public void configureMessageBroker(MessageBrokerRegistry config) {
    config.enableSimpleBroker("/topic");
    config.setUserDestinationPrefix("/user"); // convertAndSendToUser() 호출 시 메시지를 보낼 때 사용하는 접두사
  }

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint("/ws/alarm")
        .addInterceptors(customHandshakeInterceptor)  // riderId 추출
        .setHandshakeHandler(customHandshakeHandler)  // riderId를 Principal로 설정
        .setAllowedOriginPatterns("*")
        .withSockJS();
  }
}
