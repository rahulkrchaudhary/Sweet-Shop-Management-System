package com.SweetShopManagementSystem.exception;

public class SweetNotFoundException extends RuntimeException {
    public SweetNotFoundException(String message) {
        super(message);
    }
    
    public SweetNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}