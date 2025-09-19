package com.SweetShopManagementSystem.service.service_impl;

import com.SweetShopManagementSystem.dto.SweetRequest;
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

//    @Override
//    public List<Sweet> searchSweets(String name, Category category, double minPrice, double maxPrice) {
//        if (name != null) return sweetRepository.findByNameContainingIgnoreCase(name);
//        if (category != null) return sweetRepository.findByCategory(category);
//        if ((Double)minPrice != null && (Double)maxPrice != null) return sweetRepository.findByPriceBetween(minPrice, maxPrice);
//        return sweetRepository.findAll();
//    }

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
//        existing.setDescription(sweet.getDescription());
        return sweetRepository.save(existing);
    }

    @Override
    public void deleteSweet(Long id) {
        sweetRepository.deleteById(id);
    }
}