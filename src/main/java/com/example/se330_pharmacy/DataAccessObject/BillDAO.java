package com.example.se330_pharmacy.DataAccessObject;

import com.example.se330_pharmacy.Models.Bill;
import com.example.se330_pharmacy.Models.ConnectDB;
import com.example.se330_pharmacy.Models.Unit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BillDAO {
    private ConnectDB connectDB = ConnectDB.getInstance();

    public BillDAO() {
    }

    public String addBill(String employeeId, String cusName,String phoneNumber,String billValue) {
        Date now = Date.valueOf(LocalDate.now());

        String generatedId = null;
        String sql = "INSERT INTO Bill (Employee_id, Cus_Name, PhoneNumber, BillValue, DateBill) VALUES (?, ?, ?, ?, ?)";
        try (
                PreparedStatement pstmt = connectDB.databaseLink.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, Integer.parseInt(employeeId));
            pstmt.setString(2, cusName);
            pstmt.setString(3, phoneNumber);
            pstmt.setDouble(4, Double.parseDouble(billValue));
            pstmt.setDate(5, now);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        generatedId = String.valueOf(rs.getInt(1));
                    }
                } catch (SQLException ex) {
                    System.out.println("Error retrieving generated key: " + ex.getMessage());
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error executing insert statement: " + ex.getMessage());
        }

        return generatedId;


    }

    public boolean autoCreateReceipts(String employeeId, String contentReceipts, String billValue, String status, String note) {
        java.util.Date date = new java.util.Date();
        java.sql.Date currentDate = new java.sql.Date(date.getTime());

        String sql = "INSERT INTO Receipt (Employee_id, Content, CreateDate, TotalPay, Status, Note) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connectDB.databaseLink.prepareStatement(sql)) {
            preparedStatement.setInt(1, Integer.parseInt(employeeId));
            preparedStatement.setString(2, contentReceipts);
            preparedStatement.setDate(3, currentDate);
            preparedStatement.setDouble(4, Double.parseDouble(billValue));
            preparedStatement.setString(5, status);
            preparedStatement.setString(6, note);

            int rowsAffected = preparedStatement.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public List<Unit> getUnit(String id) {
        List<Unit> units = new ArrayList<>();

        String sqlQuery = STR."SELECT uni.big_unit, uni.small_unit FROM detailbill dtl, Unit uni, Product pro WHERE dtl.Product_id = pro.product_id AND pro.Unit_id = uni.Unit_id AND dtl.bill_id = \{id}";

        try (Statement statement = connectDB.databaseLink.createStatement();
             ResultSet resultSet = statement.executeQuery(sqlQuery)) {

            while (resultSet.next()) {
                String bigUnit = resultSet.getString("big_unit");
                String smallUnit = resultSet.getString("small_unit");
                units.add(new Unit(bigUnit, smallUnit));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return units;
    }

    public int getCoef(int id) throws SQLException {
        String sqlQuery = "SELECT uni.Value  FROM Product pro, Unit uni WHERE uni.Unit_id = pro.Unit_id AND pro.Product_id = ?";
        PreparedStatement stmt = connectDB.getPreparedStatement(sqlQuery);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getInt("Value");
        }
        return -1;
    }

    public int getBigQuan(int id) throws SQLException {
        String sqlQuery = "SELECT big_unit FROM Product WHERE Product_id = ?";
        PreparedStatement stmt = connectDB.getPreparedStatement(sqlQuery);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getInt("big_unit");
        }
        return -1;
    }

    public int getSmallQuan(int id) throws SQLException {
        String sqlQuery = "SELECT small_unit FROM Product WHERE Product_id = ?";
        PreparedStatement stmt = connectDB.getPreparedStatement(sqlQuery);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getInt("small_unit");
        }
        return -1;
    }

    public boolean updateProduct(String id, String bigQuantity, String smallQuantity) {

        String query;
        // Update lv1 and lv2
        query = "UPDATE Product SET big_unit = ?, small_unit = ? WHERE Product_id = ?";
        try (PreparedStatement preparedStatement = connectDB.databaseLink.prepareStatement(query)) {
            preparedStatement.setInt(1, Integer.parseInt(bigQuantity));
            preparedStatement.setInt(2, Integer.parseInt(smallQuantity));
            preparedStatement.setInt(3, Integer.parseInt(id));

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    public ObservableList<Bill> getBillData() {
        ObservableList<Bill> billList = FXCollections.observableArrayList();
        String sqlQuery = "SELECT Bill_id, Cus_Name AS Customer, PhoneNumber AS Phone, BillValue AS Value, DateBill AS Date FROM Bill";

        try (Statement stmt = connectDB.databaseLink.createStatement(); ResultSet rs = stmt.executeQuery(sqlQuery)) {
            while (rs.next()) {
                int billId = rs.getInt("Bill_id");
                String customerName = rs.getString("Customer");
                String phoneNumber = rs.getString("Phone");
                int billValue = rs.getInt("Value");
                Date dateBill = rs.getDate("Date");

                Bill bill = new Bill(billId, customerName, phoneNumber, billValue, dateBill);
                billList.add(bill);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return billList;
    }


}
