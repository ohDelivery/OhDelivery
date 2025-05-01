package com.ohdelivery.common.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor
@AllArgsConstructor
public class UpdateSlackIdEvent {

  private Long userId;
  private String slack_id;
}
