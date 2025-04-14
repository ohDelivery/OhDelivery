package com.ohdelivery.service.delivery.presentation.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ohdelivery.service.delivery.application.dto.RiderLocation;
import com.ohdelivery.service.delivery.application.service.DeliveryService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@Component
@RequiredArgsConstructor
public class RiderWebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final DeliveryService deliveryService;

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message)
        throws IOException {
        String payload = message.getPayload();
        RiderLocation location = objectMapper.readValue(payload, RiderLocation.class);

        log.info("라이더 위치 수신: {}", location.toString());

        // TODO: Redis 저장, Kafka 전송 등 처리
        deliveryService.saveRiderLocation(location);
    }
}
