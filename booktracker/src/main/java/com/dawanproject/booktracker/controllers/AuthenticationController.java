package com.dawanproject.booktracker.controllers;

import com.dawanproject.booktracker.dtos.AccountResponseDto;
import com.dawanproject.booktracker.dtos.LoginRequestDto;
import com.dawanproject.booktracker.dtos.RegisterRequestDto;
import com.dawanproject.booktracker.dtos.UserDto;
import com.dawanproject.booktracker.services.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authService;

    /**
     * Registers a new user with a hashed password (public endpoint).
     *
     * @param registerRequestDto The user data to register.
     * @return ResponseEntity containing the created user and HTTP status 201 (Created).
     */
    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@Valid @RequestBody RegisterRequestDto registerRequestDto) {
        return authService.register(registerRequestDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(409).build());
    }

    /**
     * Login
     *
     * @param loginRequestDto The user data to login.
     * @return ResponseEntity containing the created user and HTTP status 201 (Created).
     */
    @PostMapping("/login")
    public ResponseEntity<AccountResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        return authService.login(loginRequestDto);
    }
}
