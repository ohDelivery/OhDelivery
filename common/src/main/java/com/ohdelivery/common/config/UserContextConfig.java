package com.ohdelivery.common.config;

import com.ohdelivery.common.passport.usercontext.PassportArgumentResolver;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class UserContextConfig implements WebMvcConfigurer {
    private final PassportArgumentResolver passportArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(passportArgumentResolver);
    }
}
