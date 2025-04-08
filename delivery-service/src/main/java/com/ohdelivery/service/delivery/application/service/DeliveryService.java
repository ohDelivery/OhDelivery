package com.ohdelivery.service.delivery.application.service;

import com.ohdelivery.service.delivery.application.dto.request.CreateDeliveryRequest;
import com.ohdelivery.service.delivery.domain.model.Delivery;
import com.ohdelivery.service.delivery.domain.model.DeliveryRecord;
import java.time.LocalDateTime;
import java.util.UUID;

public interface DeliveryService {

    Delivery createDelivery(CreateDeliveryRequest request);

    Delivery getDelivery(UUID deliveryId);

    void completeDelivery(UUID deliveryId);

    DeliveryRecord createDeliveryRecord(UUID deliveryId, UUID riderId, Integer fee,
        LocalDateTime acceptedAt);

    DeliveryRecord getDeliveryRecord(UUID deliveryId);
}
