package com.SweetShopManagementSystem.repository;

import com.SweetShopManagementSystem.model.Category;
import com.SweetShopManagementSystem.model.Sweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SweetRepository  extends JpaRepository<Sweet, Long> {
    List<Sweet> findByNameContainingIgnoreCase(String name);

    List<Sweet> findByCategory(Category category);

    List<Sweet> findByPriceBetween(double min, double max);

    @Query("SELECT s FROM Sweet s " +
            "WHERE (:name IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "AND (:category IS NULL OR s.category = :category) " +
            "AND (:minPrice IS NULL OR :maxPrice IS NULL OR s.price BETWEEN :minPrice AND :maxPrice)")
    List<Sweet> searchSweets(@Param("name") String name,
                             @Param("category") Category category,
                             @Param("minPrice") Double minPrice,
                             @Param("maxPrice") Double maxPrice);


}