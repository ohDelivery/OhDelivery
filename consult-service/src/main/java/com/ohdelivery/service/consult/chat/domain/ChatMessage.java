package com.ohdelivery.service.consult.chat.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessage {
  String sender;
  String chatId;
  String message;
}
