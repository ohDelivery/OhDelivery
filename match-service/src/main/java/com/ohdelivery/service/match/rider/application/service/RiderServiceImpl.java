package com.ohdelivery.service.match.rider.application.service;

import com.ohdelivery.service.match.rider.application.dto.request.CreateRiderRequest;
import com.ohdelivery.service.match.rider.application.dto.request.UpdateRiderRequest;
import com.ohdelivery.service.match.rider.application.dto.request.UpdateRiderStatusRequest;
import com.ohdelivery.service.match.rider.application.dto.response.GetRiderResponse;
import com.ohdelivery.service.match.rider.application.dto.response.UpdateRiderStatusResponse;
import com.ohdelivery.service.match.rider.application.exception.AvailableRiderNotFoundException;
import com.ohdelivery.service.match.rider.application.exception.RiderInvalidStatusException;
import com.ohdelivery.service.match.rider.application.exception.RiderNotFoundException;
import com.ohdelivery.service.match.rider.domain.model.Rider;
import com.ohdelivery.service.match.rider.domain.model.RiderStatus;
import com.ohdelivery.service.match.rider.domain.repository.RedisRiderLocRepository;
import com.ohdelivery.service.match.rider.domain.repository.RiderRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RiderServiceImpl implements RiderService {

  private final RiderRepository riderRepository;
  private final RedisRiderLocRepository redisRiderLocRepository;
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

    updateRiderLocInRedis(request);
    riderRepository.save(rider);
  }

  private void updateRiderLocInRedis(UpdateRiderRequest request) {
    if (request.getStatus().equals(RiderStatus.AVAILABLE)) {
      redisRiderLocRepository.updateRiderLoc(
          request.getRider_id(), request.getLongitude(), request.getLatitude());

    } else if (request.getStatus().equals(RiderStatus.OFFLINE)) {
      redisRiderLocRepository.deleteRiderLoc(request.getRider_id());
    }
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
  public UpdateRiderStatusResponse updateRiderStatus(UUID id, UpdateRiderStatusRequest request) {
    Rider rider = riderRepository.findById(id)
        .orElseThrow(() -> new RiderNotFoundException());
    rider.changeStatus(request.getStatus());
    return UpdateRiderStatusResponse.from(rider);
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
  public void updateSlackId(long riderId, String slackId) {
    Rider rider = riderRepository.findByRiderId(riderId)
        .orElseThrow(() -> new RiderNotFoundException());
    rider.updateSlackId(slackId);
  }

  @Override
  public void deleteRiderByRiderId(long userId) {
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
  public List<GetRiderResponse> getRidersByLocation(Double sLat, Double sLon) {
    List<Rider> riders = getRidersWithRedis(sLat, sLon);
    log.info("Number of found riders : {}", riders.size());
    return riders.stream()
        .map(GetRiderResponse::from)
        .collect(Collectors.toList());
  }

  private List<Rider> getRidersWithDB(Double sLat, Double sLon) {
    List<Rider> riders = riderRepository.findAllByStatus(RiderStatus.AVAILABLE);
    riders = riders.stream()
        .filter(rider -> {
          double dis = calculateDistance(sLat, sLon, rider.getLatitude(), rider.getLongitude());
          return dis <= 10.0;
        })
        .toList();

    if (riders.isEmpty()) {
      throw new AvailableRiderNotFoundException();
    }
    return riders;
  }

  private List<Rider> getRidersWithRedis(Double sLat, Double sLon) {
    GeoResults<RedisGeoCommands.GeoLocation<String>> results = redisRiderLocRepository
        .searchByLoc(sLon, sLat);
    if (results == null) {
      throw new AvailableRiderNotFoundException();
    }

    List<Long> riderIds = results.getContent().stream()
        .map(geoLocation -> geoLocation.getContent().getName())
        .map(Long::valueOf)
        .toList();

    return riderRepository.findAllByRiderIdIn(riderIds);
  }

  @Override
  public GetRiderResponse getRiderByUserId(long riderId) {
    Rider rider = riderRepository.findByRiderId(riderId)
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

  private void validateRiderStatus(RiderStatus status) {
    if (status == null) {
      throw new RiderInvalidStatusException("라이더 상태가 지정되지 않았습니다.");
    }
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
