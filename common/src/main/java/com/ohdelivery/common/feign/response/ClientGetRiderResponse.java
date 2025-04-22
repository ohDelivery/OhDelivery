package com.ohdelivery.common.feign.response;

import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ClientGetRiderResponse {

  private final UUID id;
  private final long rider_id;
  private final String slack_id;
  private final String status;
  private final Double latitude;
  private final Double longitude;

  public boolean isAvailable() {
    return status != null && "AVAILABLE".equalsIgnoreCase(status);
  }
}
