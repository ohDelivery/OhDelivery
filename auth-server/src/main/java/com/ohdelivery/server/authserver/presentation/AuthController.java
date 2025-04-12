package com.ohdelivery.server.authserver.presentation;

import com.ohdelivery.common.passport.Passport;
import com.ohdelivery.common.response.ApiResponse;
import com.ohdelivery.common.response.SuccessCode;
import com.ohdelivery.server.authserver.application.AuthService;
import com.ohdelivery.server.authserver.application.command.LoginCommand;
import com.ohdelivery.server.authserver.domain.Tokens;
import com.ohdelivery.server.authserver.presentation.request.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    public ApiResponse<Void> logout(){
        return ApiResponse.success(
                SuccessCode.COMMON_SUCCESS.getCode().toString(),
                "Logout Successful",
                null
        );
    }
}
