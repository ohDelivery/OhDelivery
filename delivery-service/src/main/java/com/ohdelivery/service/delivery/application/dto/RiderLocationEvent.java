package com.ohdelivery.service.delivery.application.dto;

import java.util.UUID;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class RiderLocationEvent {

    UUID riderId;
    Double longitude;
    Double latitude;
    Long timestamp;
}
