package com.dawanproject.booktracker.controller;

import com.dawanproject.booktracker.controllers.UserController;
import com.dawanproject.booktracker.dtos.UserDto;
import com.dawanproject.booktracker.entities.Book;
import com.dawanproject.booktracker.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

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

    private UserService userService;

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

    @Test
    void testRegisterUser_Success() throws Exception {
        UserDto userDTO = new UserDto(null, "testuser", "test@example.com", null, false, null);
        UserDto responseDTO = new UserDto(1L, "testuser", "test@example.com", null, false, null);

        when(userService.registerUser(any(UserDto.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    void testRegisterUser_ValidationError() throws Exception {
        UserDto userDTO = new UserDto(null, "", "invalid-email", null, false, null);

        mockMvc.perform(post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.username").value("Le nom d'utilisateur est requis"))
                .andExpect(jsonPath("$.email").value("L'email doit être valide"))
                .andExpect(jsonPath("$.password").value("Le mot de passe est requis"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testCreateUser_Success() throws Exception {
        UserDto userDTO = new UserDto(null, "testuser", "test@example.com", null, false, null);
        UserDto responseDTO = new UserDto(1L, "testuser", "test@example.com", null, false, null);

        when(userService.createUser(any(UserDto.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testGetAllUsers_Success() throws Exception {
        UserDto userDTO1 = new UserDto(1L, "user1", "user1@example.com", null, false, null);
        UserDto userDTO2 = new UserDto(2L, "user2", "user2@example.com", null, false, null);

        when(userService.getAllUsers()).thenReturn(Arrays.asList(userDTO1, userDTO2));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value(1L))
                .andExpect(jsonPath("$[0].username").value("user1"))
                .andExpect(jsonPath("$[1].userId").value(2L))
                .andExpect(jsonPath("$[1].username").value("user2"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testGetUserById_Success() throws Exception {
        UserDto userDTO = new UserDto(1L, "testuser", "test@example.com", null, false, null);

        when(userService.getUserById(1L)).thenReturn(Optional.of(userDTO));

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testGetUserById_NotFound() throws Exception {
        when(userService.getUserById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testUpdateUser_Success() throws Exception {
        UserDto userDTO = new UserDto(null, "newuser", "new@example.com", null, false, null);
        UserDto responseDTO = new UserDto(1L, "newuser", "new@example.com", null, false, null);

        when(userService.updateUser(1L, any(UserDto.class))).thenReturn(Optional.of(responseDTO));

        mockMvc.perform(put("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.username").value("newuser"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testUpdateUser_NotFound() throws Exception {
        UserDto userDTO = new UserDto(null, "newuser", "new@example.com", null, false, null);

        when(userService.updateUser(1L, any(UserDto.class))).thenReturn(Optional.empty());

        mockMvc.perform(put("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testDeleteUser_Success() throws Exception {
        when(userService.deleteUser(1L)).thenReturn(true);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testDeleteUser_NotFound() throws Exception {
        when(userService.deleteUser(1L)).thenReturn(false);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testGetUserByUsername_Success() throws Exception {
        UserDto userDTO = new UserDto(1L, "testuser", "test@example.com", null, false, null);

        when(userService.getUserByUsername("testuser")).thenReturn(Optional.of(userDTO));

        mockMvc.perform(get("/users/username/testuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testGetUserByUsername_NotFound() throws Exception {
        when(userService.getUserByUsername("testuser")).thenReturn(Optional.empty());

        mockMvc.perform(get("/users/username/testuser"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testGetUserByEmail_Success() throws Exception {
        UserDto userDTO = new UserDto(1L, "testuser", "test@example.com", null, false, null);

        when(userService.getUserByEmail("test@example.com")).thenReturn(Optional.of(userDTO));

        mockMvc.perform(get("/users/email/test@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testGetUserByEmail_NotFound() throws Exception {
        when(userService.getUserByEmail("test@example.com")).thenReturn(Optional.empty());

        mockMvc.perform(get("/users/email/test@example.com"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testGetFavoriteBooks_Success() throws Exception {
        when(userService.getFavoriteBooks(1L)).thenReturn(Optional.of(Arrays.asList(1L, 2L)));

        mockMvc.perform(get("/users/1/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value(1L))
                .andExpect(jsonPath("$[1]").value(2L));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testGetFavoriteBooks_UserNotFound() throws Exception {
        when(userService.getFavoriteBooks(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/users/1/books"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testAddFavoriteBook_Success() throws Exception {
        when(userService.addFavoriteBook(1L, 1L)).thenReturn(true);

        mockMvc.perform(post("/users/1/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content("1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testAddFavoriteBook_NotFound() throws Exception {
        when(userService.addFavoriteBook(1L, 1L)).thenReturn(false);

        mockMvc.perform(post("/users/1/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content("1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testRemoveFavoriteBook_Success() throws Exception {
        when(userService.removeFavoriteBook(1L, 1L)).thenReturn(true);

        mockMvc.perform(delete("/users/1/books/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testRemoveFavoriteBook_NotFound() throws Exception {
        when(userService.removeFavoriteBook(1L, 1L)).thenReturn(false);

        mockMvc.perform(delete("/users/1/books/1"))
                .andExpect(status().isNotFound());
    }
}