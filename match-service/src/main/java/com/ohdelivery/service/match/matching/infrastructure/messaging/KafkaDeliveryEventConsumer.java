package com.ohdelivery.service.match.matching.infrastructure.messaging;

import static com.ohdelivery.common.kafka.Topic.CREATE_DELIVERY;
import static com.ohdelivery.service.match.rider.infrastructure.messaging.RiderKafkaConfig.MATCH_GROUP_ID;

import com.ohdelivery.common.kafka.dto.CreateDeliveryEvent;
import com.ohdelivery.service.match.matching.application.dto.request.CreateMatchingRequest;
import com.ohdelivery.service.match.matching.application.service.MatchingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaDeliveryEventConsumer {

  private final MatchingService matchingService;

  @KafkaListener(
      topics = CREATE_DELIVERY,
      groupId = MATCH_GROUP_ID,
      containerFactory = "createDeliveryKafkaListenerFactory"
  )
  public void consumeCreateDelivery(CreateDeliveryEvent event) {
    log.info("Received UpdateSlackIdEvent: userId={}, slackId={}", event);
    CreateMatchingRequest request = CreateMatchingRequest.from(event);
    matchingService.createMatching(request);
  }
}
