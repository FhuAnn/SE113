
package com.example.se330_pharmacy.DataAccessObject;

import com.example.se330_pharmacy.Models.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.converter.LocalDateTimeStringConverter;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ImportDAO {
    private ConnectDB connectDB = ConnectDB.getInstance();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public ImportDAO() {
    }

    public ObservableList<Product>  searchProductData(String search) {
        ObservableList<Product> products = FXCollections.observableArrayList();
        String sqlQuery = "SELECT pro.product_id , pro.productname , pro.price_import, pro.description, pro.origin, uni.big_unit, type.typename FROM product pro, producttype type, unit uni " +
                " WHERE pro.producttype_id = type.producttype_id AND uni.unit_id = pro.unit_id AND (unaccent(productname) ILIKE unaccent(?) ";
        boolean isIdSearch = false;
        try {
            int id = Integer.parseInt(search);
            sqlQuery += " OR pro.product_id = ?";
            isIdSearch = true;
        } catch(NumberFormatException e){
        }
        sqlQuery+=")";
        try (PreparedStatement statement= connectDB.databaseLink.prepareStatement(sqlQuery)) {
            statement.setString(1, "%" + search + "%");
            if (isIdSearch) {
                statement.setInt(2, Integer.parseInt(search));
            }
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Product product = new Product(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getInt(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getString(6),
                        resultSet.getString(7)
                );
                products.add(product);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public ObservableList<Supplier> getSupplierIdByName(String search) {
        String sqlQuery = "SELECT partner_id, partnername,address, phonenumber,email FROM partner WHERE unaccent(partnername) ILIKE unaccent(?)";
        ObservableList<Supplier> suppliers = FXCollections.observableArrayList();
        boolean isIdSearch = false;
        try {
            int id = Integer.parseInt(search);
            sqlQuery += " OR partner_id = ?";
            isIdSearch = true;
        } catch(NumberFormatException e){
        }
        try (PreparedStatement statement= connectDB.databaseLink.prepareStatement(sqlQuery)) {
            statement.setString(1, "%" + search + "%");
            if (isIdSearch) {
                statement.setInt(2, Integer.parseInt(search));
            }
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
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

    public int addImportData(Import _import) {
        LocalDateTime dateTime = LocalDateTime.parse(LocalDateTime.now().format(formatter),formatter);
        String sqlQuery = "INSERT INTO importform (employee_id, partner_id, formdate, totalmoney) " +
                "VALUES (?, ?, ?, ?)  RETURNING importform_id";

        try {
            PreparedStatement preparedStatement = connectDB.databaseLink.prepareStatement(sqlQuery);
            preparedStatement.setInt(1, _import.getEmployeeId());
            preparedStatement.setInt(2, _import.getSupplierId());
            preparedStatement.setObject(3, dateTime);
            preparedStatement.setFloat(4, _import.getTotal());
            return connectDB.getId(preparedStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    public boolean AddDetailtoDB(Import import_) {
        String query = "INSERT INTO detailimportform (importform_id,product_id,importprice,quantity,totalprice) VALUES(?,?,?,?,?) ";
        try(PreparedStatement statement = connectDB.databaseLink.prepareStatement(query)) {
            statement.setInt(1,import_.getImportId());
            statement.setInt(2,import_.getProductId());
            statement.setInt(3,import_.getImportPrice());
            statement.setInt(4,import_.getQuantity());
            statement.setInt(5,import_.getTotalPrice());
            int aff = statement.executeUpdate();
            if(aff>0) return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public ObservableList<Import> getHistoryImport()
    {
        ObservableList<Import> listImport = FXCollections.observableArrayList();
        String query = "SELECT im.importform_id, emp.employname, p.partnername, im.formdate  FROM importform im, employee emp,partner p WHERE im.partner_id = p.partner_id AND im.employee_id = emp.employee_id ";
        try(PreparedStatement statement = connectDB.getPreparedStatement(query))
        {
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                Import import_ = new Import(
                        resultSet.getInt(1),
                        resultSet.getString(4),
                        resultSet.getString(2),
                        resultSet.getString(3)
                );
                listImport.add(import_);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listImport;
    }
    public ObservableList<Import> getDetailFromImportId(int importId) {
        ObservableList<Import> list = FXCollections.observableArrayList();
        String query = "SELECT pro.product_id, pro.productname,dtl.importprice, dtl.quantity,dtl.totalprice FROM existedproduct pro, importform im, detailimportform dtl WHERE im.importform_id = "+importId+" AND dtl.importform_id = im.importform_id AND pro.product_id = dtl.product_id";
        try(PreparedStatement statement = connectDB.getPreparedStatement(query)){
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                Import import_ = new Import(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getInt(3),
                        resultSet.getInt(4),
                        resultSet.getInt(5));
                list.add(import_);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public boolean updateProduct(int quantity, int productID) {
        String sql = "UPDATE Product SET big_unit = big_unit + ? WHERE product_id = ?";

        try (PreparedStatement stmt = connectDB.databaseLink.prepareStatement(sql)) {

            stmt.setInt(1, quantity);
            stmt.setInt(2, productID);
            return stmt.executeUpdate() >0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
