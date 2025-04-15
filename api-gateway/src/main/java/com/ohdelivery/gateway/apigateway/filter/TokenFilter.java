package com.ohdelivery.gateway.apigateway.filter;

import static com.ohdelivery.gateway.apigateway.constants.GatewayConstants.AUTHORIZATION_HEADER;
import static com.ohdelivery.gateway.apigateway.constants.GatewayConstants.BEARER_PREFIX;
import static com.ohdelivery.gateway.apigateway.constants.GatewayConstants.PASSPORT_ATTRIBUTE;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ohdelivery.common.passport.Passport;
import com.ohdelivery.common.response.ApiResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
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
                .onStatus(status -> isHandledException(HttpStatus.valueOf(status.value())),
                        response -> response.bodyToMono(ApiResponse.class)
                                .flatMap(r ->{
                                    return writeErrorResponse(exchange, r);
                                })
                )
                .onStatus(HttpStatusCode::isError,
                        response -> Mono.error(new RuntimeException("인증 과정에 오류가 발생했습니다.")))
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

    private Mono<Void> writeErrorResponse(ServerWebExchange exchange, ApiResponse<Void> response) {
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        ObjectMapper mapper = new ObjectMapper();
        byte[] bytes;
        try {
            bytes = mapper.writeValueAsBytes(response);
        } catch (JsonProcessingException e) {
            bytes = "{\"오류 정보 추출 중 오류가 발생했습니다.\"}".getBytes(StandardCharsets.UTF_8);
        }

        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
        return exchange.getResponse().writeWith(Mono.just(buffer));
    }

    private boolean isHandledException(HttpStatusCode status) {
        return List.of(
                HttpStatus.UNAUTHORIZED,
                HttpStatus.BAD_REQUEST,
                HttpStatus.FORBIDDEN,
                HttpStatus.NOT_FOUND,
                HttpStatus.METHOD_NOT_ALLOWED,
                HttpStatus.CONFLICT,
                HttpStatus.INTERNAL_SERVER_ERROR
        ).contains(status);
    }
}
