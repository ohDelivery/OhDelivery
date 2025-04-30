package com.ohdelivery.service.consult.chat.application;

import com.ohdelivery.service.consult.chat.domain.model.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Messenger {

  private final KafkaTemplate<String, ChatMessage> kafkaTemplate;

  public void sendMessage(String topic, ChatMessage message) {
    kafkaTemplate.send(topic, message);
  }

}
