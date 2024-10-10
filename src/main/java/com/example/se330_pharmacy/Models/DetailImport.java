package com.example.se330_pharmacy.Models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DetailImport {
    private int importId;
    private int productId;
    private int importPrice;
    private String productName;
    private int quantity;
    private int total;
    private String supplier;



    public int getProduct_id() {
        return productId;
    }

    public void setProduct_id(int product_id) {
        this.productId = product_id;
    }

    // Getter và Setter cho importPrice
    public int getImportPrice() {
        return importPrice;
    }

    public void setImportPrice(int importPrice) {
        this.importPrice = importPrice;
    }

    // Getter và Setter cho quantity
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Getter và Setter cho total
    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public DetailImport(int productID, String productName, int productPrice, int productQuantities, int productTotal) {
        this.productId = productID;
        this.productName = productName;
        this.importPrice = productPrice;
        this.quantity = productQuantities;
        this.total = productTotal;
    }

    public boolean addDetailData(String proId, String id, float importPrice, int quantity, double total) {
        String query = "INSERT INTO DetailImportForm (Product_id, ImportForm_id, ImportPrice, Quantity, Total) VALUES (?, ?, ?, ?, ?)";

        try {
            Connection connection = ConnectDB.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, Integer.parseInt(proId));
            preparedStatement.setInt(2, Integer.parseInt(id));
            preparedStatement.setFloat(3, importPrice);
            preparedStatement.setInt(4, quantity);
            preparedStatement.setDouble(5, total);

            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public int getImportId() {
        return importId;
    }

    public void setImportId(int importId) {
        this.importId = importId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }
}

