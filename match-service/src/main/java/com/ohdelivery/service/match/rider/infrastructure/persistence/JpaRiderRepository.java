package com.ohdelivery.service.match.rider.infrastructure.persistence;

import com.ohdelivery.service.match.rider.domain.model.Rider;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaRiderRepository extends JpaRepository<Rider, UUID> {

  Optional<Rider> findByIdAndDeletedAtIsNull(UUID id);

  List<Rider> findAllByDeletedAtIsNull();

  Optional<Rider> findByRiderIdAndDeletedAtIsNull(Integer riderId);
}
