package com.ohdelivery.service.match.alarm.application.websocket;

import java.security.Principal;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

@Slf4j
public class CustomHandshakeHandler extends DefaultHandshakeHandler {

  // HandshakeInterceptor에서 전달받은 riderId를 Principal로 설정
  @Override
  protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler,
      Map<String, Object> attributes) {
    String riderId = (String) attributes.get("riderId");
    log.info("CustomHandshakeHandler determineUser riderId={}", riderId);
    return new StompPrincipal(riderId);
  }
}
