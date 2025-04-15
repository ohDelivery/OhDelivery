package com.ohdelivery.gateway.apigateway.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ServiceConstants {

    USER_SERVICE("user-service", "lb://user-service", new String[]{"/users/**"}),
    DELIVERY_SERVICE("delivery-service", "lb://delivery-service", new String[]{"/deliveries/**"}),
    INCENTIVE_SERVICE("incentive-service", "lb://incentive-service", new String[]{"/incentives/**"});

    private final String serviceName;
    private final String serviceUri;
    private final String[] apiPaths;

}
