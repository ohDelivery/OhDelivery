package com.ohdelivery.service.delivery.domain.repository;

import com.ohdelivery.service.delivery.domain.model.DeliveryRecord;
import java.util.Optional;
import java.util.UUID;

public interface DeliveryRecordRepository {

    DeliveryRecord save(DeliveryRecord deliveryRecord);

    Optional<DeliveryRecord> findByDeliveryId(UUID deliveryId);
}
