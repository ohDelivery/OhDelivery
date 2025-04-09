package com.ohdelivery.service.delivery.application.dto.response;

import com.ohdelivery.service.delivery.domain.model.DeliveryRecord;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;

@Getter
public class DeliveryRecordResponse {

    private final UUID deliveryId;
    private final UUID riderId;
    private final Integer fee;
    private final LocalDateTime acceptedAt;
    private final LocalDateTime departedAt;
    private final LocalDateTime deliveredAt;

    public DeliveryRecordResponse(DeliveryRecord deliveryRecord) {
        this.deliveryId = deliveryRecord.getDeliveryId();
        this.riderId = deliveryRecord.getRiderId();
        this.fee = deliveryRecord.getFee();
        this.acceptedAt = deliveryRecord.getAcceptedAt();
        this.departedAt = deliveryRecord.getDepartedAt();
        this.deliveredAt = deliveryRecord.getDeliveredAt();
    }
}
