package com.ohdelivery.service.consult.chat.infrastructure;

import com.ohdelivery.common.passport.Passport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@RequiredArgsConstructor
@Component
public class ChatChannelInterceptor implements ChannelInterceptor {

  private final String BEARER_PREFIX = "Bearer " ;
  private final WebClient webClient;

  @Value("${auth-server.validate-url}")
  private String validateUrl;

  @Override
  public Message<?> preSend(Message<?> message, MessageChannel channel) {
    StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
    handleMessage(accessor);
    return message;
  }

  private void handleMessage(StompHeaderAccessor accessor) {
    switch (accessor.getCommand()) {
      case CONNECT:
        connectChatRoom(accessor);
        break;
      case SEND:
        authenticateToken(accessor);
        break;
      case DISCONNECT:
        disconnectChatRoom(accessor);
        break;
      default:
        break;
    }
  }

  private String authenticateToken(StompHeaderAccessor accessor) {
    String token = accessor.getFirstNativeHeader("Authorization").substring(BEARER_PREFIX.length());
    try {
      Passport passport = webClient.post()
          .uri(validateUrl)
          .bodyValue(token)
          .retrieve()
          .bodyToMono(Passport.class)
          .block();
      return passport.getUserId();
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  private void connectChatRoom(StompHeaderAccessor accessor) {
  }

  private void disconnectChatRoom(StompHeaderAccessor accessor) {

  }
}
