package com.ohdelivery.service.match.rider.application.service;

import com.ohdelivery.service.match.rider.application.dto.request.CreateRiderRequest;
import com.ohdelivery.service.match.rider.application.dto.request.UpdateRiderRequest;
import com.ohdelivery.service.match.rider.application.dto.request.UpdateRiderStatusRequest;
import com.ohdelivery.service.match.rider.application.dto.response.GetRiderResponse;
import com.ohdelivery.service.match.rider.application.dto.response.UpdateRiderStstusResponse;
import com.ohdelivery.service.match.rider.application.exception.AvailableRiderNotFoundException;
import com.ohdelivery.service.match.rider.application.exception.RiderInvalidStatusException;
import com.ohdelivery.service.match.rider.application.exception.RiderNotFoundException;
import com.ohdelivery.service.match.rider.domain.model.Rider;
import com.ohdelivery.service.match.rider.domain.model.RiderStatus;
import com.ohdelivery.service.match.rider.domain.repository.RiderRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RiderServiceImpl implements RiderService {

  private final RiderRepository riderRepository;
  private static final double EARTH_RADIUS = 6371.0;

  @Override
  @Transactional
  public UUID createRider(CreateRiderRequest request) {
    Rider rider = request.toRider();
    riderRepository.save(rider);
    return rider.getId();
  }

  @Override
  @Transactional
  public GetRiderResponse getRider(UUID id) {
    Rider rider = riderRepository.findById(id)
        .orElseThrow(() -> new RiderNotFoundException());
    return new GetRiderResponse(
        rider.getId(),
        rider.getRiderId(),
        rider.getSlackId(),
        rider.getStatus().toString(),
        rider.getLatitude(),
        rider.getLongitude()
    );
  }

  @Override
  @Transactional
  public void updateRider(UUID id, UpdateRiderRequest request) {
    validateRiderStatus(request.getStatus());
    Rider rider = riderRepository.findById(id)
        .orElseThrow(() -> new RiderNotFoundException());
    rider = new Rider(
        rider.getId(),
        request.getRider_id(),
        request.getSlack_id(),
        request.getStatus(),
        request.getLatitude(),
        request.getLongitude()
    );

    riderRepository.save(rider);
  }

  @Override
  @Transactional
  public void deleteRider(UUID id) {
    Rider rider = riderRepository.findById(id)
        .orElseThrow(() -> new RiderNotFoundException());
    LocalDateTime now = LocalDateTime.now();
    String createdBy = "system";
    rider.delete(now, createdBy);
    riderRepository.save(rider);
  }

  @Override
  @Transactional
  public UpdateRiderStstusResponse updateRiderStatus(UUID id, UpdateRiderStatusRequest request) {
    Rider rider = riderRepository.findById(id)
        .orElseThrow(() -> new RiderNotFoundException());
    rider.changeStatus(request.getStatus());
    return UpdateRiderStstusResponse.from(rider);
  }

  @Override
  @Transactional
  public List<GetRiderResponse> getAllRider() {
    return riderRepository.findAll()
        .stream()
        .map(GetRiderResponse::from)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public void updateSlackId(int riderId, String slackId) {
    Rider rider = riderRepository.findByRiderId(riderId)
        .orElseThrow(() -> new RiderNotFoundException());
    rider.updateSlackId(slackId);
  }

  @Override
  public void deleteRiderByRiderId(int userId) {
    Rider rider = riderRepository.findByRiderId(userId)
        .orElseThrow(() -> new RiderNotFoundException());
    LocalDateTime now = LocalDateTime.now();
    String createdBy = "system";
    rider.delete(now, createdBy);
  }

  @Override
  public boolean checkAssignAvailable(UUID id) {
    Rider rider = riderRepository.findById(id)
        .orElseThrow(() -> new RiderNotFoundException());
    if (rider.getStatus().equals(RiderStatus.AVAILABLE)) {
      return true;
    }
    return false;
  }

  @Override
  public List<String> getRidersByLocation(Double storeLongitude, Double storeLatitude) {
    List<Rider> availableRiders = getAvailableRiders();
    List<String> slackIds = getNearByRiders(availableRiders, storeLatitude, storeLongitude);
    return slackIds;
  }
  
  private void validateRiderStatus(RiderStatus status) {
    if (status == null) {
      throw new RiderInvalidStatusException("라이더 상태가 지정되지 않았습니다.");
    }
  }

  private List<Rider> getAvailableRiders() {
    List<Rider> riders = riderRepository.findAllByStatus(RiderStatus.AVAILABLE);
    if (riders.isEmpty()) {
      throw new AvailableRiderNotFoundException();
    }
    return riders;
  }

  private List<String> getNearByRiders(List<Rider> riders, double storeLat, double storeLng) {
    List<String> slackIds = riders.stream()
        .filter(rider ->
            calculateDistance(storeLat, storeLng, rider.getLatitude(), rider.getLongitude()) <= 10)
        .map(Rider::getSlackId)
        .toList();
    if (slackIds.isEmpty()) {
      throw new AvailableRiderNotFoundException();
    }
    return slackIds;
  }

  private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
    double dLat = Math.toRadians(lat2 - lat1);
    double dLon = Math.toRadians(lon2 - lon1);
    double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
        + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
        * Math.sin(dLon / 2) * Math.sin(dLon / 2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    return EARTH_RADIUS * c;
  }
}
