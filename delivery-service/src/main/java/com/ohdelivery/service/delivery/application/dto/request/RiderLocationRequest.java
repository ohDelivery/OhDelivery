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

    public String toMessage() {
        return String.format("{\"latitude\": %.6f, \"longitude\": %.6f}", latitude, longitude);
    }
}
