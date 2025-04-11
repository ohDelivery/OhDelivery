package com.ohdelivery.service.match.alarm.application.dto;

import java.util.UUID;
import lombok.Getter;

@Getter
public class AlarmEvent {

  private UUID deliveryId;
  private String storeAddress;
  private Double storeLatitude;
  private Double storeLongitude;
}
