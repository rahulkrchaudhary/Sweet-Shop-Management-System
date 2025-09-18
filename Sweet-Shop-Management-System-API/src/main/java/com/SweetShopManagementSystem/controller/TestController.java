package com.SweetShopManagementSystem.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class TestController {
    @GetMapping("/test")
    public ResponseEntity<String> testPermitAll() {
        return ResponseEntity.ok("ok");
    }
}
