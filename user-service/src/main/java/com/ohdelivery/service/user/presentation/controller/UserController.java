package com.ohdelivery.service.user.presentation.controller;

import com.ohdelivery.common.response.ApiResponse;
import com.ohdelivery.common.response.SuccessCode;
import com.ohdelivery.service.user.application.UserService;
import com.ohdelivery.service.user.presentation.request.CreateUserRequest;
import com.ohdelivery.service.user.presentation.request.UpdateSlackIdRequest;
import com.ohdelivery.service.user.presentation.request.UserLoginRequest;
import com.ohdelivery.service.user.presentation.response.GetUserResponse;
import com.ohdelivery.service.user.presentation.response.UserAuthResponse;
import com.ohdelivery.service.user.presentation.response.UserLoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public ApiResponse<UserLoginResponse> login(@RequestBody UserLoginRequest request) {
        return ApiResponse.success(
                SuccessCode.COMMON_SUCCESS.getCode().toString(),
                "Login Success",
                userService.login(request.toCommand())
        );
    }

    @GetMapping("/auth")
    public UserAuthResponse getUserInfo(Long id) {
        return userService.getUserInfo(id);
    }

    @GetMapping
    public ApiResponse<GetUserResponse> getUser(Long id) {
        return ApiResponse.success(
                SuccessCode.COMMON_SUCCESS.getCode().toString(),
                SuccessCode.COMMON_SUCCESS.getMessage(),
                userService.getUser(id)
        );
    }
    @PostMapping("/join")
    public ApiResponse<Void> createUser(@RequestBody CreateUserRequest request) {
        userService.createUser(request.toCommand());

        return ApiResponse.success(
                SuccessCode.COMMON_SUCCESS.getCode().toString(),
                SuccessCode.COMMON_SUCCESS.getMessage()
        );
    }

    @DeleteMapping
    public ApiResponse<Void> deleteUser(Long id) {
        userService.deleteUser(id);

        return ApiResponse.success(
            SuccessCode.DELETED_SUCCESS.getCode().toString(),
            SuccessCode.DELETED_SUCCESS.getMessage()
        );
    }

    @PatchMapping
    public ApiResponse<Void> updateSlackId(@RequestBody UpdateSlackIdRequest request){
        userService.updateSlackId(request.toCommand());
        return ApiResponse.success(
                SuccessCode.UPDATED_SUCCESS.getCode().toString(),
                SuccessCode.UPDATED_SUCCESS.getMessage()
        );
    }
}
