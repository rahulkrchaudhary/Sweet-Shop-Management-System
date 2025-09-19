package com.SweetShopManagementSystem.dto;

import com.SweetShopManagementSystem.model.USER_ROLE;
import lombok.Data;

@Data
public class AuthResponse {
    private String jwt;
    private String message;
    private USER_ROLE role;
}
