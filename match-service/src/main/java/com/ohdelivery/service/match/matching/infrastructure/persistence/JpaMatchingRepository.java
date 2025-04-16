package com.ohdelivery.service.match.matching.infrastructure.persistence;

import com.ohdelivery.service.match.matching.domain.Matching;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaMatchingRepository extends JpaRepository<Matching, UUID> {

  Optional<Matching> findByIdAndDeletedAtIsNull(UUID id);

  Optional<Matching> findByDeliveryIdAndDeletedAtIsNull(UUID deliveryId);

  Page<Matching> findAllByRiderIdAndDeletedAtIsNull(UUID riderId, Pageable pageable);
}
