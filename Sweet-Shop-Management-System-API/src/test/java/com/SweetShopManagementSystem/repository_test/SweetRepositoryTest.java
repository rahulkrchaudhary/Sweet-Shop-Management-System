package com.SweetShopManagementSystem.repository_test;

import com.SweetShopManagementSystem.model.Category;
import com.SweetShopManagementSystem.model.Sweet;
import com.SweetShopManagementSystem.repository.CategoryRepository;
import com.SweetShopManagementSystem.repository.SweetRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class SweetRepositoryTest {

    @Autowired
    private SweetRepository sweetRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("findByNameContainingIgnoreCase should return matching sweets")
    void test_findByNameContainingIgnoreCase() {
        Category traditionalCategory = new Category(null, "Traditional");
        categoryRepository.save(traditionalCategory);


        Sweet s1 = new Sweet(null, "Ladoo", traditionalCategory, 50.0, 5);
        Sweet s2 = new Sweet(null, "Gulab Jamun", traditionalCategory, 70.0, 6);

        sweetRepository.save(s1);
        sweetRepository.save(s2);

        List<Sweet> result1 = sweetRepository.findByNameContainingIgnoreCase("ladoo");
        assertThat(result1).isNotEmpty();
        assertThat(result1.get(0).getName()).isEqualTo("Ladoo");

        List<Sweet> result2 = sweetRepository.findByNameContainingIgnoreCase("JAMUN");
        assertThat(result2).isNotEmpty();
        assertThat(result2.get(0).getName()).isEqualTo("Gulab Jamun");
    }

    @Test
    @DisplayName("findByCategoryIgnoreCase should return sweets of a category (case-insensitive)")
    void test_findByCategory() {
        Category traditionalCategory = new Category(null, "Traditional");
        categoryRepository.save(traditionalCategory);

        Category modernCategory = new Category(null, "Modern");
        categoryRepository.save(modernCategory);

        Sweet s1 = new Sweet(null, "Ladoo",  traditionalCategory, 50.0, 6);
        Sweet s2 = new Sweet(null, "Chocolate", modernCategory, 120.0, 5);
        sweetRepository.save(s1);
        sweetRepository.save(s2);

        List<Sweet> result = sweetRepository.findByCategory(traditionalCategory);
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCategory()).isEqualTo(traditionalCategory);
    }

    @Test
    @DisplayName("findByPriceBetween should return sweets within the given range")
    void test_findByPriceBetweenGivenPrice() {
        Category traditionalCategory = new Category(null, "Traditional");
        categoryRepository.save(traditionalCategory);
        Category milkCategory = new Category(null, "Milk");
        categoryRepository.save(milkCategory);
        Category luxuryCategory = new Category(null, "Luxury");
        categoryRepository.save(luxuryCategory);

        Sweet s1 = new Sweet(null, "Ladoo", traditionalCategory, 50.0, 4);
        Sweet s2 = new Sweet(null, "Barfi", milkCategory, 100.0, 5);
        Sweet s3 = new Sweet(null, "Premium", luxuryCategory, 500.0, 6);
        sweetRepository.save(s1);
        sweetRepository.save(s2);
        sweetRepository.save(s3);

        List<Sweet> result = sweetRepository.findByPriceBetween(30.0, 150.0);
        assertThat(result).hasSize(2).extracting("name").containsExactlyInAnyOrder("Ladoo", "Barfi");
    }

    @Test
    void testSearchByNameAndCategoryAndPriceRange() {
        Category category = new Category();
        category.setName("Traditional");

        Sweet sweet1 = new Sweet(null, "Kaju Katli", category, 100.0, 10);
        Sweet sweet2 = new Sweet(null, "Gulab Jamun", category, 50.0, 20);

        entityManager.persist(category);
        entityManager.persist(sweet1);
        entityManager.persist(sweet2);
        entityManager.flush();

        List<Sweet> results = sweetRepository.searchSweets("Kaju", category, 80.0, 120.0);

        assertEquals(1, results.size());
        assertEquals("Kaju Katli", results.get(0).getName());
    }

    @Test
    @DisplayName("no results returns empty list")
    void test_noResults() {
        List<Sweet> result = sweetRepository.findByNameContainingIgnoreCase("does-not-exist");
        assertThat(result).isEmpty();
    }
}
