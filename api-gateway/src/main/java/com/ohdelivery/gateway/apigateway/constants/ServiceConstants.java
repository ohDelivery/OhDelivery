package com.ohdelivery.gateway.apigateway.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ServiceConstants {

    USER_SERVICE("user-service", "lb://user-service", new String[]{"/api/users"});

    private final String serviceName;
    private final String serviceUri;
    private final String[] apiPaths;

}
