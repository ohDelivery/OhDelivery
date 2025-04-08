package com.ohdelivery.service.match.rider.application.service;

import com.ohdelivery.service.match.rider.application.dto.request.CreateRiderRequest;
import com.ohdelivery.service.match.rider.application.dto.request.UpdateRiderRequest;
import com.ohdelivery.service.match.rider.application.dto.response.GetRiderResponse;
import java.util.UUID;

public interface RiderService {
  UUID createRider(CreateRiderRequest request);

  GetRiderResponse getRider(UUID id);

  void updateRider(UUID id, UpdateRiderRequest request);
}
