package com.dawanproject.booktracker.controller;

import com.dawanproject.booktracker.controllers.UserController;
import com.dawanproject.booktracker.dtos.UserDto;
import com.dawanproject.booktracker.entities.User;
import com.dawanproject.booktracker.mappers.UserMapper;
import com.dawanproject.booktracker.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for UserController.
 */
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private UserRepository userRepository;

    private BCryptPasswordEncoder passwordEncoder;

    private UserMapper userMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    /**
     * Tests the registration of a new user (public endpoint).
     *
     * @throws Exception if an error occurs during the test.
     */
    @Test
    void testRegisterUser_Success() throws Exception {
        UserDto userDTO = new UserDto(null, "testuser", "test@example.com", "secret", false, null);
        User user = new User(1L, "testuser", "test@example.com", "hashedPassword", null, null, false, null, null);
        UserDto responseDTO = new UserDto(1L, "testuser", "test@example.com", null, false, Collections.emptyList());

        when(passwordEncoder.encode("secret")).thenReturn("hashedPassword");
        when(userMapper.toEntity(userDTO)).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toDTO(user)).thenReturn(responseDTO);

        mockMvc.perform(post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    /**
     * Tests validation errors during user registration.
     *
     * @throws Exception if an error occurs during the test.
     */
    @Test
    void testRegisterUser_ValidationError() throws Exception {
        UserDto userDTO = new UserDto(null, "", "invalid", "", false, null);

        mockMvc.perform(post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.username").value("Le nom d'utilisateur est requis"))
                .andExpect(jsonPath("$.email").value("L'email doit être valide"))
                .andExpect(jsonPath("$.password").value("Le mot de passe est requis"));
    }

    /**
     * Tests the creation of a new user with authentication.
     *
     * @throws Exception if an error occurs during the test.
     */
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testCreateUser_Success() throws Exception {
        UserDto userDTO = new UserDto(null, "testuser", "test@example.com", "secret", false, null);
        User user = new User(1L, "testuser", "test@example.com", "hashedPassword", null, null, false, null, null);
        UserDto responseDTO = new UserDto(1L, "testuser", "test@example.com", null, false, Collections.emptyList());

        when(passwordEncoder.encode("secret")).thenReturn("hashedPassword");
        when(userMapper.toEntity(userDTO)).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toDTO(user)).thenReturn(responseDTO);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    /**
     * Tests retrieving all users with authentication.
     *
     * @throws Exception if an error occurs during the test.
     */
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testGetAllUsers_Success() throws Exception {
        User user1 = new User(1L, "user1", "user1@example.com", "hashedPassword", null, null, false, null, null);
        User user2 = new User(2L, "user2", "user2@example.com", "hashedPassword", null, null, false, null, null);
        UserDto userDTO1 = new UserDto(1L, "user1", "user1@example.com", null, false, Collections.emptyList());
        UserDto userDTO2 = new UserDto(2L, "user2", "user2@example.com", null, false, Collections.emptyList());

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));
        when(userMapper.toDTO(user1)).thenReturn(userDTO1);
        when(userMapper.toDTO(user2)).thenReturn(userDTO2);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value(1L))
                .andExpect(jsonPath("$[0].username").value("user1"))
                .andExpect(jsonPath("$[1].userId").value(2L))
                .andExpect(jsonPath("$[1].username").value("user2"));
    }

    /**
     * Tests retrieving a user by ID with authentication.
     *
     * @throws Exception if an error occurs during the test.
     */
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testGetUserById_Success() throws Exception {
        User user = new User(1L, "testuser", "test@example.com", "hashedPassword", null, null, false, null, null);
        UserDto userDTO = new UserDto(1L, "testuser", "test@example.com", null, false, Collections.emptyList());

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    /**
     * Tests retrieving a user by ID when the user does not exist.
     *
     * @throws Exception if an error occurs during the test.
     */
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testGetUserById_NotFound() throws Exception {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isNotFound());
    }

    /**
     * Tests updating an existing user with authentication.
     *
     * @throws Exception if an error occurs during the test.
     */
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testUpdateUser_Success() throws Exception {
        UserDto userDTO = new UserDto(null, "newuser", "new@example.com", "newpassword", false, null);
        User existingUser = new User(1L, "olduser", "old@example.com", "hashedPassword", null, null, false, null, null);
        User updatedUser = new User(1L, "newuser", "new@example.com", "hashedPassword", null, null, false, null, null);
        UserDto responseDTO = new UserDto(1L, "newuser", "new@example.com", null, false, Collections.emptyList());

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.encode("newpassword")).thenReturn("hashedPassword");
        when(userMapper.toEntity(userDTO)).thenReturn(updatedUser);
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);
        when(userMapper.toDTO(updatedUser)).thenReturn(responseDTO);

        mockMvc.perform(put("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.username").value("newuser"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    /**
     * Tests updating a user that does not exist.
     *
     * @throws Exception if an error occurs during the test.
     */
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testUpdateUser_NotFound() throws Exception {
        UserDto userDTO = new UserDto(null, "newuser", "new@example.com", "newpassword", false, null);

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isNotFound());
    }

    /**
     * Tests deleting an existing user with authentication.
     *
     * @throws Exception if an error occurs during the test.
     */
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testDeleteUser_Success() throws Exception {
        when(userRepository.existsById(1L)).thenReturn(true);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNoContent());
    }

    /**
     * Tests deleting a user that does not exist.
     *
     * @throws Exception if an error occurs during the test.
     */
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testDeleteUser_NotFound() throws Exception {
        when(userRepository.existsById(1L)).thenReturn(false);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNotFound());
    }

    /**
     * Tests retrieving a user by username with authentication.
     *
     * @throws Exception if an error occurs during the test.
     */
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testGetUserByUsername_Success() throws Exception {
        User user = new User(1L, "testuser", "test@example.com", "hashedPassword", null, null, false, null, null);
        UserDto userDTO = new UserDto(1L, "testuser", "test@example.com", null, false, Collections.emptyList());

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        mockMvc.perform(get("/users/username/testuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    /**
     * Tests retrieving a user by username when the user does not exist.
     *
     * @throws Exception if an error occurs during the test.
     */
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testGetUserByUsername_NotFound() throws Exception {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        mockMvc.perform(get("/users/username/testuser"))
                .andExpect(status().isNotFound());
    }

    /**
     * Tests retrieving a user by email with authentication.
     *
     * @throws Exception if an error occurs during the test.
     */
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testGetUserByEmail_Success() throws Exception {
        User user = new User(1L, "testuser", "test@example.com", "hashedPassword", null, null, false, null, null);
        UserDto userDTO = new UserDto(1L, "testuser", "test@example.com", null, false, Collections.emptyList());

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        mockMvc.perform(get("/users/email/test@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    /**
     * Tests retrieving a user by email when the user does not exist.
     *
     * @throws Exception if an error occurs during the test.
     */
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testGetUserByEmail_NotFound() throws Exception {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        mockMvc.perform(get("/users/email/test@example.com"))
                .andExpect(status().isNotFound());
    }
}