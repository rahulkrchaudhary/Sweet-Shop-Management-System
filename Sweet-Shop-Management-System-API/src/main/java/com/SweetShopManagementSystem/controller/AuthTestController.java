package com.SweetShopManagementSystem.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthTestController {
    @GetMapping("/auth-endpoint")
    public ResponseEntity<String> secure() {
        return ResponseEntity.ok("secure");
    }
}
