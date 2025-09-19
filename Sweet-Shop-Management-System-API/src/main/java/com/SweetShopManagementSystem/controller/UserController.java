package com.SweetShopManagementSystem.controller;

import com.SweetShopManagementSystem.dto.AuthResponse;
import com.SweetShopManagementSystem.dto.LoginRequest;
import com.SweetShopManagementSystem.model.User;
import com.SweetShopManagementSystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUser(@RequestBody User user) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.registerUser(user));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) throws Exception {
        AuthResponse authResponse = userService.login(loginRequest);
        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }
}

