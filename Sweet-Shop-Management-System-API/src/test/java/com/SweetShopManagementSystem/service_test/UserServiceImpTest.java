package com.SweetShopManagementSystem.service_test;

import com.SweetShopManagementSystem.dto.AuthResponse;
import com.SweetShopManagementSystem.dto.LoginRequest;
import com.SweetShopManagementSystem.model.USER_ROLE;
import com.SweetShopManagementSystem.model.User;
import com.SweetShopManagementSystem.repository.UserRepository;
import com.SweetShopManagementSystem.security_configuration.JWTProvider;
import com.SweetShopManagementSystem.service.service_impl.CustomerUserDetailsServiceImp;
import com.SweetShopManagementSystem.service.service_impl.UserServiceImp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImpTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JWTProvider jwtProvider;

    @Mock
    private CustomerUserDetailsServiceImp customerUserDetailsServiceImp;

    @InjectMocks
    private UserServiceImp userServiceImp;

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    // create User tests

    @Test
    void test_create_user_success() throws Exception {
        User input = new User();
        input.setEmail("test@example.com");
        input.setPassword("password");
        input.setName("Test User");
        input.setRole(USER_ROLE.CUSTOMER);

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        User savedFromRepo = new User();
        savedFromRepo.setId(1L);
        savedFromRepo.setEmail("test@example.com");
        savedFromRepo.setPassword("encodedPassword");
        savedFromRepo.setName("Test User");
        savedFromRepo.setRole(USER_ROLE.CUSTOMER);

        // repository.save should return the saved user
        when(userRepository.save(any(User.class))).thenReturn(savedFromRepo);
        when(jwtProvider.generateToken(any(Authentication.class))).thenReturn("jwt-token");

        AuthResponse resp = userServiceImp.registerUser(input);

        assertNotNull(resp);
        assertEquals("jwt-token", resp.getJwt());
        assertEquals("Register success", resp.getMessage());
        assertEquals(USER_ROLE.CUSTOMER, resp.getRole());

        // verify repository interactions
        verify(userRepository, times(1)).findByEmail("test@example.com");

        // capture the User passed to save() and check password was encoded
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(captor.capture());
        User captured = captor.getValue();
        assertEquals("encodedPassword", captured.getPassword());
        assertEquals("test@example.com", captured.getEmail());
        assertEquals(USER_ROLE.CUSTOMER, captured.getRole());

        verify(jwtProvider, times(1)).generateToken(any(Authentication.class));
    }

    @Test
    void test_create_user_emailAlreadyExists() {
        User input = new User();
        input.setEmail("duplicate@example.com");

        when(userRepository.findByEmail("duplicate@example.com")).thenReturn(Optional.of(new User()));

        Exception ex = assertThrows(Exception.class, () -> userServiceImp.registerUser(input));
        assertEquals("email is already used with another account", ex.getMessage());

        verify(userRepository, times(1)).findByEmail("duplicate@example.com");
        verify(userRepository, never()).save(any());
    }

    // ---------- login tests ----------

    @Test
    void test_login_success() throws Exception {
        String email = "test@example.com";
        String rawPassword = "password";

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(rawPassword);

        // build a Spring Security UserDetails with an authority that matches the enum name
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(email)
                .password("encodedPassword")
                .authorities(new SimpleGrantedAuthority("CUSTOMER"))
                .build();

        when(customerUserDetailsServiceImp.loadUserByUsername(email)).thenReturn(userDetails);
        when(passwordEncoder.matches(rawPassword, "encodedPassword")).thenReturn(true);
        when(jwtProvider.generateToken(any(Authentication.class))).thenReturn("jwt-token");

        AuthResponse resp = userServiceImp.login(loginRequest);

        assertNotNull(resp);
        assertEquals("jwt-token", resp.getJwt());
        assertEquals("Login success", resp.getMessage());
        assertEquals(USER_ROLE.CUSTOMER, resp.getRole());

        verify(customerUserDetailsServiceImp, times(1)).loadUserByUsername(email);
        verify(jwtProvider, times(1)).generateToken(any(Authentication.class));
    }

    @Test
    void test_login_invalid_user() {
        String email = "wrong@example.com";
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword("password");

        when(customerUserDetailsServiceImp.loadUserByUsername(email)).thenReturn(null);

        assertThrows(BadCredentialsException.class, () -> userServiceImp.login(loginRequest));
        verify(customerUserDetailsServiceImp, times(1)).loadUserByUsername(email);
    }

    @Test
    void test_login_invalid_password() {
        String email = "test@example.com";
        String rawPassword = "wrongPass";

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(rawPassword);

        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(email)
                .password("encodedPassword")
                .authorities(new SimpleGrantedAuthority("CUSTOMER"))
                .build();

        when(customerUserDetailsServiceImp.loadUserByUsername(email)).thenReturn(userDetails);
        when(passwordEncoder.matches(rawPassword, "encodedPassword")).thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> userServiceImp.login(loginRequest));
        verify(customerUserDetailsServiceImp, times(1)).loadUserByUsername(email);
    }
}
