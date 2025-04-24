package com.ohdelivery.gateway.apigateway.config;

import com.ohdelivery.gateway.apigateway.routes.RouteFactoryRegistry;
import com.ohdelivery.gateway.apigateway.routes.config.GatewayServiceProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class GatewayConfiguration {
  private final GatewayServiceProperties serviceProperties;
  private final RouteFactoryRegistry routeFactoryRegistry;

  @Bean
  public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
    var routes = builder.routes();
    routeFactoryRegistry.createRoutes(routes, serviceProperties.getServices());
    return routes.build();
  }
}
