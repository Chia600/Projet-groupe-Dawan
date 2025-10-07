package com.dawanproject.booktracker.controller;

import com.dawanproject.booktracker.entities.User;
import com.dawanproject.booktracker.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
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

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Tests the registration of a new user (public endpoint).
     */
    @Test
    void testRegisterUser() throws Exception {
        User user = new User();
        user.setUserId(1L);
        user.setUsername("testuser");
        user.setEmail("test@exemple.com");
        user.setPassword("password");

        when(passwordEncoder.encode("secret")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@exemple.com"));
    }

    /**
     * tests validation errors during user registration.
     * @throws Exception if an error occurs during the test.
     */
    @Test
    void testRegisterUserValidationError() throws Exception {
        User user = new User();
        user.setUsername(""); // Invalid username
        user.setEmail("invalid-email"); // Invalid email
        user.setPassword(""); // Invalid password
        
        mockMvc.perform(post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }
    /**
     * Tests creating a new user (requires authentication).
     * @throws Exception if an error occurs during the test.
     */
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testCreateUser_Success() throws Exception {
        User user = new User();
        user.setUserId(1L);
        user.setUsername("newuser");
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");

        when(passwordEncoder.encode("secret")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.username").value("newuser"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }
    /**
     * Tests retrieving all users (requires authentication).
     * @throws Exception if an error occurs during the test.
     */
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testGetAllUsers_Success() throws Exception {
        User user1 = new User();
        user1.setUserId(1L);
        user1.setUsername("user1");
        User user2 = new User();
        user2.setUserId(2L);
        user2.setUsername("user2");

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].userId").value(1L))
                .andExpect(jsonPath("$[0].username").value("user1"))
                .andExpect(jsonPath("$[1].userId").value(2L))
                .andExpect(jsonPath("$[1].username").value("user2"));
    }
    /**
     * Tests retrieving a user by ID (requires authentication).
     * @throws Exception if an error occurs during the test.
     */
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testGetUserById_Success() throws Exception {
        User user = new User();
        user.setUserId(1L);
        user.setUsername("testuser");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    /**
     * Tests retrieving a user by ID when the user is not found (requires authentication).
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
     * Tests updating an existing user (requires authentication).
     * @throws Exception if an error occurs during the test.
     */
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testUpdateUser_Success() throws Exception {
        User existingUser = new User();
        existingUser.setUserId(1L);
        existingUser.setUsername("olduser");
        existingUser.setPassword("oldPassword");

        User updatedUser = new User();
        updatedUser.setUsername("updateduser");
        updatedUser.setPassword("newEncodedPassword");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.encode("newSecret")).thenReturn("newEncodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.username").value("updateduser"));
    }
    /**
     * Tests updating a user that does not exist (requires authentication).
     * @throws Exception if an error occurs during the test.
     */
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testUpdateUser_NotFound() throws Exception {
        User updatedUser = new User();
        updatedUser.setUsername("updateduser");
        updatedUser.setPassword("newEncodedPassword"); // Password will be encoded

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isNotFound());
    }

    /**
     * Tests deleting an existing user (requires authentication).
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
     * Tests deleting a user that does not exist (requires authentication).
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
     * Tests retrieving a user by username (requires authentication).
     * @throws Exception if an error occurs during the test.
     */
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testGetUserByUsername_Success() throws Exception {
        User user = new User();
        user.setUserId(1L);
        user.setUsername("testuser");
        
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        mockMvc.perform(get("/users/username/testuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    /**
     * Tests retrieving a user by username when the user is not found (requires authentication).
     * @throws Exception if an error occurs during the test.
     */
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testGetUserByUsername_NotFound() throws Exception {
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        mockMvc.perform(get("/users/username/nonexistent"))
                .andExpect(status().isNotFound());
    }

    /**
     * Test retrieving a user by email (requires authentication).
     * @throws Exception if an error occurs during the test.
     */
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testGetUserByEmail_Success() throws Exception {
        User user = new User();
        user.setUserId(1L);
        user.setEmail("test@example.com");
        
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        mockMvc.perform(get("/users/email/test@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    /**
     * Tests retrieving a user by email when the user is not found (requires authentication).
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
