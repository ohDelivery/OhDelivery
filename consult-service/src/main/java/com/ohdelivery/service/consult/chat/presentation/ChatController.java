package com.ohdelivery.service.consult.chat.presentation;

import com.ohdelivery.service.consult.chat.domain.ChatMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ChatController {

    @MessageMapping("/chat/send")
    public void sendMessage(@Payload ChatMessage message) {
        log.info("Sending message: {}", message);
    }
}