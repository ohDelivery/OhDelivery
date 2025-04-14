package com.ohdelivery.service.match.matching.domain.repository;

import com.ohdelivery.service.match.matching.domain.Matching;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchingRepository {

  Matching save(Matching matching);

  Optional<Matching> findById(UUID id);

  Optional<Matching> findByDeliveryId(UUID deliveryId);

  List<Matching> findByRiderId(UUID riderId);
}
