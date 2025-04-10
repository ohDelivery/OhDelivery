package com.ohdelivery.service.delivery.infrastructure.messaging;

import com.ohdelivery.common.kafka.Topic;
import com.ohdelivery.service.delivery.domain.model.Delivery;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliveryEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishCreateDeliveryEvent(Delivery delivery) {
        kafkaTemplate.send(Topic.CREATE_DELIVERY, delivery.toCreateDeliveryEvent());
    }
}
