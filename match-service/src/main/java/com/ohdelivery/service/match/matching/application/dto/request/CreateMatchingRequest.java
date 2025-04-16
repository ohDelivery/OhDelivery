package com.ohdelivery.service.match.matching.application.dto.request;

import com.ohdelivery.common.kafka.dto.CreateDeliveryEvent;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateMatchingRequest {

  @NotNull
  private UUID deliveryId;
  private String storeName;
  private String storeAddress;
  private Double storeLongitude;
  private Double storeLatitude;
  private String orderDetails;
  private String orderRequest;
  private String targetAddress;
  private Integer expectedTime;
  private Integer shortedDistance;
  private Integer fee;

  public static CreateMatchingRequest from(CreateDeliveryEvent event) {
    return CreateMatchingRequest.builder()
        .deliveryId(event.getDeliveryId())
        .storeName(event.getStoreName())
        .storeAddress(event.getStoreAddress())
        .storeLongitude(event.getStoreLongitude())
        .storeLatitude(event.getStoreLatitude())
        .orderDetails(event.getOrderDetails())
        .orderRequest(event.getOrderRequest())
        .targetAddress(event.getTargetAddress())
        .expectedTime(event.getExpectedTime())
        .shortedDistance(event.getShortedDistance())
        .fee(event.getFee())
        .build();
  }
}
