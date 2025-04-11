package com.ohdelivery.service.delivery.infrastructure.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class GeocodeResponse {

    private String status;
    private Meta meta;
    private List<Address> addresses;
    private String errorMessage;

    @Getter
    public static class Meta {

        private int totalCount;
        private int page;
        private int count;
    }

    @Getter
    public static class Address {

        private String roadAddress;
        private String jibunAddress;
        private String englishAddress;
        private List<AddressElement> addressElements;
        private String x;
        private String y;
        private double distance;
    }

    @Getter
    public static class AddressElement {

        private List<String> types;
        private String longName;
        private String shortName;
        private String code;
    }
}
