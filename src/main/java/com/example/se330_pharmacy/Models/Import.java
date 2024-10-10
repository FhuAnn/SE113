package com.example.se330_pharmacy.Models;

import java.util.Date;

public class Import {
    private int importId;
    private int employeeId;
    private int supplierId;
    private int totalPrice;
    private int productId;
    private int importPrice;
    private int quantity;
    private int total;
    private String formDate;

    String employName;
    String supplierName;
    String productName;

    // Constructors

    public Import() {
    }

    public Import(int employeeId, int supplierId, int total, String formDate) {
        this.employeeId = employeeId;
        this.supplierId = supplierId;
        this.total = total;
        this.formDate = formDate;
    }

    public Import(int importId, String formDate, String employName, String supplierName) {
        this.importId = importId;
        this.formDate = formDate;
        this.employName = employName;
        this.supplierName = supplierName;
    }
    public Import(int productID, String productName, int productPrice, int productQuantities, int productTotal) {
        this.productId = productID;
        this.productName = productName;
        this.importPrice = productPrice;
        this.quantity = productQuantities;
        this.totalPrice = productTotal;
    }

    // Getters and Setters
    public int getImportId() {
        return importId;
    }

    public void setImportId(int importId) {
        this.importId = importId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getImportPrice() {
        return importPrice;
    }

    public void setImportPrice(int importPrice) {
        this.importPrice = importPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getFormDate() {
        return formDate;
    }

    public void setFormDate(String formDate) {
        this.formDate = formDate;
    }

    public String getEmployName() {
        return employName;
    }

    public void setEmployName(String employName) {
        this.employName = employName;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

}

