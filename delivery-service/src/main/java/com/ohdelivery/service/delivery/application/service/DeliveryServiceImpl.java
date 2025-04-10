package com.ohdelivery.service.delivery.application.service;

import com.ohdelivery.service.delivery.application.dto.request.CreateDeliveryRequest;
import com.ohdelivery.service.delivery.application.exception.DeliveryNotFoundException;
import com.ohdelivery.service.delivery.domain.model.Delivery;
import com.ohdelivery.service.delivery.domain.model.DeliveryRecord;
import com.ohdelivery.service.delivery.domain.repository.DeliveryRecordRepository;
import com.ohdelivery.service.delivery.domain.repository.DeliveryRepository;
import com.ohdelivery.service.delivery.domain.service.ShortedPathService;
import com.ohdelivery.service.delivery.infrastructure.dto.LocationInfo;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryRecordRepository deliveryRecordRepository;
    private final ShortedPathService shortedPathService;

    @Override
    @Transactional
    public Delivery createDelivery(CreateDeliveryRequest request) {
        LocationInfo locationInfo = shortedPathService.getLocation(request.getStoreAddress());

        Double storeX = locationInfo.getLongitude();
        Double storeY = locationInfo.getLatitude();
        log.info("Store location: longitude = {}, latitude = {}", storeX, storeY);

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
    @Transactional(readOnly = true)
    public DeliveryRecord createDeliveryRecord(UUID deliveryId, UUID riderId, Integer fee,
        LocalDateTime acceptedAt) {
        DeliveryRecord deliveryRecord = new DeliveryRecord(deliveryId, riderId, fee, acceptedAt);

        return deliveryRecordRepository.save(deliveryRecord);
    }

    @Override
    @Transactional
    public void updateFee(UUID deliveryId, Integer fee) {
        Delivery delivery = getDelivery(deliveryId);
        delivery.updateFee(fee);
    }
}
