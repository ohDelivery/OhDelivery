package com.ohdelivery.common.kafka.dto;

import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateAlarmEvent {

  private UUID alarmId;
  private String message;
  private List<String> slackEmails;
  private List<Long> riderIds;

  public static CreateAlarmEvent from(UUID alarmId, String message, List<String> slackEmails,
      List<Long> riderIds) {
    return CreateAlarmEvent.builder()
        .alarmId(alarmId)
        .message(message)
        .slackEmails(slackEmails)
        .riderIds(riderIds)
        .build();
  }
}
