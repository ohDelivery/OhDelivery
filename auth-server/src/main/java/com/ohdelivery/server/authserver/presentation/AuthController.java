package com.ohdelivery.server.authserver.presentation;

import com.ohdelivery.common.passport.Passport;
import com.ohdelivery.common.response.ApiResponse;
import com.ohdelivery.common.response.SuccessCode;
import com.ohdelivery.server.authserver.application.AuthService;
import com.ohdelivery.server.authserver.application.command.LoginCommand;
import com.ohdelivery.server.authserver.domain.Tokens;
import com.ohdelivery.server.authserver.presentation.request.LoginRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthController {

    private final AuthService authService;

    @PostMapping("/validate")
    public Passport validate(@RequestBody String token) {
        return authService.validate(token);
    }

    @PostMapping("/login")
    public ApiResponse<Tokens> login(@RequestBody LoginRequest request) {
        return ApiResponse.success(
                SuccessCode.COMMON_SUCCESS.getCode().toString(),
                "Login Successful",
                authService.login(LoginCommand.create(request.getUsername(), request.getPassword()))
        );
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        authService.logout(token);
        return ApiResponse.success(
                SuccessCode.COMMON_SUCCESS.getCode().toString(),
                "Logout Successful"
        );
    }

    @PostMapping("/refresh")
    public ApiResponse<Tokens> refreshToken(@RequestHeader("Authorization") String header) {
        if (!header.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid Token");
        }

        Tokens token = authService.refreshToken(header.substring(7));

        return ApiResponse.success(
                SuccessCode.COMMON_SUCCESS.getCode().toString(),
                "Token Refreshed Successfully",
                token
        );
    }

}
