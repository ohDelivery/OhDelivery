package com.ohdelivery.service.delivery.application.service;

import com.ohdelivery.service.delivery.application.dto.request.CreateDeliveryRequest;
import com.ohdelivery.service.delivery.domain.model.Delivery;
import com.ohdelivery.service.delivery.domain.model.DeliveryRecord;
import java.util.UUID;

public interface DeliveryService {

    Delivery createDelivery(CreateDeliveryRequest request);

    Delivery getDelivery(UUID deliveryId);

    DeliveryRecord getDeliveryRecord(UUID deliveryId);

    void completeDelivery(UUID deliveryId);
}
