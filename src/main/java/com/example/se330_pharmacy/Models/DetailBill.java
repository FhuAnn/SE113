package com.example.se330_pharmacy.Models;

public class DetailBill {
    private int billId;
    private int productId;
    private float presentPrice;
    private int quantity;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    private String productName;
    private String unit;
    private float price;

    public DetailBill(int billID, String productName, int quantities, String unit, float price) {
        this.billId = billID;
        this.productName = productName;
        this.quantity = quantities;
        this.unit = unit;
        this.price = price;
    }

    public DetailBill(int billId, int productId, float presentPrice, int quantity) {
        this.billId = billId;
        this.productId = productId;
        this.presentPrice = presentPrice;
        this.quantity = quantity;
    }

    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public float getPresentPrice() {
        return presentPrice;
    }

    public void setPresentPrice(float presentPrice) {
        this.presentPrice = presentPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
