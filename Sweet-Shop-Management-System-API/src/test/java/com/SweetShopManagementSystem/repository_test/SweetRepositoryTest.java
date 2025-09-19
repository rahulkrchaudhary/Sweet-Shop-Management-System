package com.SweetShopManagementSystem.repository_test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class SweetRepositoryTest {

    @Autowired
    private SweetRepository sweetRepository;

    @Test
    @DisplayName("findByNameContainingIgnoreCase should return matching sweets")
    void findByNameContainingIgnoreCase_returnsMatchingSweets() {
        Sweet s1 = new Sweet(null, "Ladoo", "Traditional", 50.0, "Tasty ladoo");
        Sweet s2 = new Sweet(null, "Gulab Jamun", "Traditional", 70.0, "Soft sweet");
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
    void findByCategoryIgnoreCase_returnsMatchingSweets() {
        Sweet s1 = new Sweet(null, "Ladoo", "Traditional", 50.0, "Tasty ladoo");
        Sweet s2 = new Sweet(null, "Chocolate", "Modern", 120.0, "Choco");
        sweetRepository.save(s1);
        sweetRepository.save(s2);

        List<Sweet> result = sweetRepository.findByCategoryIgnoreCase("traditional");
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCategory()).isEqualTo("Traditional");
    }

    @Test
    @DisplayName("findByPriceBetween should return sweets within the given range")
    void findByPriceBetween_returnsMatchingSweets() {
        Sweet s1 = new Sweet(null, "Ladoo", "Traditional", 50.0, "Tasty");
        Sweet s2 = new Sweet(null, "Barfi", "Milk", 100.0, "Sweet");
        Sweet s3 = new Sweet(null, "Premium", "Luxury", 500.0, "Expensive");
        sweetRepository.save(s1);
        sweetRepository.save(s2);
        sweetRepository.save(s3);

        List<Sweet> result = sweetRepository.findByPriceBetween(30.0, 150.0);
        assertThat(result).hasSize(2).extracting("name").containsExactlyInAnyOrder("Ladoo", "Barfi");
    }

    @Test
    @DisplayName("no results returns empty list")
    void noResults_returnsEmptyList() {
        List<Sweet> result = sweetRepository.findByNameContainingIgnoreCase("does-not-exist");
        assertThat(result).isEmpty();
    }
}
