package com.ohdelivery.gateway.apigateway.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ServiceConstants {

    DELIVERY_SERVICE("delivery-service", "lb://delivery-service", new String[]{"/deliveries/**"}),
    INCENTIVE_SERVICE("incentive-service", "lb://incentive-service", new String[]{"/incentives/**"}),
    USER_SERVICE("user-service", "lb://user-service", new String[]{"/api/users"}),
    AUTH_SERVER("auth-server", "lb://auth-server", new String[]{"/auth/logout"}),
    DELIVERY_SERVICE("delivery-service", "lb://delivery-service", new String[]{"/deliveries/**"});
    MATCHING_SERVICE("match-service", "lb://match-service", new String[]{"/api/*"});

  private final String serviceName;
  private final String serviceUri;
  private final String[] apiPaths;

}
