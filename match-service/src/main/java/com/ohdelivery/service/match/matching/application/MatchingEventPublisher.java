package com.ohdelivery.service.match.matching.application;

import java.util.List;
import java.util.UUID;

public interface MatchingEventPublisher {

  void matchingCompletedEvent(UUID deliveryId, UUID riderId);

  void matchingCreatedEvent(List<UUID> slackIdList, UUID matchingId, Integer fee, String storeName,
      String targetAddress, String orderRequest);
}
