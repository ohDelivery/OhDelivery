package com.ohdelivery.service.user.presentation.controller;

import com.ohdelivery.service.user.application.UserService;
import com.ohdelivery.service.user.presentation.request.CreateUserRequest;
import com.ohdelivery.service.user.presentation.request.UserLoginRequest;
import com.ohdelivery.service.user.presentation.response.UserAuthResponse;
import com.ohdelivery.service.user.presentation.response.UserLoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
    public UserLoginResponse login(@RequestBody UserLoginRequest request) {
        return userService.login(request.toCommand());
    }

    @GetMapping
    public UserAuthResponse getUserInfo(Long id) {
        return userService.getUser(id);
    }

    @PostMapping("/join")
    public void createUser(@RequestBody CreateUserRequest request) {
        userService.createUser(request.toCommand());
    }

    @DeleteMapping
    public void deleteUser(Long id) {
        userService.deleteUser(id);
    }
}
