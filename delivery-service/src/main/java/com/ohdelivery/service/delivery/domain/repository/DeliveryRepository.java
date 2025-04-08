package com.ohdelivery.service.delivery.domain.repository;

import com.ohdelivery.service.delivery.domain.model.Delivery;

public interface DeliveryRepository {

    Delivery save(Delivery delivery);
}
