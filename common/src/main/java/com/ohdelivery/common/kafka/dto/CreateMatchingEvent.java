package com.ohdelivery.common.kafka.dto;

import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateMatchingEvent {

  List<String> slackIdList;
  List<Long> riderIdList;
  UUID matchingId;
  Integer fee;
  String storeName;
  String storeAddress;
  String targetAddress;
  String orderRequest;
}
