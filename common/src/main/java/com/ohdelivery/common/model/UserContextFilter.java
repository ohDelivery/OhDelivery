package com.ohdelivery.common.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ohdelivery.common.passport.Passport;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import org.springframework.stereotype.Component;

@Component
public class UserContextFilter implements Filter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            String passportJson = httpServletRequest.getHeader("X-User-Role");

            if (passportJson != null && !passportJson.isEmpty()) {
                Passport passport = objectMapper.readValue(passportJson, Passport.class);
                UserContextHolder.setCurrentUser(passport.getUserId());
            }

            chain.doFilter(request, response);
        }finally {
            UserContextHolder.clear();
        }
    }
}
