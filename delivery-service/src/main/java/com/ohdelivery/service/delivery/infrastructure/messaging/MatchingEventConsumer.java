package com.ohdelivery.service.delivery.infrastructure.messaging;

import com.ohdelivery.common.kafka.Topic;
import com.ohdelivery.common.kafka.dto.CompleteMatchingEvent;
import com.ohdelivery.service.delivery.application.service.DeliveryService;
import com.ohdelivery.service.delivery.infrastructure.config.KafkaConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MatchingEventConsumer {

    private final DeliveryService deliveryService;

    @KafkaListener(
        topics = Topic.COMPLETE_MATCHING,
        groupId = KafkaConfig.DELIVERY_GROUP_ID,
        containerFactory = "completeMatchingConsumerFactory"
    )
    public void testDelivery(@Payload CompleteMatchingEvent event) {
        deliveryService.completeMatching(event.getDeliveryId(), event.getRiderId());
    }
}
