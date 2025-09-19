package com.SweetShopManagementSystem.exception;

public class EmailAlreadyUsedException extends RuntimeException{
    public EmailAlreadyUsedException(String message) {
        super(message);
    }
}
