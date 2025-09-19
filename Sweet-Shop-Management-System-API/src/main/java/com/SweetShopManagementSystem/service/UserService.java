package com.SweetShopManagementSystem.service;

import com.SweetShopManagementSystem.dto.AuthResponse;
import com.SweetShopManagementSystem.dto.LoginRequest;
import com.SweetShopManagementSystem.model.User;

public interface UserService {
    public AuthResponse createUser(User user) throws Exception;
    public AuthResponse login(LoginRequest loginRequest) throws Exception;
}
