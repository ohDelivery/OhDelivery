package com.ohdelivery.service.match.rider.application.service;

import com.ohdelivery.service.match.rider.application.dto.request.CreateRiderRequest;
import com.ohdelivery.service.match.rider.application.dto.request.UpdateRiderRequest;
import com.ohdelivery.service.match.rider.application.dto.request.UpdateRiderStatusRequest;
import com.ohdelivery.service.match.rider.application.dto.response.GetRiderResponse;
import com.ohdelivery.service.match.rider.application.dto.response.UpdateRiderStstusResponse;
import java.util.List;
import java.util.UUID;

public interface RiderService {

  UUID createRider(CreateRiderRequest request);

  GetRiderResponse getRider(UUID id);

  void updateRider(UUID id, UpdateRiderRequest request);

  void deleteRider(UUID id);

  UpdateRiderStstusResponse updateRiderStatus(UUID id, UpdateRiderStatusRequest request);

  List<GetRiderResponse> getAllRider();

  void updateSlackId(long userId, String slackId);

  void deleteRiderByRiderId(long userId);

  boolean checkAssignAvailable(UUID riderId);

  List<String> getRidersByLocation(Double storeLongitude, Double storeLatitude);

  GetRiderResponse getRiderByuserId(long riderId);
}
