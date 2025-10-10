package com.dawanproject.booktracker.services;

import com.dawanproject.booktracker.dtos.UserDto;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing User entities and their book collections.
 */
public interface UserService {

    /**
     * Registers a new user with a hashed password.
     *
     * @param userDTO The user data to register.
     * @return The created UserDTO.
     * @throws IllegalArgumentException if the user data is invalid.
     */
    UserDto registerUser(UserDto userDTO);

    /**
     * Creates a new user with a hashed password.
     *
     * @param userDTO The user data to create.
     * @return The created UserDTO.
     */
    UserDto createUser(UserDto userDTO);

    /**
     * Retrieves all users.
     *
     * @return List of all UserDTOs.
     */
    List<UserDto> getAllUsers();

    /**
     * Retrieves a user by their ID.
     *
     * @param id The ID of the user.
     * @return Optional containing the UserDTO if found, empty otherwise.
     */
    Optional<UserDto> getUserById(Long id);

    /**
     * Updates an existing user.
     *
     * @param id      The ID of the user to update.
     * @param userDTO The updated user data.
     * @return Optional containing the updated UserDTO if found, empty otherwise.
     */
    Optional<UserDto> updateUser(Long id, UserDto userDTO);

    /**
     * Deletes a user by their ID.
     *
     * @param id The ID of the user to delete.
     * @return True if deleted, false if not found.
     */
    boolean deleteUser(Long id);

    /**
     * Retrieves a user by their username.
     *
     * @param username The username of the user.
     * @return Optional containing the UserDTO if found, empty otherwise.
     */
    Optional<UserDto> getUserByUsername(String username);

    /**
     * Retrieves a user by their email.
     *
     * @param email The email of the user.
     * @return Optional containing the UserDTO if found, empty otherwise.
     */
    Optional<UserDto> getUserByEmail(String email);

    /**
     * Retrieves the collection of favorite books for a user.
     *
     * @param id The ID of the user.
     * @return Optional containing the list of book IDs if the user exists, empty otherwise.
     */
    Optional<List<Long>> getFavoriteBooks(Long id);

    /**
     * Adds a book to the user's collection of favorite books.
     *
     * @param id     The ID of the user.
     * @param bookId The ID of the book to add.
     * @return True if added, false if the user or book does not exist.
     */
    boolean addFavoriteBook(Long id, Long bookId);

    /**
     * Removes a book from the user's collection of favorite books.
     *
     * @param id     The ID of the user.
     * @param bookId The ID of the book to remove.
     * @return True if removed, false if the user or book does not exist.
     */
    boolean removeFavoriteBook(Long id, Long bookId);
}