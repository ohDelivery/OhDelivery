package com.ohdelivery.common.kafka.dto;

import java.util.UUID;
import lombok.Getter;

@Getter
public class CompleteMatchingEvent {

    private UUID deliveryId;
    private UUID riderId;
}
