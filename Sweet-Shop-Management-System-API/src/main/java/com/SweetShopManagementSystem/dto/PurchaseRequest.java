package com.SweetShopManagementSystem.dto;


public class PurchaseRequest {
    private int quantity = 1;
    
    public PurchaseRequest() {
    }
    public PurchaseRequest(int quantity) {
        this.quantity = Math.max(1, quantity);
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = Math.max(1, quantity);
    }
}