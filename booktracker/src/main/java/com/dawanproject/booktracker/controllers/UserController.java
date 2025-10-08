package com.dawanproject.booktracker.controllers;

import com.dawanproject.booktracker.dtos.UserDto;
import com.dawanproject.booktracker.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * REST controller for managing User entities and their book collections.
 */
@RestController
@RequestMapping("/users")
@Validated
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Registers a new user with a hashed password (public endpoint).
     *
     * @param userDTO The user data to register.
     * @return ResponseEntity containing the created user and HTTP status 201 (Created).
     */
    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@Valid @RequestBody UserDto userDTO) {
        UserDto createdUser = userService.registerUser(userDTO);
        return ResponseEntity.status(201).body(createdUser);
    }

    /**
     * Creates a new user with a hashed password.
     *
     * @param userDTO The user data to create.
     * @return ResponseEntity containing the created user and HTTP status 201 (Created).
     */
    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDTO) {
        UserDto createdUser = userService.createUser(userDTO);
        return ResponseEntity.status(201).body(createdUser);
    }

    /**
     * Retrieves all users.
     *
     * @return ResponseEntity containing the list of all users and HTTP status 200 (OK).
     */
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id The ID of the user to retrieve.
     * @return ResponseEntity containing the user if found, or HTTP status 404 (Not Found).
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Updates an existing user.
     *
     * @param id The ID of the user to update.
     * @param userDTO The updated user data.
     * @return ResponseEntity containing the updated user if found, or HTTP status 404 (Not Found).
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @Valid @RequestBody UserDto userDTO) {
        return userService.updateUser(id, userDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Deletes a user by their ID.
     *
     * @param id The ID of the user to delete.
     * @return ResponseEntity with HTTP status 204 (No Content) if deleted, or 404 (Not Found).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (userService.deleteUser(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Retrieves a user by their username.
     *
     * @param username The username of the user.
     * @return ResponseEntity containing the user if found, or HTTP status 404 (Not Found).
     */
    @GetMapping("/username/{username}")
    public ResponseEntity<UserDto> getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Retrieves a user by their email.
     *
     * @param email The email of the user.
     * @return ResponseEntity containing the user if found, or HTTP status 404 (Not Found).
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Retrieves the collection of favorite books for a user.
     *
     * @param id The ID of the user.
     * @return ResponseEntity containing the list of book IDs and HTTP status 200 (OK), or 404 (Not Found).
     */
    @GetMapping("/{id}/books")
    public ResponseEntity<List<Long>> getFavoriteBooks(@PathVariable Long id) {
        return userService.getFavoriteBooks(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Adds a book to the user's collection of favorite books.
     *
     * @param id The ID of the user.
     * @param bookId The ID of the book to add.
     * @return ResponseEntity with HTTP status 200 (OK) if added, or 404 (Not Found).
     */
    @PostMapping("/{id}/books")
    public ResponseEntity<Void> addFavoriteBook(@PathVariable Long id, @RequestBody Long bookId) {
        if (userService.addFavoriteBook(id, bookId)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Removes a book from the user's collection of favorite books.
     *
     * @param id The ID of the user.
     * @param bookId The ID of the book to remove.
     * @return ResponseEntity with HTTP status 204 (No Content) if removed, or 404 (Not Found).
     */
    @DeleteMapping("/{id}/books/{bookId}")
    public ResponseEntity<Void> removeFavoriteBook(@PathVariable Long id, @PathVariable Long bookId) {
        if (userService.removeFavoriteBook(id, bookId)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}