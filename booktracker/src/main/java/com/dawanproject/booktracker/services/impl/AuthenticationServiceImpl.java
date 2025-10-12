package com.dawanproject.booktracker.services.impl;

import com.dawanproject.booktracker.dtos.LoginRequestDto;
import com.dawanproject.booktracker.dtos.RegisterRequestDto;
import com.dawanproject.booktracker.dtos.AccountResponseDto;
import com.dawanproject.booktracker.entities.User;
import com.dawanproject.booktracker.mappers.UserMapper;
import com.dawanproject.booktracker.repositories.UserRepository;
import com.dawanproject.booktracker.security.JwtTokenUtil;
import com.dawanproject.booktracker.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailService;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public ResponseEntity<AccountResponseDto> register(RegisterRequestDto request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body(AccountResponseDto.builder().message("Username already exists").build());
        }

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(AccountResponseDto.builder().message("Email already exists").build());
        }

        User user = userMapper.registerRequestDtoToEntity(request);
        user.setSubscriptionDate(LocalDate.now());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);

        userMapper.toDTO(savedUser);

        return ResponseEntity.ok(AccountResponseDto.builder().message("User successfully registered").build());
    }

    @Override
    public ResponseEntity<AccountResponseDto> login(LoginRequestDto request) {

        UserDetails userDetails = userDetailService.loadUserByUsername(request.getUsername());
        //var user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        var jwtToken = jwtTokenUtil.generateToken(userDetails);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Access-Control-Expose-Headers", "Authorization");
        responseHeaders.add("Authorization", "Bearer " + jwtToken);

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(AccountResponseDto.builder().message("user successfully authenticated").build());
    }
}
