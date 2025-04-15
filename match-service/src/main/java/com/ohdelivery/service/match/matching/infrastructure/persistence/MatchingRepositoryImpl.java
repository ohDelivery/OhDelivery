package com.ohdelivery.service.match.matching.infrastructure.persistence;

import com.ohdelivery.service.match.matching.domain.Matching;
import com.ohdelivery.service.match.matching.domain.repository.MatchingRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MatchingRepositoryImpl implements MatchingRepository {

  private final JpaMatchingRepository jpaMatchingRepository;

  @Override
  public Matching save(Matching matching) {
    return jpaMatchingRepository.save(matching);
  }

  @Override
  public Optional<Matching> findById(UUID id) {
    return jpaMatchingRepository.findByIdAndDeletedAtIsNull(id);
  }

  @Override
  public Optional<Matching> findByDeliveryId(UUID deliveryId) {
    return jpaMatchingRepository.findByDeliveryIdAndDeletedAtIsNull(deliveryId);
  }

  @Override
  public List<Matching> findByRiderId(UUID riderId) {
    return jpaMatchingRepository.findAllByRiderIdAndDeletedAtIsNull(riderId);
  }

}
