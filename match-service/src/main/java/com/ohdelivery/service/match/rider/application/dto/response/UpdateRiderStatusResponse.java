package com.ohdelivery.service.match.rider.application.dto.response;

import com.ohdelivery.service.match.rider.domain.model.Rider;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UpdateRiderStatusResponse {

  private final UUID id;
  private final long rider_id;
  private final String slack_id;
  private final String status;
  private final Double latitude;
  private final Double longitude;

  public static UpdateRiderStatusResponse from(Rider rider) {
    return new UpdateRiderStatusResponse(
        rider.getId(),
        rider.getRiderId(),
        rider.getSlackId(),
        rider.getStatus().toString(),
        rider.getLatitude(),
        rider.getLongitude()
    );
  }
}
