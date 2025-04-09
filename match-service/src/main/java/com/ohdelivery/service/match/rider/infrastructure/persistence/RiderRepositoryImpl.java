package com.ohdelivery.service.match.rider.infrastructure.persistence;

import com.ohdelivery.service.match.rider.domain.model.Rider;
import com.ohdelivery.service.match.rider.domain.repository.RiderRepository;
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
}
