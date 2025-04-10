package com.ohdelivery.service.match.rider.application.dto.response;

import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GetRiderResponse {
  private final UUID id;
  private final Integer rider_id;
  private final String slack_id;
  private final String status;
  private final double latitude;
  private final double longitude;

  public static GetRiderResponse from(Rider rider){
    return new GetRiderResponse(
        rider.getId(),
        rider.getRiderId(),
        rider.getSlackId(),
        rider.getStatus().toString(),
        rider.getLatitude(),
        rider.getLongitude()
    );
  }
}
