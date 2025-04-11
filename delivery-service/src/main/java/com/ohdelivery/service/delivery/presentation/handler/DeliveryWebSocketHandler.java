package com.ohdelivery.service.delivery.presentation.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ohdelivery.service.delivery.application.dto.RiderLocationEvent;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@Component
public class DeliveryWebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message)
        throws IOException {
        String payload = message.getPayload();
        RiderLocationEvent location = objectMapper.readValue(payload, RiderLocationEvent.class);

        log.info("라이더 위치 수신: {}", location.toString());

        // TODO: Redis 저장, Kafka 전송 등 처리
    }
}
