package com.ohdelivery.service.match.rider.application.service;

import com.ohdelivery.service.match.rider.application.dto.request.CreateRiderRequest;
import com.ohdelivery.service.match.rider.presentation.GetRiderResponse;
import java.util.UUID;

public interface RiderService {
  UUID createRider(CreateRiderRequest request);

  GetRiderResponse getRider(UUID id);
}
