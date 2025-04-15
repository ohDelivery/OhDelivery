package com.ohdelivery.service.match.matching.application;

import java.util.List;
import java.util.UUID;

public interface MatchingEventPublisher {

  void matchingCompletedEvent(UUID deliveryId, UUID riderId);

  void matchingCreatedEvent(
      List<String> slackIdList,
      UUID matchingId,
      Integer fee,
      String storeName,
      Double storeLongitude,
      Double storeLatitude,
      String storeAddress,
      String targetAddress,
      String orderRequest);
}
