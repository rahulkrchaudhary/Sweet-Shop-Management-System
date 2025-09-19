package com.SweetShopManagementSystem.service_test;

import com.SweetShopManagementSystem.dto.SweetRequest;
import com.SweetShopManagementSystem.model.Category;
import com.SweetShopManagementSystem.model.Sweet;
import com.SweetShopManagementSystem.repository.CategoryRepository;
import com.SweetShopManagementSystem.repository.SweetRepository;
import com.SweetShopManagementSystem.service.service_impl.SweetServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SweetServiceImplTest {
    @Mock
    private SweetRepository sweetRepository;
    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private SweetServiceImpl sweetService; // the class you showed

    @Test
    void test_addSweet() {
        SweetRequest req = new SweetRequest();
        req.setName("Ladoo");
        req.setCategory("Traditional");
        req.setPrice(50.0);
        req.setQuantity(10);

        Sweet saved = new Sweet();
        saved.setId(1L);
        saved.setName("Ladoo");
        Category category = new Category();
        category.setName("Traditional");
        Category savedCategory = categoryRepository.save(category);
        saved.setCategory(savedCategory);
        saved.setPrice(50.0);
        saved.setQuantity(10);

        when(sweetRepository.save(any(Sweet.class))).thenReturn(saved);

        Sweet result = sweetService.addSweet(req);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Ladoo", result.getName());

        ArgumentCaptor<Sweet> captor = ArgumentCaptor.forClass(Sweet.class);
        verify(sweetRepository, times(1)).save(captor.capture());
        Sweet passed = captor.getValue();
        assertEquals("Ladoo", passed.getName());
        assertEquals(50.0, passed.getPrice());
    }

    @Test
    void test_getAllSweets() {
        Sweet s1 = new Sweet(); s1.setId(1L); s1.setName("Ladoo");
        Sweet s2 = new Sweet(); s2.setId(2L); s2.setName("Barfi");
        when(sweetRepository.findAll()).thenReturn(Arrays.asList(s1, s2));

        List<Sweet> all = sweetService.getAllSweets();
        assertEquals(2, all.size());
        verify(sweetRepository, times(1)).findAll();
    }

    @Test
    void test_searchSweetsByNameCategoryAndPrice() {
        Category category = new Category(1L, "Traditional");
        Sweet sweet = new Sweet(1L, "Kaju Katli", category, 100.0, 10);

        when(categoryRepository.findByName("Traditional")).thenReturn(Optional.of(category));
        when(sweetRepository.searchSweets("Kaju", category, 80.0, 120.0))
                .thenReturn(List.of(sweet));

        List<Sweet> results = sweetService.searchSweets("Kaju", "Traditional", 80.0, 120.0);

        assertEquals(1, results.size());
        assertEquals("Kaju Katli", results.get(0).getName());
    }

    @Test
    void test_updateSweet() {
        Long id = 5L;
        Sweet existing = new Sweet();
        existing.setId(id);
        existing.setName("Old");
        existing.setCategory(new Category(1L, "OldCat"));

        when(sweetRepository.findById(id)).thenReturn(Optional.of(existing));

        SweetRequest req = new SweetRequest();
        req.setName("New");
        req.setCategory("Traditional");
        req.setPrice(80.0);
        req.setQuantity(5);

        Category savedCategory = new Category(2L, "TRADITIONAL");
        when(categoryRepository.findByName("TRADITIONAL")).thenReturn(Optional.of(savedCategory));

        Sweet saved = new Sweet();
        saved.setId(id);
        saved.setName("New");
        saved.setCategory(savedCategory);
        saved.setPrice(80.0);
        saved.setQuantity(5);

        when(sweetRepository.save(any(Sweet.class))).thenReturn(saved);

        Sweet result = sweetService.updateSweet(id, req);

        assertEquals("New", result.getName());
        assertEquals("TRADITIONAL", result.getCategory().getName());
        assertEquals(80.0, result.getPrice());
        assertEquals(5, result.getQuantity());

        verify(sweetRepository).findById(id);
        verify(sweetRepository).save(any(Sweet.class));
    }


    @Test
    void test_updateSweet_missing() {
        when(sweetRepository.findById(99L)).thenReturn(Optional.empty());
        SweetRequest req = new SweetRequest();
        assertThrows(RuntimeException.class, () -> sweetService.updateSweet(99L, req));
    }

    @Test
    void test_deleteSweet() {
        Long id = 3L;
        doNothing().when(sweetRepository).deleteById(id);

        sweetService.deleteSweet(id);

        verify(sweetRepository).deleteById(id);
    }
}

