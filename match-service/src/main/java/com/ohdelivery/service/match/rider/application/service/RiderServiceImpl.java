package com.ohdelivery.service.match.rider.application.service;

import com.ohdelivery.service.match.rider.application.dto.request.CreateRiderRequest;
import com.ohdelivery.service.match.rider.application.dto.request.UpdateRiderRequest;
import com.ohdelivery.service.match.rider.application.dto.response.UpdateRiderStstusResponse;
import com.ohdelivery.service.match.rider.domain.model.Rider;
import com.ohdelivery.service.match.rider.domain.model.RiderStatus;
import com.ohdelivery.service.match.rider.domain.repository.RiderRepository;
import com.ohdelivery.service.match.rider.application.dto.response.GetRiderResponse;
import com.ohdelivery.service.match.rider.application.exception.RiderNotFoundException;
import com.ohdelivery.service.match.rider.application.exception.RiderInvalidStatusException;
import com.ohdelivery.service.match.rider.presentation.UpdateRiderStatusRequest;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RiderServiceImpl implements RiderService {

  private final RiderRepository riderRepository;

  @Override
  public UUID createRider(CreateRiderRequest request) {
    Rider rider = request.toRider();
    riderRepository.save(rider);
    return rider.getId();
  }

  @Override
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

  private void validateRiderStatus(RiderStatus status) {
    if (status == null) {
      throw new RiderInvalidStatusException("라이더 상태가 지정되지 않았습니다.");
    }
  }
}
