package com.ohdelivery.service.match.rider.application.service;

import com.ohdelivery.service.match.rider.application.dto.request.CreateRiderRequest;
import java.util.UUID;

public interface RiderService {
  UUID createRider(CreateRiderRequest request);
}
