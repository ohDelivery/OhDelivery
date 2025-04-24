package com.ohdelivery.gateway.apigateway.routes;

import com.ohdelivery.gateway.apigateway.routes.model.RouteConfig;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DefaultRouteFactory implements RouteFactory {

  private final Map<String, List<GatewayFilter>> filterRegistry;

  public void createRoute(Builder builder, RouteConfig route,String serviceUri) {
      GatewayFilter[] filters = Arrays.stream(route.getFilters() != null ? route.getFilters() : new String[]{})
          .map(filterRegistry::get)
          .filter(Objects::nonNull)
          .flatMap(List::stream)
          .toArray(GatewayFilter[]::new);

      builder.route(route.getId(), r -> r
          .path(route.getApiPaths())
          .filters(f -> {
              Arrays.stream(filters).forEach(f::filter);
              return f;
          })
          .uri(serviceUri));
  }
}
