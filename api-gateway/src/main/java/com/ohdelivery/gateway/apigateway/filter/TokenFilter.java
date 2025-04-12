package com.ohdelivery.gateway.apigateway.filter;

import static com.ohdelivery.gateway.apigateway.constants.FilterConstants.AUTHORIZATION_HEADER;
import static com.ohdelivery.gateway.apigateway.constants.FilterConstants.BEARER_PREFIX;
import static com.ohdelivery.gateway.apigateway.constants.FilterConstants.PASSPORT_ATTRIBUTE;

import com.ohdelivery.common.passport.Passport;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class TokenFilter implements GatewayFilter {

    private final WebClient webClient;

    @Value("${auth-server.validate-url}")
    private String validateUrl;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        HttpHeaders headers = exchange.getRequest().getHeaders();
        String authorization = headers.getFirst(AUTHORIZATION_HEADER);

        if (authorization == null || !authorization.startsWith(BEARER_PREFIX)) {
            return unauthorizedResponse(exchange.getResponse());
        }

        String token = authorization.substring(BEARER_PREFIX.length());

        return webClient.post()
                .uri(validateUrl)
                .bodyValue(token)
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        response -> Mono.error(new RuntimeException("인증에 실패했습니다.")))
                .bodyToMono(Passport.class)
                .flatMap(passport -> {
                    exchange.getAttributes().put(PASSPORT_ATTRIBUTE, passport);
                    return chain.filter(exchange);
                });
    }

    private Mono<Void> unauthorizedResponse(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }
}
