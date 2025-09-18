package com.SweetShopManagementSystem.security_configuration_test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "fronted.url=http://localhost:3000",
        "jwt.secret=test-secret-test-secret-test-secret-long-enough-0123456789"
})
public class SecurityConfigurationTest {
    @Autowired
    MockMvc mockMvc;

    @Test
    void testAuthWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/api/auth/test"))
                .andExpect(status().isOk());
    }
}
