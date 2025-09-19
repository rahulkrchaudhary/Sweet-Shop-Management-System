package com.SweetShopManagementSystem.repository;

import com.SweetShopManagementSystem.model.Category;
import com.SweetShopManagementSystem.model.Sweet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SweetRepository  extends JpaRepository<Sweet, Long> {
    List<Sweet> findByNameContainingIgnoreCase(String name);

    List<Sweet> findByCategory(Category category);

    List<Sweet> findByPriceBetween(double min, double max);

}