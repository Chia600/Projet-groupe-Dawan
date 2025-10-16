package com.dawanproject.booktracker.controllers;

import com.dawanproject.booktracker.dtos.UserDto;
import com.dawanproject.booktracker.entities.FavoriteBook;
import com.dawanproject.booktracker.repositories.FavoriteBookRepository;
import com.dawanproject.booktracker.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller for managing User entities and their book collections.
 */
@RestController
@RequestMapping("/api/users")
@Validated
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final FavoriteBookRepository favoriteBookRepository;

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
     * @param id      The ID of the user to update.
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
     * Retrieves the collection of favorite books (Google Books IDs) for a user.
     *
     * @param id The ID of the user.
     * @return ResponseEntity containing the list of Google Books IDs and HTTP status 200 (OK).
     */
    @GetMapping("/{id}/books")
    public ResponseEntity<List<String>> getFavoriteBooks(@PathVariable Long id) {
        List<String> googleBookIds = favoriteBookRepository.findByUserId(id)
                .stream()
                .map(FavoriteBook::getGoogleBookId)
                .collect(Collectors.toList());
        return ResponseEntity.ok(googleBookIds);
    }

    /**
     * Adds a book (Google Books ID) to the user's collection of favorite books.
     *
     * @param id           The ID of the user.
     * @param googleBookId The Google Books ID (idVolume) to add.
     * @return ResponseEntity with HTTP status 200 (OK) if added.
     */
    @PostMapping("/{id}/books")
    public ResponseEntity<Void> addFavoriteBook(@PathVariable Long id, @RequestBody String googleBookId) {
        // Nettoyer l'ID (enlever les guillemets si présents)
        googleBookId = googleBookId.replace("\"", "").trim();
        
        // Vérifier si l'utilisateur existe
        if (userService.getUserById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        // Vérifier si déjà en favoris
        if (favoriteBookRepository.findByUserIdAndGoogleBookId(id, googleBookId).isPresent()) {
            return ResponseEntity.ok().build(); // Déjà présent, pas d'erreur
        }
        
        // Ajouter aux favoris
        FavoriteBook favorite = new FavoriteBook(id, googleBookId);
        favoriteBookRepository.save(favorite);
        
        return ResponseEntity.ok().build();
    }

    /**
     * Removes a book from the user's collection of favorite books.
     *
     * @param id           The ID of the user.
     * @param googleBookId The Google Books ID to remove.
     * @return ResponseEntity with HTTP status 204 (No Content) if removed.
     */
    @DeleteMapping("/{id}/books/{googleBookId}")
    @Transactional
    public ResponseEntity<Void> removeFavoriteBook(@PathVariable Long id, @PathVariable String googleBookId) {
        favoriteBookRepository.deleteByUserIdAndGoogleBookId(id, googleBookId);
        return ResponseEntity.noContent().build();
    }
}