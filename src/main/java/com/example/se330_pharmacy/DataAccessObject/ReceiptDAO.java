package com.example.se330_pharmacy.DataAccessObject;

import com.example.se330_pharmacy.Models.ConnectDB;
import com.example.se330_pharmacy.Models.Receipt;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReceiptDAO {
    ConnectDB connectDB = ConnectDB.getInstance();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public ObservableList<Receipt> GetReceiptsData()
    {
        ObservableList<Receipt> receipts = FXCollections.observableArrayList();
        String query = "SELECT rp.*, emp1.employname AS chargername, emp2.employname AS employname, emp1.position AS pos_charger, emp2.position AS pos_emp FROM Receipt rp " +
                "LEFT JOIN Employee emp1 ON rp.personcharge_id = emp1.employee_id " +
                "LEFT JOIN Employee emp2 ON rp.employee_id = emp2.employee_id " +
                "WHERE rp.personcharge_id IS NULL OR (rp.personcharge_id = emp1.employee_id " +
                "AND rp.employee_id = emp2.employee_id)";
        try (ResultSet resultSet = connectDB.getResultSet(query))
        {
            while (resultSet.next())
            {
                    Receipt receipt = new Receipt();
                    receipt.setReceipt_id(resultSet.getInt("receipt_id"));
                    receipt.setEmployee_id(resultSet.getInt("employee_id"));
                    receipt.setTenNhanVien(resultSet.getString("employname"));
                    receipt.setPersoncharge_id(resultSet.getInt("personcharge_id"));
                    receipt.setTenNguoiTra(resultSet.getString("chargername"));
                    receipt.setContent(resultSet.getString("content"));
                    receipt.setTotalPay(resultSet.getInt("totalpay"));
                    receipt.setStatus(resultSet.getString("status"));
                    receipt.setNote(resultSet.getString("note"));
                    receipt.setCreateDate(LocalDateTime.parse(resultSet.getString("createdate"),formatter));
                    receipt.setVitriNguoiTra(resultSet.getString("pos_charger"));
                    receipt.setViTriNhanVien(resultSet.getString("pos_emp"));
                    receipts.add(receipt);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return receipts;
    }


    public int AddReceiptToDB(Receipt receipt)
    {
        LocalDateTime dateTime = LocalDateTime.parse(LocalDateTime.now().format(formatter),formatter);
        String query ="INSERT INTO receipt (content,createdate,totalpay,note,status,personcharge_id,employee_id) VALUES(?,?,?,?,?,?,?) RETURNING receipt_id";
        int receipt_id = -1;
        try (PreparedStatement statement = connectDB.databaseLink.prepareStatement(query)) {
            statement.setString(1,receipt.getContent());
            statement.setObject(2,dateTime);
            statement.setDouble(3,receipt.getTotalPay());
            statement.setString(4,receipt.getNote());
            statement.setString(5,receipt.getStatus());
            statement.setInt(6,receipt.getPersoncharge_id());
            statement.setInt(7,receipt.getEmployee_id());
            ResultSet resultSet = statement.executeQuery(); // Sử dụng executeQuery() thay vì executeUpdate()
            if (resultSet.next()) {
                receipt_id = resultSet.getInt(1); // Lấy giá trị của makhambenh từ ResultSet
            }
        }
        catch (SQLException e ) {
            e.printStackTrace();
        }
        return receipt_id;
    }
    public boolean UpdateReceiptToDB(Receipt receipt)
    {
        LocalDateTime dateTime = LocalDateTime.parse(LocalDateTime.now().format(formatter),formatter);
        String query ="UPDATE receipt SET createdate = ?, status= ?, note = ? , personcharge_id = ? WHERE receipt_id = ?  ";
        try (PreparedStatement statement = connectDB.databaseLink.prepareStatement(query)) {
            statement.setObject(1, dateTime);
            statement.setString(2,receipt.getStatus());
            statement.setString(3,receipt.getNote());
            statement.setInt(4,receipt.getPersoncharge_id());
            statement.setInt(5,receipt.getReceipt_id());
            int aff = statement.executeUpdate(); // Sử dụng executeQuery() thay vì executeUpdate()
            if(aff > 0) return true;
        }
        catch (SQLException e ) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean autoCreateReceipt(int employeeId, String content, int totalPay, String status, String note) {
        LocalDateTime dateTime = LocalDateTime.parse(LocalDateTime.now().format(formatter),formatter);
        String sqlQuery = "INSERT INTO receipt (employee_id, content, createDate, totalpay, status, note) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connectDB.databaseLink.prepareStatement(sqlQuery);
            preparedStatement.setInt(1, employeeId);
            preparedStatement.setString(2, content);
            preparedStatement.setObject(3, dateTime);
            preparedStatement.setInt(4, totalPay);
            preparedStatement.setString(5, status);
            preparedStatement.setString(6, note);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
