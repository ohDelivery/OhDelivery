package com.ohdelivery.gateway.apigateway.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ServiceConstants {

  USER_SERVICE("user-service", "lb://user-service", new String[]{"/api/users"}),
  AUTH_SERVER("auth-server", "lb://auth-server", new String[]{"/auth/logout"}),
  MATCHING_SERVICE("match-service", "lb://match-service", new String[]{"/api/*"});

  private final String serviceName;
  private final String serviceUri;
  private final String[] apiPaths;

}
