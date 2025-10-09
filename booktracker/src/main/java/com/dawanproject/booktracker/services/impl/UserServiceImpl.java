package com.dawanproject.booktracker.services.impl;

import com.dawanproject.booktracker.dtos.UserDto;
import com.dawanproject.booktracker.entities.Book;
import com.dawanproject.booktracker.entities.User;
import com.dawanproject.booktracker.mappers.UserMapper;
import com.dawanproject.booktracker.repositories.BookRepository;
import com.dawanproject.booktracker.repositories.UserRepository;
import com.dawanproject.booktracker.services.UserService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of UserService for managing User entities and their book collections.
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, BookRepository bookRepository, 
                          PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @Override
    public UserDto registerUser(UserDto userDTO) {
        User user = userMapper.toEntity(userDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        return userMapper.toDTO(savedUser);
    }

    @Override
    public UserDto createUser(UserDto userDTO) {
        User user = userMapper.toEntity(userDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        return userMapper.toDTO(savedUser);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserDto> getUserById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toDTO);
    }

    @Override
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

    @Override
    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Optional<UserDto> getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(userMapper::toDTO);
    }

    @Override
    public Optional<UserDto> getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(userMapper::toDTO);
    }

    @Override
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

    @Override
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

    @Override
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