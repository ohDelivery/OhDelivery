package com.ohdelivery.gateway.apigateway.routes;

import com.ohdelivery.gateway.apigateway.routes.model.RouteConfig;
import com.ohdelivery.gateway.apigateway.routes.model.ServiceConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder.Builder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RouteFactoryRegistry {

    private final RouteFactory routeFactory;

    public void createRoutes(Builder builder, Iterable<ServiceConfig> services) {
        for (ServiceConfig service : services) {
            for (RouteConfig route : service.getRoutes()) {
                routeFactory.createRoute(builder, route, service.getServiceUri());
            }
        }
    }
}