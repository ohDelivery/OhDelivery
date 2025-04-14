package com.ohdelivery.service.delivery.infrastructure.service;

import com.ohdelivery.service.delivery.domain.dto.RiderLocation;
import com.ohdelivery.service.delivery.domain.service.RiderLocationService;
import com.ohdelivery.service.delivery.infrastructure.exception.RiderLocationNotFoundException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisRiderLocationService implements RiderLocationService {

    private final RedisTemplate<String, UUID> redisTemplate;

    private static final String RIDER_LOCATION_KEY = "rider:locations";

    @Override
    public void saveRiderLocation(UUID riderId, Double longitude, Double latitude, Long timestamp) {
        redisTemplate.opsForGeo()
            .add(RIDER_LOCATION_KEY, new Point(longitude, latitude), riderId);
    }

    @Override
    public RiderLocation getRiderLocation(UUID riderId) {
        List<Point> points = redisTemplate.opsForGeo().position(RIDER_LOCATION_KEY, riderId);
        if (points == null || points.isEmpty()) {
            throw new RiderLocationNotFoundException();
        }
        Point point = points.get(0);

        return new RiderLocation(point.getX(), point.getY());
    }
}
