package com.ohdelivery.service.delivery.presentation.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ohdelivery.common.passport.Passport;
import com.ohdelivery.common.passport.RoleType;
import com.ohdelivery.service.delivery.application.dto.request.RiderLocationRequest;
import com.ohdelivery.service.delivery.application.observer.BroadcasterManager;
import com.ohdelivery.service.delivery.application.observer.LocationBroadcaster;
import com.ohdelivery.service.delivery.application.service.WebSocketEventService;
import java.io.IOException;
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
public class RiderWebSocketHandler extends TextWebSocketHandler {

//    private final Map<String, WebSocketSession> riderSessions = new ConcurrentHashMap<>();

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final WebSocketEventService webSocketEventService;
    private final BroadcasterManager broadcasterManager;

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message)
        throws IOException {
        String payload = message.getPayload();
        RiderLocationRequest location = objectMapper.readValue(payload, RiderLocationRequest.class);

        log.info("라이더 위치 수신: {}", location.toString());

        webSocketEventService.saveRiderLocation(location);

        LocationBroadcaster locationBroadcaster = broadcasterManager.getBroadcaster(
            location.getRiderId());

        locationBroadcaster.notifyObservers(location.toMessage());
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String riderId = getRiderId(session);
//        riderSessions.put(riderId, session);
        broadcasterManager.addBroadcaster(riderId);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status)
        throws Exception {
        String riderId = getRiderId(session);
//        riderSessions.remove(riderId);
        broadcasterManager.removeBroadcaster(riderId);
    }

    private String getRiderId(WebSocketSession session) throws IOException {
        Passport passport = (Passport) session.getAttributes().get("passport");
        if (passport.getRoleType() != RoleType.RIDER) {
            session.close(CloseStatus.NOT_ACCEPTABLE);
        }
        return passport.getUserId();
    }
}
