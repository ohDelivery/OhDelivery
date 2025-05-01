package com.ohdelivery.service.match.rider.infrastructure.persistence;

import com.ohdelivery.service.match.rider.domain.model.Rider;
import com.ohdelivery.service.match.rider.domain.model.RiderStatus;
import com.ohdelivery.service.match.rider.domain.repository.RiderRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RiderRepositoryImpl implements RiderRepository {

  private final JpaRiderRepository jpaRiderRepository;

  @Override
  public Rider save(Rider rider) {
    return jpaRiderRepository.save(rider);
  }

  @Override
  public Optional<Rider> findById(UUID id) {
    return jpaRiderRepository.findByIdAndDeletedAtIsNull(id);
  }

  @Override
  public List<Rider> findAll() {
    return jpaRiderRepository.findAllByDeletedAtIsNull();
  }

  @Override
  public List<Rider> findAllByStatus(RiderStatus status) {
    return jpaRiderRepository.findAllByDeletedAtIsNullAndStatusIs(status);
  }

  @Override
  public Optional<Rider> findByRiderId(long riderId) {
    return jpaRiderRepository.findByRiderIdAndDeletedAtIsNull(riderId);
  }

  @Override
  public List<Rider> findAllByRiderIdIn(List<Long> riderIds) {
    return jpaRiderRepository.findAllByRiderIdInAndDeletedAtIsNullAndStatusIs(riderIds,
        RiderStatus.AVAILABLE);
  }
}
