package com.ohdelivery.service.match.rider.presentation;

import com.ohdelivery.service.match.rider.domain.model.RiderStatus;
import lombok.Getter;

@Getter
public class UpdateRiderStatusRequest {
  RiderStatus status;
}
