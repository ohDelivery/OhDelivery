package com.ohdelivery.service.delivery.infrastructure.config;

import com.ohdelivery.service.delivery.infrastructure.security.JwtHandshakeInterceptor;
import com.ohdelivery.service.delivery.presentation.handler.ConsumerWebSocketHandler;
import com.ohdelivery.service.delivery.presentation.handler.RiderWebSocketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final RiderWebSocketHandler riderWebSocketHandler;
    private final ConsumerWebSocketHandler consumerWebSocketHandler;
    private final JwtHandshakeInterceptor jwtHandshakeInterceptor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(riderWebSocketHandler, "/ws/delivery/rider")
            .addInterceptors(jwtHandshakeInterceptor)
            .setAllowedOrigins("*"); // CORS 필요시 제한
        registry.addHandler(consumerWebSocketHandler, "/ws/delivery/consumer")
            .addInterceptors(jwtHandshakeInterceptor)
            .setAllowedOrigins("*"); // CORS 필요시 제한
    }
}
