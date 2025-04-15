package com.ohdelivery.service.match.rider.domain.repository;

import com.ohdelivery.service.match.rider.domain.model.Rider;
import com.ohdelivery.service.match.rider.domain.model.RiderStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RiderRepository {

  Rider save(Rider rider);

  Optional<Rider> findById(UUID id);

  List<Rider> findAll();

  List<Rider> findAllByStatus(RiderStatus status);

  Optional<Rider> findByRiderId(int riderId);
}
