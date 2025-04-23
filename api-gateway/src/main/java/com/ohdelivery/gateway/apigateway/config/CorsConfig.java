package com.ohdelivery.gateway.apigateway.config;

import static com.ohdelivery.common.passport.PassportConstant.PASSPORT_HEADER;
import static com.ohdelivery.gateway.apigateway.constants.GatewayConstants.AUTHORIZATION_HEADER;

import java.util.List;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsWebFilter;

@Configuration
public class CorsConfig {

    @Value("${url.origin.origin-url}")
    private String originUrl;

    @Bean
    public CorsWebFilter corsFilter() {
        return new CorsWebFilter(corsWebFilter());
    }

    public CorsConfigurationSource corsWebFilter() {
        return exchange -> {
            String upgrade = exchange.getRequest().getHeaders().getUpgrade();
            if ("websocket".equalsIgnoreCase(upgrade)) {
                return null;
            }
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowCredentials(true);
            config.setAllowedOrigins(List.of(originUrl));
            config.setAllowedMethods(
                List.of(
                    HttpMethod.GET.name(),
                    HttpMethod.POST.name(),
                    HttpMethod.PUT.name(),
                    HttpMethod.DELETE.name()
                )
            );
            config.setAllowedHeaders(List.of(AUTHORIZATION_HEADER, HttpHeaders.CONTENT_TYPE));
            config.setExposedHeaders(List.of(PASSPORT_HEADER));

            return config;
        };
    }

}
