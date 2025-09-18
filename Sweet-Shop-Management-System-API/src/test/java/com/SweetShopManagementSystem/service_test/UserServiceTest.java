package com.SweetShopManagementSystem.service_test;

import com.SweetShopManagementSystem.model.USER_ROLE;
import com.SweetShopManagementSystem.model.User;
//import com.SweetShopManagementSystem.payload.request.LoginRequest;
//import com.SweetShopManagementSystem.payload.response.AuthResponse;
import com.SweetShopManagementSystem.repository.UserRepository;
//import com.SweetShopManagementSystem.security_configuration.CustomerUserDetailsService;
import com.SweetShopManagementSystem.security_configuration.JWTProvider;
//import com.SweetShopManagementSystem.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JWTProvider jwtProvider;

    @Mock
    private CustomerUserDetailsService customerUserDetailsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userRepository, passwordEncoder, jwtProvider, customerUserDetailsService);
    }

    // Register Test
    @Test
    void testCreateUserSuccess() throws Exception {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setName("Test User");
        user.setRole(USER_ROLE.CUSTOMER);

        when(userRepository.findByEmail("test@example.com")).thenReturn(null);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setEmail("test@example.com");
        savedUser.setPassword("encodedPassword");
        savedUser.setName("Test User");
        savedUser.setRole(USER_ROLE.CUSTOMER);

        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(jwtProvider.generateToken(any(Authentication.class))).thenReturn("jwt-token");

        ResponseEntity<AuthResponse> response = userService.createUserHandler(user);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals("jwt-token", response.getBody().getJwt());
        assertEquals("Register success", response.getBody().getMessage());
        assertEquals(USER_ROLE.CUSTOMER, response.getBody().getRole());

        verify(userRepository, times(1)).findByEmail("test@example.com");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testCreateUserEmailAlreadyExists() {
        User user = new User();
        user.setEmail("duplicate@example.com");

        when(userRepository.findByEmail("duplicate@example.com")).thenReturn(new User());

        Exception exception = assertThrows(Exception.class, () -> userService.createUserHandler(user));

        assertEquals("email is alaready used with another account", exception.getMessage());
    }

    // Login
    @Test
    void testSigninSuccess() {
        LoginRequest loginRequest = new LoginRequest("test@example.com", "password");

        UserDetails userDetails = org.springframework.security.core.userdetails.User.withUsername("test@example.com")
                .password("encodedPassword")
                .authorities("CUSTOMER")
                .build();

        when(customerUserDetailsService.loadUserByUsername("test@example.com")).thenReturn(userDetails);
        when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(true);
        when(jwtProvider.generateToken(any(Authentication.class))).thenReturn("jwt-token");

        ResponseEntity<AuthResponse> response = userService.signin(loginRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("jwt-token", response.getBody().getJwt());
        assertEquals("Login success", response.getBody().getMessage());
        assertEquals(USER_ROLE.CUSTOMER, response.getBody().getRole());
    }

    @Test
    void testSigninInvalidUser() {
        LoginRequest loginRequest = new LoginRequest("wrong@example.com", "password");
        when(customerUserDetailsService.loadUserByUsername("wrong@example.com")).thenReturn(null);

        assertThrows(BadCredentialsException.class, () -> userService.signin(loginRequest));
    }

    @Test
    void testSigninInvalidPassword() {
        LoginRequest loginRequest = new LoginRequest("test@example.com", "wrongPass");

        UserDetails userDetails = org.springframework.security.core.userdetails.User.withUsername("test@example.com")
                .password("encodedPassword")
                .authorities("CUSTOMER")
                .build();

        when(customerUserDetailsService.loadUserByUsername("test@example.com")).thenReturn(userDetails);
        when(passwordEncoder.matches("wrongPass", "encodedPassword")).thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> userService.signin(loginRequest));
    }
}
