package com.ohdelivery.service.delivery.infrastructure.persistence;

import com.ohdelivery.service.delivery.domain.model.Delivery;
import com.ohdelivery.service.delivery.domain.repository.DeliveryRepository;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaDeliveryRepository extends DeliveryRepository, JpaRepository<Delivery, UUID> {

}
