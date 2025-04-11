package com.ohdelivery.service.match.matching.application;

import java.util.UUID;

public interface MatchingEventPublisher {

  void matchingCompletedEvent(UUID deliveryId, UUID riderId);
}
