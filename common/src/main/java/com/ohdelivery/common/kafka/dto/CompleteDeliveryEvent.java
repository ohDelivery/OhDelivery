package com.ohdelivery.common.kafka.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CompleteDeliveryEvent {

    private UUID deliveryId;
    private String storeAddress;
    private String targetAddress;
    private Integer expectedTime;
    private Integer shortedDistance;
    private Integer fee;
    private String paymentType;
    private Integer paymentAmount;
    private LocalDateTime acceptedAt;
    private LocalDateTime departedAt;
    private LocalDateTime deliveredAt;
}
