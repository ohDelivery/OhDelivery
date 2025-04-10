package com.ohdelivery.service.delivery.infrastructure.messaging;

import com.ohdelivery.common.kafka.Topic;
import com.ohdelivery.common.kafka.dto.CompleteDeliveryEvent;
import com.ohdelivery.common.kafka.dto.CreateDeliveryEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliveryEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishCreateDeliveryEvent(CreateDeliveryEvent event) {
        kafkaTemplate.send(Topic.CREATE_DELIVERY, event);
    }

    public void publishCompleteDeliveryEvent(CompleteDeliveryEvent event) {
        kafkaTemplate.send(Topic.COMPLETE_DELIVERY, event);
    }
}
