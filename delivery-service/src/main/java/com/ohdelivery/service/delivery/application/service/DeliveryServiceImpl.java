package com.ohdelivery.service.delivery.application.service;

import com.ohdelivery.service.delivery.application.dto.request.CreateDeliveryRequest;
import com.ohdelivery.service.delivery.application.exception.DeliveryNotFoundException;
import com.ohdelivery.service.delivery.domain.model.Delivery;
import com.ohdelivery.service.delivery.domain.model.DeliveryRecord;
import com.ohdelivery.service.delivery.domain.repository.DeliveryRecordRepository;
import com.ohdelivery.service.delivery.domain.repository.DeliveryRepository;
import com.ohdelivery.service.delivery.domain.service.ShortedPathService;
import com.ohdelivery.service.delivery.infrastructure.dto.LocationInfo;
import com.ohdelivery.service.delivery.infrastructure.dto.PathInfo;
import com.ohdelivery.service.delivery.infrastructure.messaging.DeliveryEventProducer;
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
    private final DeliveryEventProducer deliveryEventProducer;

    @Override
    @Transactional
    public Delivery createDelivery(CreateDeliveryRequest request) {
        LocationInfo storeLocation = shortedPathService.getLocation(request.getStoreAddress());
        LocationInfo targetLocation = shortedPathService.getLocation(request.getTargetAddress());

        Double storeX = storeLocation.getLongitude();
        Double storeY = storeLocation.getLatitude();
        log.info("Store location: longitude = {}, latitude = {}", storeX, storeY);

        Double targetX = targetLocation.getLongitude();
        Double targetY = targetLocation.getLatitude();
        log.info("Target location: longitude = {}, latitude = {}", targetX, targetY);

        PathInfo path = shortedPathService.getPath(storeLocation, targetLocation);
        log.info("Path: {}", path.getPath());

        Delivery saveDelivery = deliveryRepository.save(
            request.toDelivery(path.getDistance(), path.getDistance()));

        deliveryEventProducer.publishCreateDeliveryEvent(saveDelivery);

        return saveDelivery;
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

        // TODO 배달 완료 이벤트 던지기
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
