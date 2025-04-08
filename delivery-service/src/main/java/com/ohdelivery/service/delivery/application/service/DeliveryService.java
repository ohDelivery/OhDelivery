package com.ohdelivery.service.delivery.application.service;

import com.ohdelivery.service.delivery.application.dto.request.CreateDeliveryRequest;
import com.ohdelivery.service.delivery.domain.model.Delivery;

public interface DeliveryService {

    Delivery createDelivery(CreateDeliveryRequest request);
}
