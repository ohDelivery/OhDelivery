package com.ohdelivery.common.kafka.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CompleteMatchingEvent {

  private UUID deliveryId;
  private UUID riderId;
}
