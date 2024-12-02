package com.example.se330_pharmacy.DataAccessObject;

import com.example.se330_pharmacy.Models.Bill;
import com.example.se330_pharmacy.Models.ConnectDB;
import com.example.se330_pharmacy.Models.Report;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReportDAO {
    ConnectDB connect = ConnectDB.getInstance();

    public ReportDAO() {
    }

    public ObservableList<Report> getReportStatus(int month,int year) {
        ObservableList<Report>  reports = FXCollections.observableArrayList();
        String query = "SELECT b.bill_id, b.datebill,emp.employname,b.billvalue FROM employee emp, bill b WHERE emp.employee_id = b.employee_id and EXTRACT (MONTH FROM b.datebill) = "+ month +" and EXTRACT ( YEAR FROM b.datebill) = "+ year +" ORDER by b.bill_id desc";

        try (PreparedStatement statement = connect.getPreparedStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Report report = new Report();
                report.setIdBillStatus(resultSet.getInt(1));
                report.setDateStatus(resultSet.getDate(2));
                report.setEmploynameStatus(resultSet.getString(3));
                report.setValueStatus(resultSet.getInt(4));
                reports.add(report);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reports;
    }
    public ObservableList<Report> getReportTop(int month,int year) {
        ObservableList<Report>  reports = FXCollections.observableArrayList();
        String query = "SELECT emp.employee_id, emp.employname,COUNT(b.bill_id) as sl,SUM(b.billvalue) as tongthu FROM employee emp, bill b WHERE emp.employee_id = b.employee_id and EXTRACT (MONTH FROM b.datebill) = "+ month +" and EXTRACT ( YEAR FROM b.datebill) = "+ year +" GROUP BY emp.employee_id ORDER by SUM(b.billvalue) desc";

        try (PreparedStatement statement = connect.getPreparedStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Report report = new Report();
                report.setIdEmployTop(resultSet.getInt(1));
                report.setEmploynameTop(resultSet.getString(2));
                report.setNumberTop(resultSet.getInt(3));
                report.setTotalTop(resultSet.getInt(4));
                reports.add(report);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reports;
    }
    /*public List<Report> searchReports(String searchText) {
        List<Report> reports = new ArrayList<>();
        String query = "SELECT id, medicineName, purchasePrice, quantityPurchased, sellingPrice, quantitySold, remainingRate, unit " +
                "FROM report WHERE LOWER(medicineName) LIKE ? OR CAST(id AS TEXT) LIKE ?";

        try (Connection connection = connect.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            String searchPattern = "%" + searchText.toLowerCase() + "%";
            statement.setString(1, searchPattern);
            statement.setString(2, searchPattern);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Report report = new Report(
                            resultSet.getInt("id"),
                            resultSet.getString("medicineName"),
                            resultSet.getDouble("purchasePrice"),
                            resultSet.getInt("quantityPurchased"),
                            resultSet.getDouble("sellingPrice"),
                            resultSet.getInt("quantitySold"),
                            resultSet.getDouble("remainingRate"),
                            resultSet.getString("unit")
                    );
                    reports.add(report);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reports;
    }*/
    public ObservableList<Bill> GetTop10ProductData(int month,int year)
    {
        ObservableList<Bill> list = FXCollections.observableArrayList();
        String sqlQuery = "SELECT p.product_id, p.productname AS name, SUM(dt.quantities) AS quantity FROM product p, detailbill dt, bill b WHERE p.product_id = dt.product_id  and b.bill_id = dt.bill_id  and EXTRACT (MONTH FROM b.datebill) = " + month + " and EXTRACT ( YEAR FROM b.datebill) = " + year + " GROUP BY p.product_id,p.productname ORDER BY SUM(dt.quantities) DESC";
        try(PreparedStatement statement = connect.getPreparedStatement(sqlQuery))
        {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Bill bill = new Bill();
                bill.setProductId(resultSet.getInt("product_id"));
                bill.setProductName(resultSet.getString("name"));
                bill.setQuantities(resultSet.getInt("quantity"));
                list.add(bill);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public int GetRevenueFromSaleMonth(int month, int year) throws SQLException {
        String sqlQuery = "SELECT SUM(billvalue) as sl from bill Where EXTRACT( MONTH FROM datebill) =" + month + " and EXTRACT (YEAR FROM datebill) = " + year + "";
        return connect.getData(sqlQuery).getInt("sl");
    }
    public String GetRevenueFromSaleToday(int day,int month, int year) throws SQLException {
        String sqlQuery = "SELECT SUM(billvalue) as sl from bill Where EXTRACT (DAY FROM datebill) = "+day+" and EXTRACT( MONTH FROM datebill) =" + month + " and EXTRACT (YEAR FROM datebill) = " + year + "";
        return String.valueOf(connect.getData(sqlQuery).getInt("sl"));
    }
    public String GetRevenueImportMonth(int month,int year) throws SQLException {
        String sqlQuery = "SELECT SUM(totalmoney) as sl FROM importform WHERE  EXTRACT( MONTH FROM formdate) =" + month + " and EXTRACT (YEAR FROM formdate) = " + year + " ";
        return String.valueOf(connect.getData(sqlQuery).getInt("sl"));
    }
    public String GetRevenueImportDay(int day,int month,int year) throws SQLException {
        String sqlQuery = "SELECT SUM(totalmoney) as sl FROM importform WHERE  EXTRACT (DAY FROM formdate) =" + day + " and EXTRACT( MONTH FROM formdate) =" + month + " and EXTRACT (YEAR FROM formdate) = " + year + " ";
        return String.valueOf(connect.getData(sqlQuery).getInt("sl"));
    }
    public String GetExpendExportMonth(int month,int year) throws SQLException {
        String sqlQuery = "SELECT SUM(totalmoney) as sl FROM exportform WHERE EXTRACT( MONTH FROM exportdate) =" + month + " and EXTRACT (YEAR FROM exportdate) = " + year + " ";
        return String.valueOf(connect.getData(sqlQuery).getInt("sl"));
    }
    public String GetExpendExportDay(int day,int month,int year) throws SQLException {
        String sqlQuery = "SELECT SUM(totalmoney) as sl FROM exportform WHERE  EXTRACT (DAY FROM exportdate) =" + day + " and EXTRACT( MONTH FROM exportdate) =" + month + " and EXTRACT (YEAR FROM exportdate) = " + year + " ";
        return String.valueOf(connect.getData(sqlQuery).getInt("sl"));
    }
    public String GetNumberOfProductMonth(int month, int year) throws SQLException {
        String sqlQuery = "SELECT SUM(dt.quantities) as sl from bill b, detailbill dt Where b.bill_id = dt.bill_id and EXTRACT( MONTH FROM datebill) =" + month + " and EXTRACT (YEAR FROM datebill) = " + year + "";
        return String.valueOf(connect.getData(sqlQuery).getInt("sl"));
    }
    public String GetNumberOfProductToday(int day, int month, int year) throws SQLException {
        String sqlQuery = "SELECT SUM(dt.quantities) as sl from bill b, detailbill dt Where b.bill_id = dt.bill_id and EXTRACT (DAY FROM datebill) =" + day + " and EXTRACT (MONTH FROM datebill) = " + month + " and EXTRACT ( YEAR FROM datebill) = " + year + "";
        return String.valueOf(connect.getData(sqlQuery).getInt("sl"));
    }
    public String GetNumberOfBillMonth(int month, int year) throws SQLException {
        String sqlQuery = "SELECT COUNT(bill_id) as sl from bill Where EXTRACT (MONTH FROM datebill) =" + month + " and EXTRACT (YEAR FROM datebill) = " + year + "";
        return String.valueOf(connect.getData(sqlQuery).getInt("sl"));
    }
    public String GetNumberOfBillToday(int day, int month, int year) throws SQLException {
        String sqlQuery = "SELECT COUNT(bill_id) as sl from bill Where EXTRACT (DAY FROM datebill) =" + day + " and EXTRACT (Month FROM datebill) = " + month + " and EXTRACT (YEAR FROM datebill) = " + year + "";
        return String.valueOf(connect.getData(sqlQuery).getInt("sl"));
    }


}
