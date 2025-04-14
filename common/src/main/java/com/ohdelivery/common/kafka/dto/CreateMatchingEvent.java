package com.ohdelivery.common.kafka.dto;

import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateMatchingEvent {

  List<String> slackIdList;
  UUID matchingId;
  Integer fee;
  String storeName;
  String storeAddress;
  String targetAddress;
  String orderRequest;
}
