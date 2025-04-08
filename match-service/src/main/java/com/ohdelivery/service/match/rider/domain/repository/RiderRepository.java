package com.ohdelivery.service.match.rider.domain.repository;

import com.ohdelivery.service.match.rider.domain.model.Rider;

public interface RiderRepository {
  Rider save(Rider rider);
}
