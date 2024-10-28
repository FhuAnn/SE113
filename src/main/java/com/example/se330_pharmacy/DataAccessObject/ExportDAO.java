
package com.example.se330_pharmacy.DataAccessObject;

import com.example.se330_pharmacy.Models.ConnectDB;
import com.example.se330_pharmacy.Models.DetailExport;
import com.example.se330_pharmacy.Models.ExportForm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ExportDAO {
    final private ConnectDB connectDB = ConnectDB.getInstance();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public ExportDAO() {}

    public String addData(String employee, String reason, String totalPrice) {
        String sql = "INSERT INTO ExportForm (Employee_id, ExportReason, ExportDate, TotalMoney) " +
                "VALUES (?, ?, ?, ?) RETURNING exportform_id";

        try (PreparedStatement stmt = connectDB.databaseLink.prepareStatement(sql)) {
            stmt.setInt(1, Integer.parseInt(employee));
            stmt.setString(2, reason);
            stmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now())); // Set the current timestamp
            stmt.setInt(4, Integer.parseInt(totalPrice));

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return String.valueOf(rs.getInt(1));
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void addDetailData(int productID, String exportID, int quantity, double price, double totalPrice) {
        String sql = "INSERT INTO DetailExportForm (Product_id, Exportform_id, Price, Quantity,  total) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connectDB.databaseLink.prepareStatement(sql)) {

            stmt.setInt(1, productID);
            stmt.setInt(2, Integer.parseInt(exportID));
            stmt.setDouble(3, price);
            stmt.setInt(4, quantity);
            stmt.setDouble(5, totalPrice);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateProduct(int quantity, int productID) {
        String sql = "UPDATE Product SET big_unit = big_unit - ? WHERE Product_id = ?";

        try (PreparedStatement stmt = connectDB.databaseLink.prepareStatement(sql)) {
            stmt.setInt(1, quantity);
            stmt.setInt(2, productID);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<ExportForm> getAllExportForm() {
        ObservableList<ExportForm> forms = FXCollections.observableArrayList();
        String query = "SELECT exportform_id , employname, exportreason, exportdate, totalmoney " +
                "FROM exportform, employee WHERE exportform.employee_id = employee.employee_id";

        try (PreparedStatement preparedStatement = connectDB.databaseLink.prepareStatement(query);
             ResultSet rs = preparedStatement.executeQuery()) {

            while (rs.next()) {
                ExportForm form = new ExportForm();
                form.setExportFormId(rs.getInt(1));
                form.setEmployeeName(rs.getString(2));
                form.setExportReason(rs.getString(3));
                form.setExportDate(rs.getString(4));
                form.setTotalMoney(rs.getLong(5));
                forms.add(form);
            }
        } catch (Exception e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
        return forms;
    }

    public ObservableList<DetailExport> getExportById(int id) {
        ObservableList<DetailExport> detailExports = FXCollections.observableArrayList();

        String query = "SELECT pro.product_id, productname, dtex.price, dtex.quantity, unit.big_unit, dtex.total " +
                "FROM existedproduct pro, unit, detailexportform dtex " +
                "WHERE dtex.product_id = pro.product_id AND pro.unit_id = unit.unit_id AND dtex.exportform_id = ?";

        try (PreparedStatement statement = connectDB.databaseLink.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                DetailExport detailExport = new DetailExport();
                detailExport.setExportProductId(rs.getInt(1));
                detailExport.setExportProductName(rs.getString(2));
                detailExport.setExportProductPrice(rs.getInt(3));
                detailExport.setExportProductQuan(rs.getInt(4));
                detailExport.setExportProductUnit(rs.getString(5));
                detailExport.setExportTotal(rs.getLong(6));
                detailExports.add(detailExport);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return detailExports;
    }

    public boolean autoCreateReceipt(int employeeId, String content, int totalPay, String status, String note) {
        LocalDateTime dateTime = LocalDateTime.parse(LocalDateTime.now().format(formatter), formatter);
        String sqlQuery = "INSERT INTO receipt (employee_id, content, createDate, totalpay, status, note) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connectDB.databaseLink.prepareStatement(sqlQuery)) {
            preparedStatement.setInt(1, employeeId);
            preparedStatement.setString(2, content);
            preparedStatement.setTimestamp(3, Timestamp.valueOf(dateTime));
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
