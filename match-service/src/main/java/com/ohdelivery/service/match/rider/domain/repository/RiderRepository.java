package com.ohdelivery.service.match.rider.domain.repository;

import com.ohdelivery.service.match.rider.domain.model.Rider;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RiderRepository {

  Rider save(Rider rider);

  Optional<Rider> findById(UUID id);

  List<Rider> findAll();

  Optional<Rider> findByRiderId(int riderId);
}
