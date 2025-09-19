package com.SweetShopManagementSystem.controller_test;

import com.SweetShopManagementSystem.dto.SweetRequest;
import com.SweetShopManagementSystem.model.Category;
import com.SweetShopManagementSystem.model.Sweet;
import com.SweetShopManagementSystem.service.SweetService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static java.util.Collections.singletonList;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SweetController.class)
class SweetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SweetService sweetService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /api/sweets - create sweet (201)")
    @WithMockUser
        // any authenticated user
    void test_addSweet() throws Exception {
        SweetRequest req = new SweetRequest();
        req.setName("Gulab Jamun");
        req.setCategory("Traditional");
        req.setPrice(70.0);
        req.setQuantity(20);

        Sweet saved = new Sweet();
        saved.setId(1L);
        saved.setName("Gulab Jamun");
        Category category = new Category(null, "Traditional");
        saved.setCategory(category);
        saved.setPrice(70.0);
        saved.setQuantity(20);

        when(sweetService.addSweet(any(SweetRequest.class))).thenReturn(saved);

        mockMvc.perform(post("/api/sweets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Gulab Jamun"));
    }

    @Test
    @DisplayName("GET /api/sweets - get all sweets (200)")
    void test_getAllSweets() throws Exception {
        Sweet s = new Sweet();
        s.setId(1L);
        s.setName("Barfi");
        when(sweetService.getAllSweets()).thenReturn(singletonList(s));

        mockMvc.perform(get("/api/sweets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Barfi"));
    }

    @Test
    @DisplayName("GET /api/sweets/search - search sweets (200)")
    void test_searchSweets() throws Exception {
        Sweet s = new Sweet();
        s.setId(2L);
        s.setName("Kaju Katli");
        when(sweetService.searchSweets(eq("Kaju"), eq("Traditional"), eq(50.0), eq(150.0)))
                .thenReturn(singletonList(s));

        mockMvc.perform(get("/api/sweets/search")
                        .param("name", "Kaju")
                        .param("category", "Traditional")
                        .param("minPrice", "50.0")
                        .param("maxPrice", "150.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(2))
                .andExpect(jsonPath("$[0].name").value("Kaju Katli"));
    }

    @Test
    @DisplayName("PUT /api/sweets/{id} - update sweet (200)")
    @WithMockUser
    void test_updateSweet() throws Exception {
        Long id = 10L;
        SweetRequest req = new SweetRequest();
        req.setName("New Name");
        req.setCategory("Modern");
        req.setPrice(120.0);
        req.setQuantity(5);

        Sweet updated = new Sweet();
        updated.setId(id);
        updated.setName("New Name");
        updated.setCategory("Modern");
        updated.setPrice(120.0);
        updated.setQuantity(5);

        when(sweetService.updateSweet(eq(id), any(SweetRequest.class))).thenReturn(updated);

        mockMvc.perform(put("/api/sweets/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("New Name"));
    }

    @Test
    @DisplayName("DELETE /api/sweets/{id} - admin allowed (204)")
    @WithMockUser(roles = {"ADMIN"})
    void test_deleteSweet_asAdmin() throws Exception {
        Long id = 5L;
        doNothing().when(sweetService).deleteSweet(id);

        mockMvc.perform(delete("/api/sweets/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/sweets/{id} - non-admin forbidden (403)")
    @WithMockUser(roles = {"USER"})
    void test_deleteSweet_asUser() throws Exception {
        Long id = 6L;
        // no need to stub service; controller should block before calling service
        mockMvc.perform(delete("/api/sweets/{id}", id))
                .andExpect(status().isForbidden());
        // ensure service.deleteSweet was never called
        Mockito.verify(sweetService, Mockito.never()).deleteSweet(anyLong());
    }
}
