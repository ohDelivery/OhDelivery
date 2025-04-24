package com.ohdelivery.service.delivery.application.dto.request;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class RiderLocationRequest {

    String riderId;
    Double longitude;
    Double latitude;
    Long timestamp;
}
