package com.example.se330_pharmacy.Models;

public class Product {
    private int productId;
    private String productName;
    private long productImportPrice;
    private String productDescription;
    private String productOrigin;
    private String productType;
    private String productBigUnit;
    private String productSmallUnit;
    private int productSmallUnitQuantities;
    private int productBigUnitQuantities;
    private int productCoef;


    public int getProductCoef() {
        return productCoef;
    }


    public Product() {
    }
    @Override
    public String toString() {
        return productCoef + " "+ productSmallUnit +"/"+ productBigUnit;
    }
    public Product(int productId, String productName, int productImport, String productDescription, String productOrigin, String productType, String productBigUnit, String productSmallUnit, int productSmallUnitQuantities, int productBigUnitQuantities) {
        this.productId = productId;
        this.productName = productName;
        this.productImportPrice = productImport;
        this.productDescription = productDescription;
        this.productOrigin = productOrigin;
        this.productType = productType;
        this.productBigUnit = productBigUnit;
        this.productSmallUnit = productSmallUnit;
        this.productSmallUnitQuantities = productSmallUnitQuantities;
        this.productBigUnitQuantities = productBigUnitQuantities;
    }
    public Product(int productId, String productName, long productImportPrice, String productDescription, String productOrigin, String productType, String productBigUnit) {
        this.productId = productId;
        this.productName = productName;
        this.productImportPrice = productImportPrice;
        this.productDescription = productDescription;
        this.productOrigin = productOrigin;
        this.productType = productType;
        this.productBigUnit = productBigUnit;
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
    public void setProductCoef(int productCoef) {
        this.productCoef = productCoef;
    }

    public void setProductImportPrice(long productImportPrice) {
        this.productImportPrice = productImportPrice;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public long getProductImportPrice() {
        return productImportPrice;
    }

    public void setProductImportPrice(int productImportPrice) {
        this.productImportPrice = productImportPrice;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductOrigin() {
        return productOrigin;
    }

    public void setProductOrigin(String productOrigin) {
        this.productOrigin = productOrigin;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getProductBigUnit() {
        return productBigUnit;
    }

    public void setProductBigUnit(String productBigUnit) {
        this.productBigUnit = productBigUnit;
    }

    public String getProductSmallUnit() {
        return productSmallUnit;
    }

    public void setProductSmallUnit(String productSmallUnit) {
        this.productSmallUnit = productSmallUnit;
    }

    public int getProductSmallUnitQuantities() {
        return productSmallUnitQuantities;
    }

    public void setProductSmallUnitQuantities(int productSmallUnitQuantities) {
        this.productSmallUnitQuantities = productSmallUnitQuantities;
    }

    public int getProductBigUnitQuantities() {
        return productBigUnitQuantities;
    }

    public void setProductBigUnitQuantities(int productBigUnitQuantities) {
        this.productBigUnitQuantities = productBigUnitQuantities;
    }

}
