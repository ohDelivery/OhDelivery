package com.ohdelivery.service.consult.chat.application;

import com.ohdelivery.service.consult.chat.domain.KafkaConstants;
import com.ohdelivery.service.consult.chat.domain.model.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Messenger {

  private final KafkaTemplate<String, ChatMessage> kafkaTemplate;
  private final SimpMessageSendingOperations template;

  public void sendMessage(String topic, ChatMessage message) {
    kafkaTemplate.send(topic, message);
  }

  @KafkaListener(topics = KafkaConstants.TOPIC_CHAT, containerFactory = "kafkaListenerContainerFactory")
  public void receiveMessage(ChatMessage message) {
    template.convertAndSend("/sub/chat/" + message.getChatId(), message);
  }

}
