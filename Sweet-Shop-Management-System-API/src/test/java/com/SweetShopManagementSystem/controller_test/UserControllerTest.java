package com.SweetShopManagementSystem.controller_test;

import com.SweetShopManagementSystem.dto.AuthResponse;
import com.SweetShopManagementSystem.dto.LoginRequest;
import com.SweetShopManagementSystem.model.USER_ROLE;
import com.SweetShopManagementSystem.model.User;
import com.SweetShopManagementSystem.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void test_registerUser_success() throws Exception {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setName("Test User");
        user.setRole(USER_ROLE.CUSTOMER);

        AuthResponse mockResponse = new AuthResponse();
        mockResponse.setJwt("mock-jwt");
        mockResponse.setMessage("Register success");
        mockResponse.setRole(USER_ROLE.CUSTOMER);

        Mockito.when(userService.registerUser(any(User.class))).thenReturn(mockResponse);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.jwt").value("mock-jwt"))
                .andExpect(jsonPath("$.message").value("Register success"))
                .andExpect(jsonPath("$.role").value("CUSTOMER"));
    }

    @Test
    void test_login_success() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password");

        AuthResponse mockResponse = new AuthResponse();
        mockResponse.setJwt("mock-jwt");
        mockResponse.setMessage("Login success");
        mockResponse.setRole(USER_ROLE.CUSTOMER);

        Mockito.when(userService.login(any(LoginRequest.class))).thenReturn(mockResponse);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.jwt").value("mock-jwt"))
                .andExpect(jsonPath("$.message").value("Login success"))
                .andExpect(jsonPath("$.role").value("CUSTOMER"));
    }
}

