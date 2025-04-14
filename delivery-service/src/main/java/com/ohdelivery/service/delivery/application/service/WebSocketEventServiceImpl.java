package com.ohdelivery.service.delivery.application.service;

import com.ohdelivery.service.delivery.application.dto.request.RiderLocationRequest;
import com.ohdelivery.service.delivery.application.dto.response.RiderLocationResponse;
import com.ohdelivery.service.delivery.domain.dto.RiderLocation;
import com.ohdelivery.service.delivery.domain.service.RiderLocationService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebSocketEventServiceImpl implements WebSocketEventService {

    private final RiderLocationService riderLocationService;

    @Override
    public void saveRiderLocation(RiderLocationRequest riderLocationRequest) {
        riderLocationService.saveRiderLocation(
            riderLocationRequest.getRiderId(), riderLocationRequest.getLongitude(),
            riderLocationRequest.getLatitude(), riderLocationRequest.getTimestamp());
    }

    @Override
    public RiderLocationResponse getRiderLocation(UUID riderId) {
        RiderLocation riderLocation = riderLocationService.getRiderLocation(riderId);

        return new RiderLocationResponse(riderLocation.getLongitude(), riderLocation.getLatitude());
    }
}
