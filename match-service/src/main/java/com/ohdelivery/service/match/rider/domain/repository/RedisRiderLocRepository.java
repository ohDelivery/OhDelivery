package com.ohdelivery.service.match.rider.domain.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ohdelivery.service.match.rider.domain.model.Rider;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands.DistanceUnit;
import org.springframework.data.redis.connection.RedisGeoCommands.GeoLocation;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.domain.geo.GeoReference;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RedisRiderLocRepository {

  private final StringRedisTemplate redisTemplate;

  private static final String RIDER_INFO_PREFIX = "rider:info:";  // Rider Info Key
  private static final String RIDER_LOC_PREFIX = "rider:location:nearby:";  // GeoHash Key
  private final ObjectMapper objectMapper;

  public void saveRiderInfo(Rider rider) {
    String key = RIDER_INFO_PREFIX + rider.getRiderId();
    String value;
    try {
      value = objectMapper.writeValueAsString(rider);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Failed to serialize rider info", e);
    }
    redisTemplate.opsForValue().set(key, value);
  }

  public List<Rider> getRidersInfo(List<String> riderIds) {
    List<String> keys = riderIds.stream()
        .map(id -> RIDER_INFO_PREFIX + id)
        .toList();

    List<String> riderInfos = redisTemplate.opsForValue().multiGet(keys);

    List<Rider> riders = new ArrayList<>();
    for (String riderInfo : riderInfos) {
      if (riderInfo != null) {
        try {
          riders.add(objectMapper.readValue(riderInfo, Rider.class));
        } catch (JsonProcessingException e) {
          throw new RuntimeException("Failed to deserialize rider info", e);
        }
      }
    }
    return riders;
  }

  public void updateRiderLoc(Integer riderId, Double lon, Double lat) {
    redisTemplate.opsForGeo().add(RIDER_LOC_PREFIX, new Point(lon, lat), riderId.toString());
  }

  public void deleteRiderLoc(Integer riderId) {
    redisTemplate.opsForGeo().remove(RIDER_LOC_PREFIX, riderId.toString());
  }

  public GeoResults<GeoLocation<String>> searchByLoc(Double lon, Double lat) {
    return redisTemplate.opsForGeo().search(
        RIDER_LOC_PREFIX,
        GeoReference.fromCoordinate(lon, lat),
        new Distance(10.0, DistanceUnit.KILOMETERS)
    );
  }
}
