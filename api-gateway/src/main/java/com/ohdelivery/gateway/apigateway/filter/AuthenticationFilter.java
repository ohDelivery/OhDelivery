package com.ohdelivery.gateway.apigateway.filter;

import com.ohdelivery.gateway.apigateway.AuthServerClient;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class AuthenticationFilter implements GatewayFilter {

    private final AuthServerClient authServerClient;
    
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return null;
    }
}
