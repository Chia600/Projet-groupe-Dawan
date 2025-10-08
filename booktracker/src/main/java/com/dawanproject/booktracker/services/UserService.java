package com.dawanproject.booktracker.services;

import com.dawanproject.booktracker.dtos.UserDto;
import com.dawanproject.booktracker.entities.Book;
import com.dawanproject.booktracker.entities.User;
import com.dawanproject.booktracker.mappers.UserMapper;
import com.dawanproject.booktracker.repositories.BookRepository;
import com.dawanproject.booktracker.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class for managing User entities and their book collections.
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, BookRepository bookRepository, 
                      PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    /**
     * Registers a new user with a hashed password.
     *
     * @param userDTO The user data to register.
     * @return The created UserDTO.
     * @throws IllegalArgumentException if the user data is invalid.
     */
    public UserDto registerUser(UserDto userDTO) {
        User user = userMapper.toEntity(userDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        return userMapper.toDTO(savedUser);
    }

    /**
     * Creates a new user with a hashed password.
     *
     * @param userDTO The user data to create.
     * @return The created UserDTO.
     */
    public UserDto createUser(UserDto userDTO) {
        User user = userMapper.toEntity(userDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        return userMapper.toDTO(savedUser);
    }

    /**
     * Retrieves all users.
     *
     * @return List of all UserDTOs.
     */
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id The ID of the user.
     * @return Optional containing the UserDTO if found, empty otherwise.
     */
    public Optional<UserDto> getUserById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toDTO);
    }

    /**
     * Updates an existing user.
     *
     * @param id The ID of the user to update.
     * @param userDTO The updated user data.
     * @return Optional containing the updated UserDTO if found, empty otherwise.
     */
    public Optional<UserDto> updateUser(Long id, UserDto userDTO) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            User user = userMapper.toEntity(userDTO);
            user.setUserId(id);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setBooks(existingUser.get().getBooks()); // Preserve favorite books
            User savedUser = userRepository.save(user);
            return Optional.of(userMapper.toDTO(savedUser));
        }
        return Optional.empty();
    }

    /**
     * Deletes a user by their ID.
     *
     * @param id The ID of the user to delete.
     * @return True if deleted, false if not found.
     */
    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Retrieves a user by their username.
     *
     * @param username The username of the user.
     * @return Optional containing the UserDTO if found, empty otherwise.
     */
    public Optional<UserDto> getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(userMapper::toDTO);
    }

    /**
     * Retrieves a user by their email.
     *
     * @param email The email of the user.
     * @return Optional containing the UserDTO if found, empty otherwise.
     */
    public Optional<UserDto> getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(userMapper::toDTO);
    }

    /**
     * Retrieves the collection of favorite books for a user.
     *
     * @param id The ID of the user.
     * @return Optional containing the list of book IDs if the user exists, empty otherwise.
     */
    public Optional<List<Long>> getFavoriteBooks(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            List<Long> bookIds = user.get().getBooks().stream()
                    .map(Book::getBookId)
                    .collect(Collectors.toList());
            return Optional.of(bookIds);
        }
        return Optional.empty();
    }

    /**
     * Adds a book to the user's collection of favorite books.
     *
     * @param id The ID of the user.
     * @param bookId The ID of the book to add.
     * @return True if added, false if the user or book does not exist.
     */
    public boolean addFavoriteBook(Long id, Long bookId) {
        Optional<User> user = userRepository.findById(id);
        Optional<Book> book = bookRepository.findById(bookId);
        if (user.isPresent() && book.isPresent()) {
            User u = user.get();
            if (!u.getBooks().contains(book.get())) {
                u.getBooks().add(book.get());
                userRepository.save(u);
            }
            return true;
        }
        return false;
    }

    /**
     * Removes a book from the user's collection of favorite books.
     *
     * @param id The ID of the user.
     * @param bookId The ID of the book to remove.
     * @return True if removed, false if the user or book does not exist.
     */
    public boolean removeFavoriteBook(Long id, Long bookId) {
        Optional<User> user = userRepository.findById(id);
        Optional<Book> book = bookRepository.findById(bookId);
        if (user.isPresent() && book.isPresent()) {
            User u = user.get();
            if (u.getBooks().remove(book.get())) {
                userRepository.save(u);
            }
            return true;
        }
        return false;
    }
}