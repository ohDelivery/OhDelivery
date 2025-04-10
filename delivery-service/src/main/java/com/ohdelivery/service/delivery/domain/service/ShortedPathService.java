package com.ohdelivery.service.delivery.domain.service;

import com.ohdelivery.service.delivery.infrastructure.dto.LocationInfo;
import com.ohdelivery.service.delivery.infrastructure.dto.PathInfo;

public interface ShortedPathService {

    LocationInfo getLocation(String address);

    PathInfo getPath(LocationInfo startLocation, LocationInfo goalLocation);
}
