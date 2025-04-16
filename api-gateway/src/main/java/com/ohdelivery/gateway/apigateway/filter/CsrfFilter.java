package com.ohdelivery.gateway.apigateway.filter;

import static com.ohdelivery.gateway.apigateway.constants.GatewayConstants.CSRF_HEADER;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class CsrfFilter implements GatewayFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (!exchange.getRequest().getMethod().matches(HttpMethod.GET.name())) {
            String csrfToken = exchange.getRequest().getHeaders().getFirst(CSRF_HEADER);
            if (csrfToken == null || !isValid(exchange, csrfToken)) {
                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                return exchange.getResponse().setComplete();
            }
        }
        return chain.filter(exchange);
    }

    private boolean isValid(ServerWebExchange exchange, String headerToken) {
        return exchange.getRequest().getCookies().getFirst(CSRF_HEADER) != null &&
                headerToken.equals(exchange.getRequest().getCookies().getFirst(CSRF_HEADER).getValue());
    }
}