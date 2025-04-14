package com.ohdelivery.common.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateSlackIdEvent {

  private int userId;
  private String slack_id;
}
