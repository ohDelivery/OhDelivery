package com.ohdelivery.service.delivery.domain.service;

import com.ohdelivery.service.delivery.domain.dto.RiderLocation;
import java.util.UUID;

public interface RiderLocationService {

    void saveRiderLocation(UUID riderId, Double longitude, Double latitude, Long timestamp);

    RiderLocation getRiderLocation(UUID riderId);
}
