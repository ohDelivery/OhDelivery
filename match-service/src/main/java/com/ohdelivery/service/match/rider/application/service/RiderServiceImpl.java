package com.ohdelivery.service.match.rider.application.service;

import com.ohdelivery.service.match.rider.application.dto.request.CreateRiderRequest;
import com.ohdelivery.service.match.rider.application.dto.request.UpdateRiderRequest;
import com.ohdelivery.service.match.rider.domain.model.Rider;
import com.ohdelivery.service.match.rider.domain.repository.RiderRepository;
import com.ohdelivery.service.match.rider.application.dto.response.GetRiderResponse;
import jakarta.persistence.EntityNotFoundException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RiderServiceImpl implements RiderService {

  private final RiderRepository riderRepository;

  @Override
  public UUID createRider(CreateRiderRequest request) {
    Rider rider = new Rider(
        request.getRider_id(),
        request.getStatus(),
        request.getLatitude(),
        request.getLongitude()
    );
    riderRepository.save(rider);
    return rider.getId();
  }

  @Override
  public GetRiderResponse getRider(UUID id) {
    Rider rider = riderRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Rider not found"));
    return new GetRiderResponse(
        rider.getId(),
        rider.getRiderId(),
        rider.getStatus().toString(),
        rider.getLatitude(),
        rider.getLongitude()
    );
  }

  @Override
  public void updateRider(UUID id, UpdateRiderRequest request) {
    Rider rider = riderRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Rider not found"));
    rider = new Rider(
        rider.getId(),
        request.getRider_id(),
        request.getStatus(),
        request.getLatitude(),
        request.getLongitude()
    );

    riderRepository.save(rider);
  }
}
