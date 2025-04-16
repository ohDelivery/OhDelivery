package com.ohdelivery.service.delivery.presentation.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ohdelivery.service.delivery.application.dto.request.RiderLocationRequest;
import com.ohdelivery.service.delivery.application.service.WebSocketEventService;
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
    private final WebSocketEventService webSocketEventService;

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message)
        throws IOException {
        String payload = message.getPayload();
        RiderLocationRequest location = objectMapper.readValue(payload, RiderLocationRequest.class);

        log.info("라이더 위치 수신: {}", location.toString());

        webSocketEventService.saveRiderLocation(location);
    }
}
