package com.example.se330_pharmacy.Models;

public class CartItem {
    private int productId;
    private String productName;
    private String productPrice;
    private int productQuantities;
    private String productUnit;

    public CartItem() {
    }

    public CartItem(int productId, String productName, String productPrice, String productQuantities, String productUnit) {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productQuantities = Integer.parseInt(productQuantities);
        this.productUnit = productUnit;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public int getProductQuantities() {
        return productQuantities;
    }

    public void setProductQuantities(String productQuantities) {
        this.productQuantities = Integer.parseInt(productQuantities);
    }

    public String getProductUnit() {
        return productUnit;
    }

    public void setProductUnit(String productUnit) {
        this.productUnit = productUnit;
    }
}
