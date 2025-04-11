package com.ohdelivery.gateway.apigateway.config;

import com.ohdelivery.gateway.apigateway.AuthServerClient;
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

    private final AuthServerClient authServiceClient;
    private final WebClient.Builder webClientBuilder;

    @Bean
    public AuthenticationFilter jwtAuthFilter() {
        return new AuthenticationFilter(authServiceClient);
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

        return builder.build();
    }

    private void addRoute(Builder builder, String serviceName, String serviceUri, GatewayFilter[] gatewayFilters,
                          String... apiPaths) {

        final GatewayFilter[] filters = gatewayFilters != null ? gatewayFilters : new GatewayFilter[0];

        builder.route(serviceName, routeSpec -> routeSpec
                .path(apiPaths)
                .filters(filterSpec -> {
                    // 필터 배열 순회하며 추가
                    Arrays.stream(filters).forEach(filterSpec::filter);
                    return filterSpec;
                })

                .uri(serviceUri)
        );
    }
}
