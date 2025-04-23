package com.ohdelivery.service.delivery.infrastructure.observer;

import com.ohdelivery.service.delivery.application.observer.Observer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Slf4j
@Getter
@AllArgsConstructor
public class WebSocketLocationObserver implements Observer {

    private final WebSocketSession session;
    private final String riderId;

    @Override
    public void update(String location) {
        try {
            log.info("Observer sendMessage: {}", location);
            session.sendMessage(new TextMessage(location));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
