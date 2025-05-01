package com.ohdelivery.service.consult.chat.presentation.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateChatRequest {
  String agentId;
  String riderId;
}
