package com.ohdelivery.service.match.matching.infrastructure.messaging;

import static com.ohdelivery.common.kafka.Topic.COMPLETE_MATCHING;
import static com.ohdelivery.common.kafka.Topic.CREATED_MATCHING;
import static com.ohdelivery.common.kafka.Topic.FAILED_MATCHING;

import com.ohdelivery.common.kafka.dto.CompleteMatchingEvent;
import com.ohdelivery.common.kafka.dto.CreateMatchingEvent;
import com.ohdelivery.common.kafka.dto.FailCreateMatchingEvent;
import com.ohdelivery.service.match.matching.application.MatchingEventPublisher;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MatchingEventProducer implements MatchingEventPublisher {

  private final KafkaTemplate<String, Object> kafkaTemplate;

  @Override
  public void matchingCompletedEvent(UUID deliveryId, UUID riderId) {
    CompleteMatchingEvent event = new CompleteMatchingEvent(deliveryId, riderId);
    log.info("matching completed event: {}", event);
    kafkaTemplate.send(COMPLETE_MATCHING, event);
  }

  @Override
  public void matchingCreatedEvent(
      List<String> slackIdList,
      UUID matchingId,
      Integer fee,
      String storeName,
      String storeAddress,
      String targetAddress,
      String orderRequest) {
    CreateMatchingEvent event = new CreateMatchingEvent(
        slackIdList,
        matchingId,
        fee,
        storeName,
        storeAddress,
        targetAddress,
        orderRequest);
    log.info("matching created event: {}", event);
    kafkaTemplate.send(CREATED_MATCHING, event);
  }

  @Override
  public void matchingCreateFailedEvent(UUID deliveryId) {
    FailCreateMatchingEvent event = new FailCreateMatchingEvent(deliveryId);
    log.info("matching created failed event: {}", event);
    kafkaTemplate.send(FAILED_MATCHING, event);
  }

}
