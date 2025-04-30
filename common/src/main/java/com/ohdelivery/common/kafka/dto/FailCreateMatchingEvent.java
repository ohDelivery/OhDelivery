package com.ohdelivery.common.kafka.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FailCreateMatchingEvent {

    private UUID deliveryId;
}
