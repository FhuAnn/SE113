package com.example.se330_pharmacy.DataAccessObject;

import com.example.se330_pharmacy.Models.ConnectDB;
import com.example.se330_pharmacy.Models.DetailBill;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DetailBillDAO {
    private ConnectDB connectDB = ConnectDB.getInstance();
    public DetailBillDAO() {
    }
    public boolean addDetailData(String billId, String productId, String price, String quantity, String unit) {
        String sql = "INSERT INTO DetailBill (Bill_id, Product_id, Quantities, Unit_Name, Price) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connectDB.databaseLink.prepareStatement(sql)) {
            preparedStatement.setInt(1, Integer.parseInt(billId));
            preparedStatement.setInt(2, Integer.parseInt(productId));
            preparedStatement.setInt(3, Integer.parseInt(quantity));
            preparedStatement.setString(4, unit);
            preparedStatement.setInt(5, Integer.parseInt(price));

            int rowsAffected = preparedStatement.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public ObservableList<DetailBill> getDetailBill(String billId) {
        ObservableList<DetailBill> detailBillList = FXCollections.observableArrayList();
        String sqlQuery = "SELECT Bill_id as BillID, pro.ProductName as \"Product Name\", Quantities, Unit_Name as Unit, dtl.Price " +
                "FROM DetailBill dtl, existedproduct pro " +
                "WHERE Bill_id =" + billId + " AND pro.Product_id = dtl.Product_id";

        try (Statement stmt = connectDB.databaseLink.createStatement(); ResultSet rs = stmt.executeQuery(sqlQuery)) {
            while (rs.next()) {
                int billID = rs.getInt("BillID");
                String productName = rs.getString("Product Name");
                int quantities = rs.getInt("Quantities");
                String unit = rs.getString("Unit");
                float price = rs.getFloat("Price");

                DetailBill detailBill = new DetailBill(billID, productName, quantities, unit, price);
                detailBillList.add(detailBill);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return detailBillList;
    }
}