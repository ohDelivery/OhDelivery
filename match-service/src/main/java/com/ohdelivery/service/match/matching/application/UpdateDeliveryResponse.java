package com.ohdelivery.service.match.matching.application;

import com.ohdelivery.service.match.matching.domain.Matching;
import com.ohdelivery.service.match.matching.domain.vo.DeliveryInfo;
import com.ohdelivery.service.match.matching.domain.vo.RiderInfo;
import java.util.UUID;
import lombok.Getter;

@Getter
public class UpdateDeliveryResponse {

  private UUID id;
  private RiderInfo riderInfo;
  private DeliveryInfo deliveryInfo;
  private MatchingStatus matchingStatus;

  public UpdateDeliveryResponse(Matching matching) {
    this.id = matching.getId();
    this.riderInfo = matching.getRiderInfo();
    this.deliveryInfo = matching.getDeliveryInfo();
    this.matchingStatus = matching.getMatchingStatus();
  }
}
