package com.dawanproject.booktracker.controller;

import com.dawanproject.booktracker.controllers.UserController;
import com.dawanproject.booktracker.dtos.UserDto;
import com.dawanproject.booktracker.security.SecurityConfig;
import com.dawanproject.booktracker.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for UserController.
 */
@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
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
    @WithAnonymousUser 
    void testRegisterUser_Success() throws Exception {
        UserDto userDTO = new UserDto(1L,"John","Doe", "testuser", "test@example.com", "secret", false, Collections.emptyList(), Collections.emptyList());
        UserDto responseDTO = new UserDto(1L,"John","Doe", "testuser", "test@example.com", null, false, Collections.emptyList(), Collections.emptyList());

        when(userService.registerUser(any(UserDto.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/users/register")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    @WithAnonymousUser 
    void testRegisterUser_ValidationError() throws Exception {
        // Données invalides pour tester la validation
        String invalidUserJson = "{\"username\":\"\",\"email\":\"invalid\",\"password\":\"\"}";

        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidUserJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"}) 
    void testCreateUser_Success() throws Exception {
        UserDto userDTO = new UserDto(1L,"John","Doe", "testuser", "test@example.com", "secret", false, Collections.emptyList(), Collections.emptyList());
        UserDto responseDTO = new UserDto(1L,"John","Doe", "testuser", "test@example.com", null, false, Collections.emptyList(), Collections.emptyList());

        when(userService.createUser(any(UserDto.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/users")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"}) 
    void testGetAllUsers_Success() throws Exception {
        UserDto userDTO1 = new UserDto(1L,"John","Doe", "user1", "user1@example.com", null, false, Collections.emptyList(), Collections.emptyList());
        UserDto userDTO2 = new UserDto(2L,"John","Doe", "user2", "user2@example.com", null, false, Collections.emptyList(), Collections.emptyList());

        when(userService.getAllUsers()).thenReturn(Arrays.asList(userDTO1, userDTO2));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value(1L))
                .andExpect(jsonPath("$[0].username").value("user1"))
                .andExpect(jsonPath("$[1].userId").value(2L))
                .andExpect(jsonPath("$[1].username").value("user2"));
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    void testGetUserById_Success() throws Exception {
        UserDto userDTO = new UserDto(1L,"John","Doe", "testuser", "test@example.com", null, false, Collections.emptyList(), Collections.emptyList());

        when(userService.getUserById(1L)).thenReturn(Optional.of(userDTO));

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    void testGetUserById_NotFound() throws Exception {
        when(userService.getUserById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "user", roles = {"ADMIN", "USER"})
    void testUpdateUser_Success() throws Exception {
        UserDto userDTO = new UserDto(1L,"John","Doe", "testuser", "new@example.com", "newpassword", false, Collections.emptyList(), Collections.emptyList());
        UserDto responseDTO = new UserDto(1L,"John","Doe", "testuser", "new@example.com", null, false, Collections.emptyList(), Collections.emptyList());

        when(userService.updateUser(anyLong(), any(UserDto.class))).thenReturn(Optional.of(responseDTO));

        mockMvc.perform(put("/api/users/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    void testUpdateUser_NotFound() throws Exception {
        UserDto userDTO = new UserDto(1L,"John","Doe", "newuser", "new@example.com", "newpassword", false, Collections.emptyList(), Collections.emptyList());

        when(userService.updateUser(anyLong(), any(UserDto.class))).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/users/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void testDeleteUser_Success() throws Exception {
        when(userService.deleteUser(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/users/1")
                .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"}) 
    void testDeleteUser_NotFound() throws Exception {
        when(userService.deleteUser(1L)).thenReturn(false);

        mockMvc.perform(delete("/api/users/1")
                .with(csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    void testGetUserByUsername_Success() throws Exception {
        UserDto userDTO = new UserDto(1L,"John","Doe", "testuser", "test@example.com", null, false, Collections.emptyList(), Collections.emptyList());

        when(userService.getUserByUsername("testuser")).thenReturn(Optional.of(userDTO));

        mockMvc.perform(get("/api/users/username/testuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    void testGetUserByUsername_NotFound() throws Exception {
        when(userService.getUserByUsername("testuser")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/username/testuser"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    void testGetUserByEmail_Success() throws Exception {
        UserDto userDTO = new UserDto(1L,"","", "testuser", "test@example.com", null, false, Collections.emptyList(), Collections.emptyList());

        when(userService.getUserByEmail("test@example.com")).thenReturn(Optional.of(userDTO));

        mockMvc.perform(get("/api/users/email/test@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    void testGetUserByEmail_NotFound() throws Exception {
        when(userService.getUserByEmail("test@example.com")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/email/test@example.com"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    void testGetFavoriteBooks_Success() throws Exception {
        List<Long> favoriteBooks = Arrays.asList(1L, 2L);
        when(userService.getFavoriteBooks(1L)).thenReturn(Optional.of(favoriteBooks));

        mockMvc.perform(get("/api/users/1/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value(1L))
                .andExpect(jsonPath("$[1]").value(2L));
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    void testGetFavoriteBooks_UserNotFound() throws Exception {
        when(userService.getFavoriteBooks(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/1/books"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    void testAddFavoriteBook_Success() throws Exception {
        when(userService.addFavoriteBook(1L, 1L)).thenReturn(true);

        mockMvc.perform(post("/api/users/1/books")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    void testAddFavoriteBook_NotFound() throws Exception {
        when(userService.addFavoriteBook(1L, 1L)).thenReturn(false);

        mockMvc.perform(post("/api/users/1/books")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    void testRemoveFavoriteBook_Success() throws Exception {
        when(userService.removeFavoriteBook(1L, 1L)).thenReturn(true);

        mockMvc.perform(delete("/api/users/1/books/1")
                .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    void testRemoveFavoriteBook_NotFound() throws Exception {
        when(userService.removeFavoriteBook(1L, 1L)).thenReturn(false);

        mockMvc.perform(delete("/api/users/1/books/1")
                .with(csrf()))
                .andExpect(status().isNotFound());
    }
}