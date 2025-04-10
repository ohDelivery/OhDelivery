package com.ohdelivery.service.delivery.domain.service;

import com.ohdelivery.service.delivery.infrastructure.dto.LocationInfo;

public interface ShortedPathService {

    LocationInfo getLocation(String address);
}
