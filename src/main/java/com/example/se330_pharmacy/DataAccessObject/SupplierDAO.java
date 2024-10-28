package com.example.se330_pharmacy.DataAccessObject;

import com.example.se330_pharmacy.Models.ConnectDB;
import com.example.se330_pharmacy.Models.Supplier;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SupplierDAO {
    ConnectDB connectDB =ConnectDB.getInstance();
    public ObservableList<Supplier> getSuppliers() {
        String sqlQuery = "SELECT partner_id, partnername,address, phonenumber,email FROM partner ORDER BY partner_id ASC";
        ObservableList<Supplier> suppliers = FXCollections.observableArrayList();
        try (ResultSet rs = connectDB.getPreparedStatement(sqlQuery).executeQuery()) {
            while(rs.next()) {
                Supplier suppier = new Supplier(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5));
                suppliers.add(suppier);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return suppliers;
    }

    public void insertSupplier(Supplier supplier, Runnable onSuccess) {
        String sqlInsert = "INSERT INTO partner (partnername, address, phonenumber, email) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connectDB.databaseLink.prepareStatement(sqlInsert)) {
            pstmt.setString(1, supplier.getPartnername());
            pstmt.setString(2, supplier.getAddress());
            pstmt.setString(3, supplier.getPhonenumber());
            pstmt.setString(4, supplier.getEmail());
            pstmt.executeUpdate();
            if (onSuccess != null) {
                onSuccess.run();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void updateSupplier(Supplier supplier, Runnable onSuccess) {
        String sqlUpdate = "UPDATE partner SET partnername = ?, address = ?, phonenumber = ?, email = ? WHERE partner_id = ?";
        try (PreparedStatement pstmt = connectDB.databaseLink.prepareStatement(sqlUpdate)) {
            pstmt.setString(1, supplier.getPartnername());
            pstmt.setString(2, supplier.getAddress());
            pstmt.setString(3, supplier.getPhonenumber());
            pstmt.setString(4, supplier.getEmail());
            pstmt.setInt(5, supplier.getPartner_id());
            pstmt.executeUpdate();
            if (onSuccess != null) {
                onSuccess.run();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteSupplier(Supplier supplier, Runnable onSuccess) {
        String sqlDelete = "DELETE FROM partner WHERE partner_id = ?";
        try (PreparedStatement pstmt = connectDB.databaseLink.prepareStatement(sqlDelete)) {
            pstmt.setInt(1, supplier.getPartner_id());
            pstmt.executeUpdate();
            if (onSuccess != null) {
                onSuccess.run();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
