package com.ohdelivery.gateway.apigateway.routes.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RouteConfig {
  private String id;
  private String[] apiPaths;
  private String[] filters;
}
