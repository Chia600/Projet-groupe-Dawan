package com.dawanproject.booktracker.services;

import com.dawanproject.booktracker.dtos.LoginRequestDto;
import com.dawanproject.booktracker.dtos.RegisterRequestDto;
import com.dawanproject.booktracker.dtos.AccountResponseDto;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {

    /**
     * Registers a new user with a hashed password.
     *
     * @param registerRequestDto The user data to register.
     * @return ResponseEntity<UserResponseDto>
     */
    ResponseEntity<AccountResponseDto> register(RegisterRequestDto registerRequestDto);

    /**
     * Login to account
     *
     * @param loginRequestDto User data to login
     * @return ResponseEntity<UserResponseDto>
     */
    ResponseEntity<AccountResponseDto> login(LoginRequestDto loginRequestDto);
}
