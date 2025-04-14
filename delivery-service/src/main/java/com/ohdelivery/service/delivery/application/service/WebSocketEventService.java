package com.ohdelivery.service.delivery.application.service;

import com.ohdelivery.service.delivery.application.dto.RiderLocation;

public interface WebSocketEventService {

    void saveRiderLocation(RiderLocation riderLocation);
}
