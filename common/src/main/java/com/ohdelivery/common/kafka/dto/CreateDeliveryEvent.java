package com.ohdelivery.common.kafka.dto;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateDeliveryEvent {

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
  private String paymentType;
  private Integer paymentAmount;
}
