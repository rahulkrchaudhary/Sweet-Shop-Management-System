package com.SweetShopManagementSystem.controller;

import com.SweetShopManagementSystem.dto.PurchaseRequest;
import com.SweetShopManagementSystem.dto.RestockRequest;
import com.SweetShopManagementSystem.dto.SweetRequest;
import com.SweetShopManagementSystem.model.Sweet;
import com.SweetShopManagementSystem.service.SweetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sweets")
public class SweetController {

    @Autowired
    private SweetService sweetService;

    @PostMapping
    public ResponseEntity<Sweet> addSweet(@RequestBody SweetRequest sweetRequest) {
        Sweet sweet = sweetService.addSweet(sweetRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(sweet);
    }

    @GetMapping
    public ResponseEntity<List<Sweet>> getAllSweets() {
        List<Sweet> sweets = sweetService.getAllSweets();
        return ResponseEntity.ok(sweets);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Sweet>> searchSweet(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice) {

        List<Sweet> sweets = sweetService.searchSweets(name, category, minPrice, maxPrice);
        return ResponseEntity.ok(sweets);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Sweet> updateSweet(
            @PathVariable Long id,
            @RequestBody SweetRequest sweetRequest) {

        Sweet sweet = sweetService.updateSweet(id, sweetRequest);
        return ResponseEntity.ok(sweet);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSweetById(@PathVariable Long id) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = false;
        if (auth != null && auth.getAuthorities() != null) {
            isAdmin = auth.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ADMIN"));
        }
        if (!isAdmin) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        sweetService.deleteSweet(id);
        return ResponseEntity.noContent().build(); // 204
    }

    @PostMapping("/{id}/purchase")
    public ResponseEntity<Sweet> purchaseSweet(
            @PathVariable Long id,
            @RequestBody(required = false) PurchaseRequest purchaseRequest) {
        
        int quantity = (purchaseRequest != null) ? purchaseRequest.getQuantity() : 1;
        Sweet updatedSweet = sweetService.purchaseSweet(id, quantity);
        return ResponseEntity.ok(updatedSweet);
    }

    @PostMapping("/{id}/restock")
    public ResponseEntity<Sweet> restockSweet(
            @PathVariable Long id,
            @RequestBody RestockRequest restockRequest) {
        
        // Check if user is admin
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = false;
        if (auth != null && auth.getAuthorities() != null) {
            isAdmin = auth.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ADMIN"));
        }
        if (!isAdmin) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Sweet updatedSweet = sweetService.restockSweet(id, restockRequest.getQuantity());
        return ResponseEntity.ok(updatedSweet);
    }
}

