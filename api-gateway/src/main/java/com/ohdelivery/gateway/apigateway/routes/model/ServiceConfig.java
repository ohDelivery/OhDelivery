package com.ohdelivery.gateway.apigateway.routes.model;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServiceConfig {
  private String serviceUri;
  private List<RouteConfig> routes;
}
