package com.SweetShopManagementSystem.service_test;

import com.SweetShopManagementSystem.model.Category;
import com.SweetShopManagementSystem.model.Sweet;
import com.SweetShopManagementSystem.repository.CategoryRepository;
import com.SweetShopManagementSystem.repository.SweetRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SweetServiceImplTest {
    @Mock
    private SweetRepository sweetRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @InjectMocks
    private SweetServiceImpl sweetService; // the class you showed

    @Test
    void test_addSweet() {
        SweetRequest req = new SweetRequest();
        req.setName("Ladoo");

        Category traditionalCategory = new Category(null, "Traditional");
        Category savedCategory =categoryRepository.save(traditionalCategory);
        req.setCategory(savedCategory);

        req.setPrice(50.0);
        req.setQuantity(10);

        Sweet saved = new Sweet();
        saved.setId(1L);
        saved.setName("Ladoo");
        saved.setCategory(req.getCategory());
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
    void getAllSweets_returnsList() {
        Sweet s1 = new Sweet(); s1.setId(1L); s1.setName("Ladoo");
        Sweet s2 = new Sweet(); s2.setId(2L); s2.setName("Barfi");
        when(sweetRepository.findAll()).thenReturn(Arrays.asList(s1, s2));

        List<Sweet> all = sweetService.getAllSweets();
        assertEquals(2, all.size());
        verify(sweetRepository, times(1)).findAll();
    }

    @Test
    void searchSweets_byName_callsRepo() {
        Sweet s = new Sweet(); s.setName("Gulab Jamun");
        when(sweetRepository.findByNameContainingIgnoreCase("Jamun")).thenReturn(List.of(s));

        List<Sweet> r = sweetService.searchSweets("Jamun", null, 0, 0);
        assertEquals(1, r.size());
        assertEquals("Gulab Jamun", r.get(0).getName());
        verify(sweetRepository).findByNameContainingIgnoreCase("Jamun");
    }

    @Test
    void searchSweets_byPriceRange_callsRepo() {
        Sweet s = new Sweet(); s.setName("Barfi"); s.setPrice(100.0);
        when(sweetRepository.findByPriceBetween(50.0, 150.0)).thenReturn(List.of(s));

        List<Sweet> r = sweetService.searchSweets(null, null, 50.0, 150.0);
        assertEquals(1, r.size());
        verify(sweetRepository).findByPriceBetween(50.0, 150.0);
    }

    @Test
    void updateSweet_existing_returnsUpdated() {
        Long id = 5L;
        Sweet existing = new Sweet(); existing.setId(id); existing.setName("Old");
        when(sweetRepository.findById(id)).thenReturn(Optional.of(existing));

        SweetRequest req = new SweetRequest();
        req.setName("New");
        Category category = new Category(null, "Traditional");
        Category savedCategory = categoryRepository.save(category);
        req.setCategory(savedCategory);
        req.setPrice(80.0);
        req.setQuantity(5);

        Sweet saved = new Sweet();
        saved.setId(id);
        saved.setName("New");
        saved.setCategory(req.getCategory());
        saved.setPrice(80.0);

        when(sweetRepository.save(any(Sweet.class))).thenReturn(saved);

        Sweet result = sweetService.updateSweet(id, req);

        assertEquals("New", result.getName());
        verify(sweetRepository).findById(id);
        verify(sweetRepository).save(any(Sweet.class));
    }

    @Test
    void updateSweet_missing_throws() {
        when(sweetRepository.findById(99L)).thenReturn(Optional.empty());
        SweetRequest req = new SweetRequest();
        assertThrows(RuntimeException.class, () -> sweetService.updateSweet(99L, req));
    }

    @Test
    void deleteSweet_callsRepository() {
        Long id = 3L;
        doNothing().when(sweetRepository).deleteById(id);

        sweetService.deleteSweet(id);

        verify(sweetRepository).deleteById(id);
    }
}

