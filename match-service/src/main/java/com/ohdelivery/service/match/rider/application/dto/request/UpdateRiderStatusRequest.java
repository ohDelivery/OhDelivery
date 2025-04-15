package com.ohdelivery.service.match.rider.application.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ohdelivery.service.match.rider.domain.model.RiderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdateRiderStatusRequest {

  @NotNull
  @JsonProperty("status")
  private RiderStatus status;
}
