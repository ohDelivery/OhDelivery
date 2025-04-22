package com.ohdelivery.common.feign.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ClientGetNearRidersRequest {

  private final Double sLat;
  private final Double sLon;

  public static ClientGetNearRidersRequest of(Double storeLatitude, Double storeLongitude) {
    return new ClientGetNearRidersRequest(storeLatitude, storeLongitude);
  }
}
