package com.ohdelivery.common.kafka.dto;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FailAlarmEvent {

  private CreateAlarmEvent createAlarmEvent;
  private String errorMessage;
  private Instant failedAt;

  public static FailAlarmEvent from(CreateAlarmEvent createAlarmEvent, Exception e) {
    return FailAlarmEvent.builder()
        .createAlarmEvent(createAlarmEvent)
        .errorMessage(e.getMessage())
        .failedAt(Instant.now())
        .build();
  }
}
