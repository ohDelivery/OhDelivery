package com.ohdelivery.service.delivery.infrastructure.redis;

import com.ohdelivery.service.delivery.infrastructure.service.RedisRiderLocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisGeoEventHandler {

    private final RedisRiderLocationService redisRiderLocationService;

    @EventListener
    public void GeoKeyExpiredEvent(RedisGeoExpiredEvent geoKeyExpiredEvent) {
        redisRiderLocationService.removeRiderLocation(geoKeyExpiredEvent.getRiderId());
    }
}
