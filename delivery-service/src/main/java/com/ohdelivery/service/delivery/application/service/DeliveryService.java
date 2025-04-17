package com.ohdelivery.service.delivery.application.service;

import com.ohdelivery.service.delivery.application.dto.request.CreateDeliveryRequest;
import com.ohdelivery.service.delivery.application.dto.response.DeliveryRecordResponse;
import com.ohdelivery.service.delivery.application.dto.response.DeliveryResponse;
import java.util.UUID;

public interface DeliveryService {

    DeliveryResponse createDelivery(CreateDeliveryRequest request);

    DeliveryResponse getDelivery(UUID deliveryId);

    void completeDelivery(UUID deliveryId);

    DeliveryRecordResponse createDeliveryRecord(UUID deliveryId, UUID riderId, Integer fee);

    DeliveryRecordResponse getDeliveryRecord(UUID deliveryId);

    void updateFee(UUID deliveryId, Integer fee);

    void completeMatching(UUID deliveryId, UUID riderId);

    void cancelMatching(UUID deliveryId);
}
