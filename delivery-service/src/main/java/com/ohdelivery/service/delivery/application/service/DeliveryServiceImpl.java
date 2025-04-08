package com.ohdelivery.service.delivery.application.service;

import com.ohdelivery.service.delivery.application.dto.request.CreateDeliveryRequest;
import com.ohdelivery.service.delivery.domain.model.Delivery;
import com.ohdelivery.service.delivery.domain.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;

    @Override
    public Delivery createDelivery(CreateDeliveryRequest request) {
        Delivery delivery = request.toDelivery(0, 0);
        return deliveryRepository.save(delivery);
    }
}
