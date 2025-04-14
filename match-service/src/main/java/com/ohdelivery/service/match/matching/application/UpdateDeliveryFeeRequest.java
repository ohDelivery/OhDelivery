package com.ohdelivery.service.match.matching.application;

import java.util.UUID;
import lombok.Getter;

@Getter
public class UpdateDeliveryFeeRequest {

  private UUID deliveryId;
  private Integer fee;

}
