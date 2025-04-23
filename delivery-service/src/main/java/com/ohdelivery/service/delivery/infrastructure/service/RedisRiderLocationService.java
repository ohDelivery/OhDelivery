package com.ohdelivery.service.delivery.infrastructure.service;

import com.ohdelivery.service.delivery.domain.dto.RiderLocation;
import com.ohdelivery.service.delivery.domain.service.RiderLocationService;
import com.ohdelivery.service.delivery.infrastructure.exception.RiderLocationNotFoundException;
import com.ohdelivery.service.delivery.infrastructure.redis.RedisKey;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisRiderLocationService implements RiderLocationService {

    private final RedisTemplate<String, String> riderLocationTemplate;

    @Override
    public void saveRiderLocation(String riderId, Double longitude, Double latitude,
        Long timestamp) {
        riderLocationTemplate.opsForGeo()
            .add(RedisKey.RIDER_LOCATION_KEY, new Point(longitude, latitude), riderId);
        setRiderLocationTtl(riderId);
    }

    @Override
    public RiderLocation getRiderLocation(String riderId) {
        List<Point> points = riderLocationTemplate.opsForGeo()
            .position(RedisKey.RIDER_LOCATION_KEY, riderId);
        if (points == null || points.isEmpty()) {
            throw new RiderLocationNotFoundException();
        }
        Point point = points.get(0);

        if (point == null) {
            throw new RiderLocationNotFoundException();
        }

        return new RiderLocation(point.getX(), point.getY());
    }

    @Override
    public void removeRiderLocation(String riderId) {
        riderLocationTemplate.opsForGeo().remove(RedisKey.RIDER_LOCATION_KEY, riderId);
    }

    private void setRiderLocationTtl(String riderId) {
        String key = RedisKey.RIDER_LOCATION_TTL_KEY + ":" + riderId;

        riderLocationTemplate.opsForSet().add(key, riderId);
        riderLocationTemplate.expire(key, 10, TimeUnit.SECONDS);
    }
}
