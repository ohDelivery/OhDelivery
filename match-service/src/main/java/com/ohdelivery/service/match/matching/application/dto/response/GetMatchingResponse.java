package com.ohdelivery.service.match.matching.application.dto.response;

import com.ohdelivery.common.feign.GetDeliveryResponse;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class GetMatchingResponse {

  private UUID id;
  private UUID rider_id;
  private UUID deliveryId;
  private GetDeliveryResponse delivery;
}
