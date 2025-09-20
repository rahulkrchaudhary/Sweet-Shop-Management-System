package com.SweetShopManagementSystem.service.service_impl;

import com.SweetShopManagementSystem.dto.SweetRequest;
import com.SweetShopManagementSystem.exception.InsufficientQuantityException;
import com.SweetShopManagementSystem.exception.SweetNotFoundException;
import com.SweetShopManagementSystem.model.Category;
import com.SweetShopManagementSystem.model.Sweet;
import com.SweetShopManagementSystem.repository.CategoryRepository;
import com.SweetShopManagementSystem.repository.SweetRepository;
import com.SweetShopManagementSystem.service.SweetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SweetServiceImpl implements SweetService {

    @Autowired
    SweetRepository sweetRepository;
    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public Sweet addSweet(SweetRequest sweetRequest) {
        Sweet sweet = new Sweet();
        sweet.setName(sweetRequest.getName());

        Optional<Category> category = categoryRepository.findByName(sweetRequest.getCategory().toUpperCase());
        if(category.isEmpty()){
            Category newCategory = new Category();
            newCategory.setName(sweetRequest.getCategory().toUpperCase());
            Category savedCategory = categoryRepository.save(newCategory);
            sweet.setCategory(savedCategory);
        }else{
            sweet.setCategory(category.get());
        }

        sweet.setPrice(sweetRequest.getPrice());
        sweet.setQuantity(sweetRequest.getQuantity());

        return sweetRepository.save(sweet);
    }

    @Override
    public List<Sweet> getAllSweets() {
        return sweetRepository.findAll();
    }

    @Override
    public List<Sweet> searchSweets(String name, String categoryName, Double minPrice, Double maxPrice) {
        Category searchCategory = null;
        if (categoryName != null) {
            searchCategory = categoryRepository.findByName(categoryName)
                    .orElse(null);
        }
        return sweetRepository.searchSweets(name, searchCategory, minPrice, maxPrice);
    }



    @Override
    public Sweet updateSweet(Long id, SweetRequest sweetRequest) {
        Sweet existing = sweetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sweet not found"));
        if(existing.getName()==null || !existing.getName().equals(sweetRequest.getName())){
            existing.setName(sweetRequest.getName());
        }
        if (existing.getCategory() == null || !existing.getCategory().getName().equals(sweetRequest.getCategory().toUpperCase())) {

            Optional<Category> category = categoryRepository.findByName(sweetRequest.getCategory().toUpperCase());

            if (category.isEmpty()) {
                Category newCategory = new Category();
                newCategory.setName(sweetRequest.getCategory().toUpperCase());
                Category savedCategory = categoryRepository.save(newCategory);
                existing.setCategory(savedCategory);
            } else {
                existing.setCategory(category.get());
            }
        }

        existing.setPrice(sweetRequest.getPrice());
        existing.setQuantity(sweetRequest.getQuantity());
        return sweetRepository.save(existing);
    }

    @Override
    public void deleteSweet(Long id) {
        sweetRepository.deleteById(id);
    }

    @Override
    public Sweet purchaseSweet(Long id, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Purchase quantity must be greater than 0");
        }
        Sweet sweet = sweetRepository.findById(id)
                .orElseThrow(() -> new SweetNotFoundException("Sweet not found with id: " + id));

        if (sweet.getQuantity() < quantity) {
            throw new InsufficientQuantityException(
                "Insufficient quantity. Available: " + sweet.getQuantity() + ", Requested: " + quantity);
        }

        sweet.setQuantity(sweet.getQuantity() - quantity);

        return sweetRepository.save(sweet);
    }

    @Override
    public Sweet restockSweet(Long id, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Restock quantity must be greater than 0");
        }

        Sweet sweet = sweetRepository.findById(id)
                .orElseThrow(() -> new SweetNotFoundException("Sweet not found with id: " + id));
        sweet.setQuantity(sweet.getQuantity() + quantity);

        return sweetRepository.save(sweet);
    }
}