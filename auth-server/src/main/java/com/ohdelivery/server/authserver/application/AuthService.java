package com.ohdelivery.server.authserver.application;

import com.ohdelivery.common.passport.Passport;
import com.ohdelivery.server.authserver.application.command.LoginCommand;
import com.ohdelivery.server.authserver.application.dto.ResponseWrapper;
import com.ohdelivery.server.authserver.application.dto.UserInfo;
import com.ohdelivery.server.authserver.application.jwt.JwtProvider;
import com.ohdelivery.server.authserver.application.jwt.exceptions.UserException.UserNotFoundException;
import com.ohdelivery.server.authserver.domain.TokenStatus;
import com.ohdelivery.server.authserver.domain.Tokens;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final JwtProvider jwtProvider;
    private final WebClient webClient;

    @Value("${user-service.login-url}")
    private String loginUrl;

    public Tokens login(LoginCommand loginCommand) {
        ResponseWrapper response = webClient.post()
                .uri(loginUrl)
                .bodyValue(loginCommand)
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        r -> Mono.error(new UserNotFoundException())
                )
                .bodyToMono(ResponseWrapper.class)
                .block();

        return jwtProvider.createTokens(
                response.getData().getId().toString(),
                response.getData().getRole().getAuthority()
        );
    }

    public Passport validate(String token) {
        UserInfo userInfo = jwtProvider.validateToken(token);
        return new Passport(userInfo.getId(), userInfo.getRole());
    }

    public void logout(String token){
        jwtProvider.invalidateTokens(token, TokenStatus.LOGGED_OUT);
    }

    public Tokens refreshToken(String token) {
        return jwtProvider.checkRefreshToken(token);
    }
}
