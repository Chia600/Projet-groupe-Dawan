package com.dawanproject.booktracker.services.impl;

import com.dawanproject.booktracker.dtos.AccountResponseDto;
import com.dawanproject.booktracker.dtos.LoginRequestDto;
import com.dawanproject.booktracker.dtos.RegisterRequestDto;
import com.dawanproject.booktracker.dtos.UserDto;
import com.dawanproject.booktracker.entities.Role;
import com.dawanproject.booktracker.entities.User;
import com.dawanproject.booktracker.enums.RoleName;
import com.dawanproject.booktracker.mappers.UserMapper;
import com.dawanproject.booktracker.repositories.UserRepository;
import com.dawanproject.booktracker.security.JwtTokenUtil;
import com.dawanproject.booktracker.services.AccountService;
import com.dawanproject.booktracker.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;
    private final JwtTokenUtil jwtTokenUtil;
    private final AccountService accountService;

    @Override
    public Optional<UserDto> register(RegisterRequestDto request) {
        if (userRepository.findByUsername(request.username()).isPresent()) {
            return Optional.empty();
        }

        User user = userMapper.registerRequestDtoToEntity(request);
        user.setSubscriptionDate(LocalDate.now());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        List<Role> role = Arrays.asList(new Role(RoleName.USER));
        accountService.addRoleToUser(savedUser, role);

        return Optional.of(userMapper.toDTO(savedUser));
    }

    @Override
    public ResponseEntity<AccountResponseDto> login(LoginRequestDto request) {

        User user;
        try {
            user = userRepository.findByUsername(request.username()).orElseThrow(() -> new UsernameNotFoundException("User does not exist"));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.badRequest().body(AccountResponseDto.builder().message(e.getMessage()).build());
        }

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        var jwtToken = jwtTokenUtil.generateToken(user, user.getUserId());

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Access-Control-Expose-Headers", "Authorization");
        responseHeaders.add("Authorization", "Bearer " + jwtToken);

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(AccountResponseDto.builder().message("user successfully authenticated").token(jwtToken).build());
    }
}
