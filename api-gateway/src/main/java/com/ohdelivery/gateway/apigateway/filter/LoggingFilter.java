package com.ohdelivery.gateway.apigateway.filter;

import lombok.extern.log4j.Log4j2;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@Log4j2
public class LoggingFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("REQUEST {} : {} ", exchange.getRequest().getMethod(), exchange.getRequest().getPath());
        return chain.filter(exchange)
                .then(Mono.fromRunnable(() -> {
                    log.info("RESPONSE {} : {} ", exchange.getResponse().getStatusCode(),
                            exchange.getRequest().getPath());
                }));
    }
}
