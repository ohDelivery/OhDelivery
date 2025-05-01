package com.ohdelivery.service.match.rider.domain.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.connection.RedisGeoCommands.GeoLocation;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.domain.geo.GeoReference;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RedisRiderLocRepository {

  private final StringRedisTemplate redisTemplate;

  private static final String RIDER_LOC_PREFIX = "rider:location:nearby:";

  public void updateRiderLoc(Long riderId, Double lon, Double lat) {
    redisTemplate.opsForGeo().add(RIDER_LOC_PREFIX, new Point(lon, lat), riderId.toString());
  }

  public void deleteRiderLoc(Long riderId) {
    String key = RIDER_LOC_PREFIX + riderId;
    redisTemplate.opsForGeo().remove(key, riderId.toString());
  }

  public GeoResults<GeoLocation<String>> searchByLoc(Double lon, Double lat) {
    return redisTemplate.opsForGeo().search(
        RIDER_LOC_PREFIX,
        GeoReference.fromCoordinate(lon, lat),
        new Distance(10.0, RedisGeoCommands.DistanceUnit.KILOMETERS)
    );
  }
}
