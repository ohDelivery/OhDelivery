package com.ohdelivery.service.match.matching.infrastructure.messaging;

import com.ohdelivery.service.match.matching.application.MatchingEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MatchingEventProducer implements MatchingEventPublisher {
  private static final String MATCHING_EVENT_TOPIC = "matching-events";
  private final KafkaTemplate<String, Object> kafkaTemplate;

  @Override
  public void matchingCompletedEvent(String key, Object event) {
    kafkaTemplate.send(MATCHING_EVENT_TOPIC,key,event);
  }
}
