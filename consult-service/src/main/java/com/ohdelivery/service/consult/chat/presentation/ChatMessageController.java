package com.ohdelivery.service.consult.chat.presentation;

import com.ohdelivery.service.consult.chat.application.ChatService;
import com.ohdelivery.service.consult.chat.presentation.request.ChatMessageRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping
@RequiredArgsConstructor
@RestController
public class ChatMessageController {

  private final ChatService chatService;

  @MessageMapping("/chat/send")
  public void sendMessage(ChatMessageRequest message) {
    log.info("Sending message: {}", message);
    chatService.sendMessage(message.getChatId(), message.getSender(), message.getMessage());
  }

}
