package com.ohdelivery.service.delivery.domain.service;

import java.util.UUID;

public interface LocationService {

    void saveRiderLocation(UUID riderId, Double longitude, Double latitude, Long timestamp);
}
