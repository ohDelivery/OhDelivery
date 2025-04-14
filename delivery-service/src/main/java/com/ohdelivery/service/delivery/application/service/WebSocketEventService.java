package com.ohdelivery.service.delivery.application.service;

import com.ohdelivery.service.delivery.application.dto.request.RiderLocationRequest;
import com.ohdelivery.service.delivery.application.dto.response.RiderLocationResponse;
import java.util.UUID;

public interface WebSocketEventService {

    void saveRiderLocation(RiderLocationRequest riderLocationRequest);

    RiderLocationResponse getRiderLocation(UUID riderId);
}
