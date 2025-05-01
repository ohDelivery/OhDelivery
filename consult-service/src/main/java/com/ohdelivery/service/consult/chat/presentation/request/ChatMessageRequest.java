package com.ohdelivery.service.consult.chat.presentation.request;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessageRequest {
  UUID chatId;
  String sender;
  String message;
}
