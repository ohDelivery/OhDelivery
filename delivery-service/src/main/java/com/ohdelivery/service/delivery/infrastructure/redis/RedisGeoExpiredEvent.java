package com.ohdelivery.service.delivery.infrastructure.redis;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RedisGeoExpiredEvent {

    String riderId;
}
