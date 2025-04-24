package com.ohdelivery.service.delivery.application.service;

import com.ohdelivery.service.delivery.application.dto.request.RiderLocationRequest;
import com.ohdelivery.service.delivery.application.dto.response.RiderLocationResponse;
import com.ohdelivery.service.delivery.application.observer.BroadcasterManager;
import com.ohdelivery.service.delivery.application.observer.LocationBroadcaster;
import com.ohdelivery.service.delivery.domain.dto.RiderLocation;
import com.ohdelivery.service.delivery.domain.service.RiderLocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final RiderLocationService riderLocationService;
    private final BroadcasterManager broadcasterManager;

    public void saveRiderLocation(RiderLocationRequest riderLocationRequest) {
        riderLocationService.saveRiderLocation(
            riderLocationRequest.getRiderId(), riderLocationRequest.getLongitude(),
            riderLocationRequest.getLatitude(), riderLocationRequest.getTimestamp());
    }

    public RiderLocationResponse getRiderLocation(String riderId) {
        RiderLocation riderLocation = riderLocationService.getRiderLocation(riderId);

        return new RiderLocationResponse(riderLocation.getLongitude(), riderLocation.getLatitude());
    }

    public void sendRiderLocation(String riderId) {
        RiderLocationResponse riderLocationResponse = getRiderLocation(riderId);

        LocationBroadcaster locationBroadcaster = broadcasterManager.getBroadcaster(riderId);

        locationBroadcaster.notifyObservers(riderLocationResponse.toMessage());
    }
}
