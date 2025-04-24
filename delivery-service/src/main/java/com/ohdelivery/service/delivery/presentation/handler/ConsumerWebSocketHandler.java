package com.ohdelivery.service.delivery.presentation.handler;

import com.ohdelivery.service.delivery.application.observer.BroadcasterManager;
import com.ohdelivery.service.delivery.application.observer.LocationBroadcaster;
import com.ohdelivery.service.delivery.infrastructure.observer.WebSocketLocationObserver;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
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

    private final Map<WebSocketSession, WebSocketLocationObserver> consumerSessions = new ConcurrentHashMap<>();
    private final BroadcasterManager broadcasterManager;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        String riderId = message.getPayload();
        LocationBroadcaster broadcaster = broadcasterManager.getBroadcaster(riderId);
        WebSocketLocationObserver observer = new WebSocketLocationObserver(session, riderId);
        broadcaster.registerObserver(observer);
        consumerSessions.put(session, observer);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        WebSocketLocationObserver observer = consumerSessions.get(session);
        LocationBroadcaster broadcaster = broadcasterManager.getBroadcaster(observer.getRiderId());
        broadcaster.removeObserver(observer);
        consumerSessions.remove(session);
    }
}
