package com.ohdelivery.service.match.alarm.application.dto.request;

import com.ohdelivery.common.kafka.dto.CreateMatchingEvent;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AlarmRequest {

  List<String> slackIdList;
  List<Long> riderIdList;
  UUID matchingId;
  String message;

  public static AlarmRequest from(CreateMatchingEvent event, String message) {
    return AlarmRequest.builder()
        .slackIdList(event.getSlackIdList())
        .riderIdList(event.getRiderIdList())
        .matchingId(event.getMatchingId())
        .message(message)
        .build();
  }
}
