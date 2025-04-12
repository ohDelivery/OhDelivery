package com.ohdelivery.gateway.apigateway.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ohdelivery.gateway.apigateway.constants.ServiceConstants;
import com.ohdelivery.gateway.apigateway.filter.AuthenticationFilter;
import com.ohdelivery.gateway.apigateway.filter.TokenFilter;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder.Builder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class GatewayConfiguration {

    private final ObjectMapper objectMapper;
    private final WebClient.Builder webClientBuilder;

    @Bean
    public AuthenticationFilter jwtAuthFilter() {
        return new AuthenticationFilter(objectMapper);
    }

    @Bean
    public TokenFilter tokenFilter() {
        return new TokenFilter(webClientBuilder.build());
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder routeLocatorBuilder) {
        Builder builder = routeLocatorBuilder.routes();
        TokenFilter tokenFilter = tokenFilter();
        AuthenticationFilter authenticationFilter = jwtAuthFilter();

        for (ServiceConstants serviceConstant : ServiceConstants.values()) {
            addRoute(
                    builder,
                    serviceConstant.getServiceName(),
                    serviceConstant.getServiceUri(),
                    new GatewayFilter[]{tokenFilter, authenticationFilter},
                    serviceConstant.getApiPaths()
            );
        }

        addRoute(
                builder,
                "user-service",
                "lb://user-service",
                new GatewayFilter[]{},
                new String[] {"/api/users/join"}
        );
        addRoute(
                builder,
                "auth-server",
                "lb://auth-server",
                new GatewayFilter[]{},
                new String[] {"/auth/login", "/auth/validate", "/auth/logout"}
        );

        return builder.build();
    }

    private void addRoute(Builder builder, String serviceName, String serviceUri, GatewayFilter[] gatewayFilters,
                          String... apiPaths) {

        final GatewayFilter[] filters = gatewayFilters != null ? gatewayFilters : new GatewayFilter[0];

        builder.route(serviceName, routeSpec -> routeSpec
                .path(apiPaths)
                .filters(filterSpec -> {
                    Arrays.stream(filters).forEach(filterSpec::filter);
                    return filterSpec;
                })

                .uri(serviceUri)
        );
    }
}
