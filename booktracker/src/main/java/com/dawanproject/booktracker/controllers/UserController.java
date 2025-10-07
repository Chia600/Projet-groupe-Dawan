package com.dawanproject.booktracker.controllers;

import com.dawanproject.booktracker.dtos.UserDto;
import com.dawanproject.booktracker.entities.User;
import com.dawanproject.booktracker.mappers.UserMapper;
import com.dawanproject.booktracker.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing User entities.
 */
@RestController
@RequestMapping("/users")
@Validated
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    /**
     * Registers a new user with a hashed password (public endpoint).
     *
     * @param userDTO The user data to register, provided in the request body.
     * @return ResponseEntity containing the created user and HTTP status 201 (Created).
     * @throws jakarta.validation.ConstraintViolationException if the user data violates validation constraints.
     */
    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@Valid @RequestBody UserDto userDTO) {
        User user = userMapper.toEntity(userDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        return ResponseEntity.status(201).body(userMapper.toDTO(savedUser));
    }

    /**
     * Creates a new user with a hashed password.
     *
     * @param userDTO The user data to create, provided in the request body.
     * @return ResponseEntity containing the created user and HTTP status 201 (Created).
     * @throws jakarta.validation.ConstraintViolationException if the user data violates validation constraints.
     */
    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDTO) {
        User user = userMapper.toEntity(userDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        return ResponseEntity.status(201).body(userMapper.toDTO(savedUser));
    }

    /**
     * Retrieves all users.
     *
     * @return ResponseEntity containing the list of all users and HTTP status 200 (OK).
     */
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userRepository.findAll().stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id The ID of the user to retrieve.
     * @return ResponseEntity containing the user if found, or HTTP status 404 (Not Found) if not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(userMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Updates an existing user.
     *
     * @param id The ID of the user to update.
     * @param userDTO The updated user data provided in the request body.
     * @return ResponseEntity containing the updated user if found, or HTTP status 404 (Not Found) if not found.
     * @throws jakarta.validation.ConstraintViolationException if the updated user data violates validation constraints.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @Valid @RequestBody UserDto userDTO) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            User user = userMapper.toEntity(userDTO);
            user.setUserId(id);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User savedUser = userRepository.save(user);
            return ResponseEntity.ok(userMapper.toDTO(savedUser));
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Deletes a user by their ID.
     *
     * @param id The ID of the user to delete.
     * @return ResponseEntity with HTTP status 204 (No Content) if deleted, or 404 (Not Found) if not found.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Retrieves a user by their username.
     *
     * @param username The username of the user to retrieve.
     * @return ResponseEntity containing the user if found, or HTTP status 404 (Not Found) if not found.
     */
    @GetMapping("/username/{username}")
    public ResponseEntity<UserDto> getUserByUsername(@PathVariable String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.map(userMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Retrieves a user by their email.
     *
     * @param email The email of the user to retrieve.
     * @return ResponseEntity containing the user if found, or HTTP status 404 (Not Found) if not found.
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.map(userMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}