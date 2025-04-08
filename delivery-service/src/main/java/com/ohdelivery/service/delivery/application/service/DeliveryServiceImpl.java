package com.ohdelivery.service.delivery.application.service;

import com.ohdelivery.service.delivery.application.dto.request.CreateDeliveryRequest;
import com.ohdelivery.service.delivery.application.exception.DeliveryNotFoundException;
import com.ohdelivery.service.delivery.domain.model.Delivery;
import com.ohdelivery.service.delivery.domain.model.DeliveryRecord;
import com.ohdelivery.service.delivery.domain.repository.DeliveryRecordRepository;
import com.ohdelivery.service.delivery.domain.repository.DeliveryRepository;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryRecordRepository deliveryRecordRepository;

    @Override
    @Transactional
    public Delivery createDelivery(CreateDeliveryRequest request) {
        Delivery delivery = request.toDelivery(0, 0);
        return deliveryRepository.save(delivery);
    }

    @Override
    @Transactional(readOnly = true)
    public Delivery getDelivery(UUID deliveryId) {
        return deliveryRepository.findById(deliveryId)
            .orElseThrow(DeliveryNotFoundException::new);
    }

    @Override
    @Transactional
    public void completeDelivery(UUID deliveryId) {
        Delivery delivery = getDelivery(deliveryId);
        delivery.complete();

        DeliveryRecord deliveryRecord = getDeliveryRecord(deliveryId);
        deliveryRecord.complete();
    }

    @Override
    @Transactional(readOnly = true)
    public DeliveryRecord getDeliveryRecord(UUID deliveryId) {
        return deliveryRecordRepository.findByDeliveryId(deliveryId)
            .orElseThrow(DeliveryNotFoundException::new);
    }

    @Override
    public DeliveryRecord createDeliveryRecord(UUID deliveryId, UUID riderId, Integer fee,
        LocalDateTime acceptedAt) {
        DeliveryRecord deliveryRecord = new DeliveryRecord(deliveryId, riderId, fee, acceptedAt);

        return deliveryRecordRepository.save(deliveryRecord);
    }
}
