package com.ohdelivery.service.delivery.application.service;

import com.ohdelivery.common.kafka.dto.CompleteDeliveryEvent;
import com.ohdelivery.common.kafka.dto.UpdateDeliveryEvent;
import com.ohdelivery.service.delivery.application.dto.RiderLocation;
import com.ohdelivery.service.delivery.application.dto.request.CreateDeliveryRequest;
import com.ohdelivery.service.delivery.application.dto.response.DeliveryRecordResponse;
import com.ohdelivery.service.delivery.application.dto.response.DeliveryResponse;
import com.ohdelivery.service.delivery.application.exception.DeliveryNotFoundException;
import com.ohdelivery.service.delivery.domain.model.Delivery;
import com.ohdelivery.service.delivery.domain.model.DeliveryRecord;
import com.ohdelivery.service.delivery.domain.repository.DeliveryRecordRepository;
import com.ohdelivery.service.delivery.domain.repository.DeliveryRepository;
import com.ohdelivery.service.delivery.domain.service.LocationService;
import com.ohdelivery.service.delivery.domain.service.ShortedPathService;
import com.ohdelivery.service.delivery.infrastructure.dto.LocationInfo;
import com.ohdelivery.service.delivery.infrastructure.dto.PathInfo;
import com.ohdelivery.service.delivery.infrastructure.messaging.DeliveryEventProducer;
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
    private final LocationService locationService;

    @Override
    @Transactional
    public DeliveryResponse createDelivery(CreateDeliveryRequest request) {
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

        deliveryEventProducer.publishCreateDeliveryEvent(
            saveDelivery.toCreateDeliveryEvent(storeX, storeY));

        return DeliveryResponse.from(saveDelivery);
    }

    @Override
    @Transactional(readOnly = true)
    public DeliveryResponse getDelivery(UUID deliveryId) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
            .orElseThrow(DeliveryNotFoundException::new);
        return DeliveryResponse.from(delivery);
    }

    @Override
    @Transactional
    public void completeDelivery(UUID deliveryId) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
            .orElseThrow(DeliveryNotFoundException::new);
        delivery.complete();

        DeliveryRecord deliveryRecord = deliveryRecordRepository.findByDeliveryId(deliveryId)
            .orElseThrow(DeliveryNotFoundException::new);
        deliveryRecord.complete();

        CompleteDeliveryEvent completeDeliveryEvent = createCompleteDeliveryEvent(delivery,
            deliveryRecord);
        deliveryEventProducer.publishCompleteDeliveryEvent(completeDeliveryEvent);
    }

    @Override
    @Transactional(readOnly = true)
    public DeliveryRecordResponse getDeliveryRecord(UUID deliveryId) {
        DeliveryRecord deliveryRecord = deliveryRecordRepository.findByDeliveryId(deliveryId)
            .orElseThrow(DeliveryNotFoundException::new);
        return DeliveryRecordResponse.from(deliveryRecord);
    }

    @Override
    @Transactional(readOnly = true)
    public DeliveryRecordResponse createDeliveryRecord(UUID deliveryId, UUID riderId, Integer fee) {
        DeliveryRecord deliveryRecord = new DeliveryRecord(deliveryId, riderId, fee);

        return DeliveryRecordResponse.from(deliveryRecordRepository.save(deliveryRecord));
    }

    @Override
    @Transactional
    public void updateFee(UUID deliveryId, Integer fee) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
            .orElseThrow(DeliveryNotFoundException::new);
        delivery.updateFee(fee);

        deliveryEventProducer.publishUpdateDeliveryEvent(
            new UpdateDeliveryEvent(delivery.getId(), delivery.getFee()));
    }

    @Override
    @Transactional
    public void completeMatching(UUID deliveryId, UUID riderId) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
            .orElseThrow(DeliveryNotFoundException::new);
        delivery.updateWaitingForCooking();

        createDeliveryRecord(delivery.getId(), riderId, delivery.getFee());
    }

    @Override
    public void saveRiderLocation(RiderLocation riderLocation) {
        locationService.saveRiderLocation(
            riderLocation.getRiderId(), riderLocation.getLongitude(),
            riderLocation.getLatitude(), riderLocation.getTimestamp());
    }

    private CompleteDeliveryEvent createCompleteDeliveryEvent(Delivery delivery,
        DeliveryRecord deliveryRecord) {
        return CompleteDeliveryEvent.builder()
            .deliveryId(delivery.getId())
            .storeAddress(delivery.getOrderInfo().getStoreAddress())
            .targetAddress(delivery.getTargetAddress())
            .expectedTime(delivery.getPathInfo().getExpectedTime())
            .shortedDistance(delivery.getPathInfo().getShortedDistance())
            .fee(delivery.getFee())
            .paymentType(delivery.getPaymentType().toString())
            .paymentAmount(delivery.getPaymentAmount())
            .acceptedAt(deliveryRecord.getAcceptedAt())
            .departedAt(deliveryRecord.getDepartedAt())
            .deliveredAt(deliveryRecord.getDeliveredAt())
            .build();
    }

}
