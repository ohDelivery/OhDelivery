package com.ohdelivery.service.delivery.presentation.handler;

import com.ohdelivery.service.delivery.application.dto.response.RiderLocationResponse;
import com.ohdelivery.service.delivery.application.service.WebSocketEventService;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConsumerWebSocketHandler extends TextWebSocketHandler {

    private final Map<WebSocketSession, UUID> sessionRiderMap = new ConcurrentHashMap<>();
    private final WebSocketEventService webSocketEventService;

    @PostConstruct
    public void init() {
        startBroadcasting();
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message)
        throws IOException {
        UUID riderId = UUID.fromString(message.getPayload());
        // TODO redis에 현재 라이더 위치 추적중인지 확인하고 아니라면 연결 끊기
        try {
            RiderLocationResponse response = webSocketEventService.getRiderLocation(riderId);
            if (response == null) {
                session.sendMessage(new TextMessage("현재 배달중인 주문이 아닙니다!"));
                session.close(CloseStatus.NORMAL);
            }
        } catch (Exception e) {
            session.sendMessage(new TextMessage("현재 배달중인 주문이 아닙니다!"));
            session.close(CloseStatus.NORMAL);
        }
        sessionRiderMap.put(session, riderId);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessionRiderMap.remove(session);
    }

    private void startBroadcasting() {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            try {
                log.info("send rider locations");
                for (Map.Entry<WebSocketSession, UUID> entry : sessionRiderMap.entrySet()) {
                    WebSocketSession session = entry.getKey();
                    UUID riderId = entry.getValue();

                    RiderLocationResponse response = webSocketEventService.getRiderLocation(
                        riderId);
                    String message = String.format("{\"latitude\": %.6f, \"longitude\": %.6f}",
                        response.getLatitude(), response.getLongitude());

                    try {
                        session.sendMessage(new TextMessage(message));
                    } catch (IOException e) {
                        session.close(CloseStatus.NORMAL);
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 5, TimeUnit.SECONDS);
    }

}
