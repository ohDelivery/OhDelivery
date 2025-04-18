package com.ohdelivery.service.match.alarm.application.websocket;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

@Slf4j
public class CustomHandshakeInterceptor implements HandshakeInterceptor {

  @Override
  public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
      WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {

    if (request instanceof ServletServerHttpRequest serverRequest) {    // http 서블릿 요청일 경우
      HttpServletRequest req = serverRequest.getServletRequest();
      String riderId = req.getParameter("riderId");
      log.info("CustomHandshakeInterceptor beforeHandshake riderId={}", riderId);
      attributes.put("riderId", riderId);
    }
    return true;
  }

  @Override
  public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
      WebSocketHandler wsHandler, Exception exception) {

  }
}
