package com.ohdelivery.gateway.apigateway.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ohdelivery.gateway.apigateway.filter.AuthenticationFilter;
import com.ohdelivery.gateway.apigateway.filter.TokenFilter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class FilterConfiguration {

    @Bean
    public AuthenticationFilter jwtAuthFilter(ObjectMapper objectMapper) {
        return new AuthenticationFilter(objectMapper);
    }

    @Bean
    public TokenFilter tokenFilter(WebClient webClient) {
        return new TokenFilter(webClient);
    }

    @Bean
    public Map<String, List<GatewayFilter>> filterRegistry(TokenFilter tokenFilter, AuthenticationFilter authenticationFilter) {
        return Map.of(
            "token-filter", Arrays.asList(tokenFilter),
            "auth-filter", Arrays.asList(authenticationFilter),
            "service-default", Arrays.asList(tokenFilter, authenticationFilter),
            "websocket-default", Arrays.asList()
        );
    }
}