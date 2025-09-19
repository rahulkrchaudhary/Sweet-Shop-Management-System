package com.SweetShopManagementSystem.dto;

import com.SweetShopManagementSystem.model.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SweetRequest {
    private Long sweetId;
    private String name;
    private String category;
    private double price;
    private int quantity;
}