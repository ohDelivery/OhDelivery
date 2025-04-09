package com.ohdelivery.service.match.rider.application.dto.request;

import com.ohdelivery.service.match.rider.domain.model.RiderStatus;
import lombok.Getter;

@Getter
public class CreateRiderRequest {
  private Integer rider_id;
  private RiderStatus status;
  private double latitude;
  private double longitude;
}
