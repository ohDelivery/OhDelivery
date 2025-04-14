package com.ohdelivery.service.delivery.infrastructure.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RiderLocation {

    Double longitude;
    Double latitude;
    Long timestamp;
}
