package com.ohdelivery.service.delivery.application.service;

import com.ohdelivery.service.delivery.application.dto.RiderLocation;
import com.ohdelivery.service.delivery.domain.service.RiderLocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebSocketEventServiceImpl implements WebSocketEventService {

    private final RiderLocationService riderLocationService;

    @Override
    public void saveRiderLocation(RiderLocation riderLocation) {
        riderLocationService.saveRiderLocation(
            riderLocation.getRiderId(), riderLocation.getLongitude(),
            riderLocation.getLatitude(), riderLocation.getTimestamp());
    }
}
