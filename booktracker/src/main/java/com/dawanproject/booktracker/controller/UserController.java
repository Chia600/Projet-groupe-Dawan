package com.dawanproject.booktracker.controller;

import com.dawanproject.booktracker.entities.User;
import com.dawanproject.booktracker.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing User entities.
 */
@RestController
@RequestMapping("/users")
@Validated
@RequiredArgsConstructor
public class UserController {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    /**
     * Registers a new user with a hashed password.
     *
     * @param user The user to register, provided in the request body.
     * @return ResponseEntity containing the created user and HTTP status 201 (Created).
     * @throws jakarta.validation.ConstraintViolationException if the user data violates validation constraints.
     */
    @PostMapping("/register")
    @PreAuthorize("permitAll()")
    public ResponseEntity<User> registerUser(@Valid @RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        return ResponseEntity.status(201).body(savedUser);
    }

    /**
     * Creates a new user with a hashed password.
     *
     * @param user The user to create, provided in the request body.
     * @return ResponseEntity containing the created user and HTTP status 201 (Created).
     * @throws jakarta.validation.ConstraintViolationException if the user data violates validation constraints.
     */
    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        return ResponseEntity.status(201).body(savedUser);
    }

    /**
     * Retrieves all users.
     *
     * @return ResponseEntity containing the list of all users and HTTP status 200 (OK).
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id The ID of the user to retrieve.
     * @return ResponseEntity containing the user if found, or HTTP status 404 (Not Found) if not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(ResponseEntity::ok)
                   .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Updates an existing user.
     *
     * @param id The ID of the user to update.
     * @param updatedUser The updated user data provided in the request body.
     * @return ResponseEntity containing the updated user if found, or HTTP status 404 (Not Found) if not found.
     * @throws jakarta.validation.ConstraintViolationException if the updated user data violates validation constraints.
     */
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @Valid @RequestBody User updatedUser) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            updatedUser.setUserId(id);
            updatedUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            User savedUser = userRepository.save(updatedUser);
            return ResponseEntity.ok(savedUser);
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
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.map(ResponseEntity::ok)
                   .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Retrieves a user by their email.
     *
     * @param email The email of the user to retrieve.
     * @return ResponseEntity containing the user if found, or HTTP status 404 (Not Found) if not found.
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.map(ResponseEntity::ok)
                   .orElseGet(() -> ResponseEntity.notFound().build());
    }
}