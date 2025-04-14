package com.ohdelivery.service.delivery.application.service;

import com.ohdelivery.service.delivery.application.dto.request.CreateDeliveryRequest;
import com.ohdelivery.service.delivery.domain.model.Delivery;
import com.ohdelivery.service.delivery.domain.model.DeliveryRecord;
import java.util.UUID;

public interface DeliveryService {

    Delivery createDelivery(CreateDeliveryRequest request);

    Delivery getDelivery(UUID deliveryId);

    void completeDelivery(UUID deliveryId);

    DeliveryRecord createDeliveryRecord(UUID deliveryId, UUID riderId, Integer fee);

    DeliveryRecord getDeliveryRecord(UUID deliveryId);

    void updateFee(UUID deliveryId, Integer fee);

    void completeMatching(UUID deliveryId, UUID riderId);
}
