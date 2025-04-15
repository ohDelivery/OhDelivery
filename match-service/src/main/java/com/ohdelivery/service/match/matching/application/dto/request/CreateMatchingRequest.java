package com.ohdelivery.service.match.matching.application.dto.request;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Getter;

@Getter
public class CreateMatchingRequest {

  @NotNull
  private UUID deliveryId;
  private String storeName;
  private String storeAddress;
  private String orderDetails;
  private String orderRequest;
  private String targetAddress;
  private Integer expectedTime;
  private Integer shortedDistance;
  private Integer fee;
}
