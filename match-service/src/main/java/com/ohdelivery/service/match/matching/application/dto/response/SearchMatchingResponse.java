package com.ohdelivery.service.match.matching.application.dto.response;

import com.ohdelivery.service.match.matching.domain.Matching;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SearchMatchingResponse {

  private UUID id;
  private UUID riderId;
  private UUID deliveryId;

  public static SearchMatchingResponse from(Matching matching) {
    return new SearchMatchingResponse(
        matching.getId(),
        matching.getRiderId(),
        matching.getDeliveryId()
    );
  }
}
