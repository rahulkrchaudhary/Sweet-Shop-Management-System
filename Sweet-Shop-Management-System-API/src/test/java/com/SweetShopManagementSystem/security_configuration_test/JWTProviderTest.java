package com.SweetShopManagementSystem.security_configuration_test;

import com.SweetShopManagementSystem.security_configuration.JWTProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;

public class JWTProviderTest {
    private JWTProvider jwtProvider;

    @BeforeEach
    void setUp() {
        jwtProvider = new JWTProvider();
    }

    @Test
    void testGenerateTokenAndExtractEmail() {
        // Arrange
        var auth = new UsernamePasswordAuthenticationToken(
                "testuser@example.com",
                "password",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );

        // Act
        String token = jwtProvider.generateToken(auth);
        String email = jwtProvider.getEmailFromJwtToken("Bearer " + token);

        // Assert
        assertThat(token).isNotNull();
        assertThat(email).isEqualTo("testuser@example.com");
    }

    @Test
    void testTokenContainsRole() {
        var auth = new UsernamePasswordAuthenticationToken(
                "admin@example.com",
                "password",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"))
        );

        String token = jwtProvider.generateToken(auth);
        String authorities = jwtProvider.getAuthoritiesFromJwtToken("Bearer " + token);

        // Verify token is generated and contains the correct authority
        assertThat(token).isNotNull();
        assertThat(authorities).contains("ROLE_ADMIN");
    }
}
