package com.ohdelivery.gateway.apigateway.filter;

import static com.ohdelivery.common.passport.PassportConstant.PASSPORT_HEADER;
import static com.ohdelivery.gateway.apigateway.constants.GatewayConstants.PASSPORT_ATTRIBUTE;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ohdelivery.common.passport.Passport;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class AuthenticationFilter implements GatewayFilter {

    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        Passport passport = exchange.getAttribute(PASSPORT_ATTRIBUTE);

        if (passport == null) {
            return Mono.error(new RuntimeException("로그인한 사용자 정보가 없습니다."));
        }

        try {
            String passportJson = objectMapper.writeValueAsString(passport);

            ServerWebExchange mutatedExchange = exchange.mutate()
                    .request(exchange.getRequest().mutate()
                            .header(PASSPORT_HEADER, passportJson)
                            .build())
                    .build();

        return chain.filter(mutatedExchange);
        } catch (Exception e) {
            return Mono.error(new RuntimeException("로그인한 사용자 정보 등록에 실패했습니다.", e));
        }

    }
}
