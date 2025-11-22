package com.onlineshop.controller;

import com.onlineshop.dto.AuthDtos;
import com.onlineshop.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:8080, http://localhost:8081") // sesuaikan dengan port frontend
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public AuthDtos.UserResponse register(@RequestBody AuthDtos.RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthDtos.UserResponse login(@RequestBody AuthDtos.LoginRequest request) {
        return authService.login(request);
    }
}
