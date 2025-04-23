package com.ohdelivery.service.delivery.application.service;

import com.ohdelivery.service.delivery.application.dto.request.RiderLocationRequest;
import com.ohdelivery.service.delivery.application.dto.response.RiderLocationResponse;
import com.ohdelivery.service.delivery.domain.dto.RiderLocation;
import com.ohdelivery.service.delivery.domain.service.RiderLocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebSocketEventService {

    private final RiderLocationService riderLocationService;

    public void saveRiderLocation(RiderLocationRequest riderLocationRequest) {
        riderLocationService.saveRiderLocation(
            riderLocationRequest.getRiderId(), riderLocationRequest.getLongitude(),
            riderLocationRequest.getLatitude(), riderLocationRequest.getTimestamp());
    }

    public RiderLocationResponse getRiderLocation(String riderId) {
        RiderLocation riderLocation = riderLocationService.getRiderLocation(riderId);

        return new RiderLocationResponse(riderLocation.getLongitude(), riderLocation.getLatitude());
    }
}
