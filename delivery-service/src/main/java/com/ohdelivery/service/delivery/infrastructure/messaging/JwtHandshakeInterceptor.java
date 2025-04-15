package com.ohdelivery.service.delivery.infrastructure.messaging;

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
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    private final WebClient webClient;

    @Value("${auth-server.validate-url}")
    private String validateUrl;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
        ServerHttpResponse response,
        WebSocketHandler wsHandler,
        Map<String, Object> attributes) {

        if (request instanceof ServletServerHttpRequest servletRequest) {
            HttpServletRequest req = servletRequest.getServletRequest();
            String token = req.getParameter("token");

            if (token == null || token.isBlank()) {
                return false;
            }

            // 비동기 처리를 동기화: block() 사용 (WebSocket 핸드셰이크는 동기 메서드)
            try {
                Passport passport = webClient.post()
                    .uri(validateUrl)
                    .bodyValue(token)
                    .retrieve()
                    .bodyToMono(Passport.class)
                    .block(); // 동기 방식으로 응답 받기

                attributes.put("passport", passport);
                log.info("WebSocket JWT 토큰 인증 성공!!");
                return true;

            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return false; // 인증 실패
            }
        }

        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request,
        ServerHttpResponse response,
        WebSocketHandler wsHandler,
        Exception exception) {
        // no-op
    }
}
