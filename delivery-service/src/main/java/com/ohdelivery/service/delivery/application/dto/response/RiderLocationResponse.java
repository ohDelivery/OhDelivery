package com.ohdelivery.service.delivery.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RiderLocationResponse {

    Double longitude;
    Double latitude;


    public String toMessage() {
        return String.format("{\"latitude\": %.6f, \"longitude\": %.6f}", latitude, longitude);
    }
}
