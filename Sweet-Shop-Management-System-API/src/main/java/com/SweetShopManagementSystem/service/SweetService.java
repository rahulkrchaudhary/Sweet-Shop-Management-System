package com.SweetShopManagementSystem.service;

import com.SweetShopManagementSystem.dto.SweetRequest;
import com.SweetShopManagementSystem.model.Category;
import com.SweetShopManagementSystem.model.Sweet;

import java.util.List;

public interface SweetService {
    Sweet addSweet(SweetRequest sweetRequest);
    List<Sweet> getAllSweets();
    List<Sweet> searchSweets(String name, String category, Double minPrice, Double maxPrice);
    Sweet updateSweet(Long id, SweetRequest sweetRequest);
    void deleteSweet(Long id);
}