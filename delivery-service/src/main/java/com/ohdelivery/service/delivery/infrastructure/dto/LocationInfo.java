package com.ohdelivery.service.delivery.infrastructure.dto;

import com.ohdelivery.service.delivery.infrastructure.exception.InvalidAddressException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LocationInfo {

    private Double latitude;
    private Double longitude;

    public static LocationInfo fromGeocodeResponse(GeocodeResponse geocodeResponse) {
        if (geocodeResponse == null || geocodeResponse.getAddresses() == null
            || geocodeResponse.getAddresses().isEmpty()) {
            throw new InvalidAddressException();
        }

        Double latitude = Double.parseDouble(geocodeResponse.getAddresses().get(0).getY());
        Double longitude = Double.parseDouble(geocodeResponse.getAddresses().get(0).getX());

        return new LocationInfo(latitude, longitude);
    }
}
