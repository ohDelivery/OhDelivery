package com.ohdelivery.service.delivery.infrastructure.service;

import com.ohdelivery.service.delivery.domain.service.LocationService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisService implements LocationService {

    private final RedisTemplate<String, UUID> redisTemplate;

    private static final String RIDER_LOCATION_KEY = "rider:locations";
    private static final String RIDER_LOCATION_TTL_KEY = "rider:locations:ttl";

    @Override
    public void saveRiderLocation(UUID riderId, Double longitude, Double latitude, Long timestamp) {
        redisTemplate.opsForGeo()
            .add(RIDER_LOCATION_KEY, new Point(longitude, latitude), riderId);
    }
}
