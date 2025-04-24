package com.ohdelivery.service.delivery.domain.service;

import com.ohdelivery.service.delivery.domain.dto.RiderLocation;

public interface RiderLocationService {

    void saveRiderLocation(String riderId, Double longitude, Double latitude, Long timestamp);

    RiderLocation getRiderLocation(String riderId);

    void removeRiderLocation(String riderId);
}
