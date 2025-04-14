package com.ohdelivery.service.match.matching.infrastructure.messaging;

import static com.ohdelivery.common.kafka.Topic.COMPLETE_MATCHING;

import com.ohdelivery.common.kafka.dto.CompleteMatchingEvent;
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
  public void matchingCreatedEvent(List<UUID> slackIdList, UUID id, Integer fee, String storeName,
      String targetAddress, String orderRequest) {

  }
}
