package com.ohdelivery.common.kafka.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonDeserialize(builder = CreateDeliveryEvent.CreateDeliveryEventBuilder.class)
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

  // Builder 클래스를 명시적으로 생성
  @JsonPOJOBuilder(withPrefix = "")
  public static class CreateDeliveryEventBuilder {

  }

}
