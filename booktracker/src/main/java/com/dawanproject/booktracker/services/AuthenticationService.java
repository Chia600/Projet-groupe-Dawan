package com.dawanproject.booktracker.services;

import com.dawanproject.booktracker.dtos.AccountResponseDto;
import com.dawanproject.booktracker.dtos.LoginRequestDto;
import com.dawanproject.booktracker.dtos.RegisterRequestDto;
import com.dawanproject.booktracker.dtos.UserDto;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface AuthenticationService {

    /**
     * Registers a new user with a hashed password.
     *
     * @param registerRequestDto The user data to register.
     * @return Optional containing the UserDTO if created, empty otherwise.
     */
    Optional<UserDto> register(RegisterRequestDto registerRequestDto);

    /**
     * Login to account
     *
     * @param loginRequestDto User data to login
     * @return Optional containing the UserDTO if found, empty otherwise.
     */
    ResponseEntity<AccountResponseDto> login(LoginRequestDto loginRequestDto);
}
