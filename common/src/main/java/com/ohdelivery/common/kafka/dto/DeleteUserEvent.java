package com.ohdelivery.common.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeleteUserEvent {

  private Long userId;
}
