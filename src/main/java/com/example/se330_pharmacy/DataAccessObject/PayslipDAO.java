package com.example.se330_pharmacy.DataAccessObject;

import com.example.se330_pharmacy.Models.ConnectDB;
import com.example.se330_pharmacy.Models.Payslip;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PayslipDAO {
    ConnectDB connectDB = ConnectDB.getInstance();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public ObservableList<Payslip> GetPaySlipData()
    {
        ObservableList<Payslip> payslips = FXCollections.observableArrayList();
        String query = "SELECT * FROM payslip pl, Employee emp  WHERE pl.employee_id = emp.employee_id";
        try (ResultSet resultSet = connectDB.getResultSet(query))
        {
            while (resultSet.next())
            {
                Payslip payslip = new Payslip();
                payslip.setPayslip_id(resultSet.getInt("payslip_id"));
                payslip.setReceipt_id(resultSet.getInt("receipt_id"));
                payslip.setEmployee_id(resultSet.getInt("employee_id"));
                payslip.setTenNhanVien(resultSet.getString("employname"));
                payslip.setViTriLamViec(resultSet.getString("position"));
                payslip.setCreateDate(LocalDateTime.parse(resultSet.getString("createdate"),formatter));
                payslip.setContent(resultSet.getString("content"));
                payslip.setTotalPay(resultSet.getInt("totalpay"));
                payslip.setStatus(resultSet.getString("status"));
                payslip.setNote(resultSet.getString("note"));
                payslip.setReceipt_id(resultSet.getInt("receipt_id"));
                payslips.add(payslip);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return payslips;
    }
    public boolean UpdatePayslipCompleted(int _id,int _receipt_id)
    {
        String query = "UPDATE payslip SET status = 'Completed', receipt_id = ? WHERE payslip_id = ?";
        try(PreparedStatement statement = connectDB.databaseLink.prepareStatement(query)) {
            statement.setInt(1,_receipt_id);
            statement.setInt(2,_id);
            int affRow = statement.executeUpdate();
            if(affRow ==0 ) return false;
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return true;
    }
    public boolean AddPaySlip(Payslip payslip)
    {
        LocalDateTime dateTime = LocalDateTime.parse(LocalDateTime.now().format(formatter),formatter);
        String query = "INSERT INTO payslip (employee_id,content,createdate,totalpay,note,status)" +
                " VALUES (?,?,?,?,?,?)";
        try(PreparedStatement statement = connectDB.databaseLink.prepareStatement(query)) {
            statement.setInt(1,payslip.getEmployee_id());
            statement.setString(2,payslip.getContent());
            statement.setObject(3,dateTime);
            statement.setDouble(4,payslip.getTotalPay());
            statement.setString(5,payslip.getNote());
            statement.setString(6,payslip.getStatus());
            int affRow = statement.executeUpdate();
            if(affRow ==0 ) return false;
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return true;
    }
    public boolean UpdatePaySlip(Payslip payslip)
    {
        LocalDateTime localDateTime = LocalDateTime.parse(LocalDateTime.now().format(formatter),formatter);
        String query = "UPDATE payslip SET  createdate = ?,totalpay = ?,note =?  WHERE payslip_id = ?";
        try(PreparedStatement statement = connectDB.databaseLink.prepareStatement(query)) {
            statement.setObject(1,localDateTime);
            statement.setDouble(2,payslip.getTotalPay());
            statement.setString(3,payslip.getNote());
            statement.setInt(4,payslip.getPayslip_id());
            return connectDB.handleData(statement);
        } catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    }
    public boolean isReceiptOfPayslip(int _id){
        String query = "SELECT 1 FROM payslip WHERE receipt_id = ? ";
        try (PreparedStatement statement = connectDB.databaseLink.prepareStatement(query)) {
            statement.setInt(1,_id);
            try (ResultSet rs =  statement.executeQuery();) {
                return rs.next();
            }
        } catch (SQLException e ){
            e.printStackTrace();
        }
        return false;
    }
}
