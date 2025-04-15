package com.ohdelivery.service.delivery.application.dto.response;

import com.ohdelivery.service.delivery.domain.model.DeliveryRecord;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DeliveryRecordResponse {

    private final UUID deliveryId;
    private final UUID riderId;
    private final Integer fee;
    private final LocalDateTime acceptedAt;
    private final LocalDateTime departedAt;
    private final LocalDateTime deliveredAt;

    public static DeliveryRecordResponse from(DeliveryRecord deliveryRecord) {
        return DeliveryRecordResponse.builder()
            .deliveryId(deliveryRecord.getDeliveryId())
            .riderId(deliveryRecord.getRiderId())
            .fee(deliveryRecord.getFee())
            .acceptedAt(deliveryRecord.getAcceptedAt())
            .departedAt(deliveryRecord.getDepartedAt())
            .deliveredAt(deliveryRecord.getDeliveredAt())
            .build();
    }
}
