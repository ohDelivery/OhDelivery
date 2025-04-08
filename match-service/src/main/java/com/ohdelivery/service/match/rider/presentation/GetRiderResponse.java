package com.ohdelivery.service.match.rider.presentation;

import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GetRiderResponse {
  private final UUID id;
  private final Integer rider_id;
  private final String status;
  private final double latitude;
  private final double longitude;
}
