package com.ohdelivery.gateway.apigateway.routes;

import com.ohdelivery.gateway.apigateway.routes.model.RouteConfig;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder.Builder;

public interface RouteFactory {

  void createRoute(Builder builder, RouteConfig route, String serviceUri);

}
