package com.ohdelivery.service.delivery.application.dto.request;

import java.util.UUID;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class RiderLocationRequest {

    UUID riderId;
    Double longitude;
    Double latitude;
    Long timestamp;
}
