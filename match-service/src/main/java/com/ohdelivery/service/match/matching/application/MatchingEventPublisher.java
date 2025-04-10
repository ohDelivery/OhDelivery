package com.ohdelivery.service.match.matching.application;

public interface MatchingEventPublisher {
  void matchingCompletedEvent(String key, Object event);
}
