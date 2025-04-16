package com.ohdelivery.common.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateUserEvent {
    private Long userId;
    private String slackId;
}
