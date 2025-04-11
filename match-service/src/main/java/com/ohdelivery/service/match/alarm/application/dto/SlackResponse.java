package com.ohdelivery.service.match.alarm.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SlackResponse {

  private String slackId;
  private String channelId;
  private String sentAt;

  public static SlackResponse toResponse(String slackId, String channelId, String sentAt) {
    return SlackResponse.builder()
        .slackId(slackId)
        .channelId(channelId)
        .sentAt(sentAt)
        .build();
  }
}
