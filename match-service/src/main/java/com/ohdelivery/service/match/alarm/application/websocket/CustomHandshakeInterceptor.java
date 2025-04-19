package com.ohdelivery.service.match.alarm.application.websocket;

import com.ohdelivery.common.passport.Passport;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomHandshakeInterceptor implements HandshakeInterceptor {

  private final WebClient webClient;

  @Value("${auth-server.validate-url}")
  private String validateUrl;

  @Override
  public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
      WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {

    // 실제 HTTP 요청 객체인 HttpServletRequest로 변환하여 파라미터를 가져옴
    if (request instanceof ServletServerHttpRequest serverRequest) {
      HttpServletRequest req = serverRequest.getServletRequest();
      String token = req.getParameter("token");
      log.info("CustomHandshakeInterceptor beforeHandshake token={}", token);

      try {
        // WebClient를 사용하여 JWT 토큰 검증 요청 (동기 처리)
        Passport passport = webClient.post()
            .uri(validateUrl)
            .bodyValue(token)
            .retrieve()
            .bodyToMono(Passport.class)
            .block();

        // 사용자 정보 저장
        attributes.put("userId", passport.getUserId());
        log.info("WebSocket JWT 토큰 인증 성공 UserId : {}", passport.getUserId());
        return true;

      } catch (Exception e) {
        log.error(e.getMessage(), e);
        return false;
      }
    }
    return false;
  }

  @Override
  public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
      WebSocketHandler wsHandler, Exception exception) {

  }
}
