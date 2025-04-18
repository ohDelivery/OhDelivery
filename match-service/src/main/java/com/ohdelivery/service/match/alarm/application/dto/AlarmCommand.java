package com.ohdelivery.service.match.alarm.application.dto;

import com.ohdelivery.common.kafka.dto.CreateMatchingEvent;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AlarmCommand {

  List<String> slackIdList;
  List<Long> riderIdList;
  UUID matchingId;
  Integer fee;
  String storeName;
  String storeAddress;
  String targetAddress;
  String orderRequest;

  public static AlarmCommand from(CreateMatchingEvent event) {
    return AlarmCommand.builder()
        .slackIdList(event.getSlackIdList())
        .riderIdList(event.getRiderIdList())
        .matchingId(event.getMatchingId())
        .fee(event.getFee())
        .storeName(event.getStoreName())
        .storeAddress(event.getStoreAddress())
        .targetAddress(event.getTargetAddress())
        .orderRequest(event.getOrderRequest())
        .build();
  }
}
