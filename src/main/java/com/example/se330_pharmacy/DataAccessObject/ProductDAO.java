package com.example.se330_pharmacy.DataAccessObject;

import com.example.se330_pharmacy.ForeignKeyViolationException;
import com.example.se330_pharmacy.Models.ConnectDB;
import com.example.se330_pharmacy.Models.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductDAO {
    private final ConnectDB connectDB = ConnectDB.getInstance();
    public ProductDAO() {
    }
    public ObservableList<Product> getAllProduct() {
        ObservableList<Product> products = FXCollections.observableArrayList();
        String query = "SELECT product_id , productname, price_import, product.small_unit , unit.small_unit, " +
                "product.big_unit, unit.big_unit, unit.value, description, origin, pty.typename  FROM product,unit, producttype pty WHERE product.unit_id = unit.unit_id AND product.producttype_id = pty.producttype_id ORDER BY product.product_id ASC ";

        try (ResultSet rs = connectDB.getResultSet(query)) {
            while (rs.next()) {
                Product product = new Product();
                product.setProductId(rs.getInt(1));
                product.setProductName(rs.getString(2));
                product.setProductImportPrice(rs.getInt(3));
                product.setProductSmallUnit(rs.getString(5));
                product.setProductSmallUnitQuantities(rs.getInt(4));
                product.setProductBigUnit(rs.getString(7));
                product.setProductBigUnitQuantities(rs.getInt(6));
                product.setProductCoef(rs.getInt(8));
                product.setProductDescription(rs.getString(9));
                product.setProductOrigin(rs.getString(10));
                product.setProductType(rs.getString(11));
                products.add(product);
            }
        } catch (Exception e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
        return products;
    }


    public ObservableList<Product> getAllProductExport() {
        ObservableList<Product> products = FXCollections.observableArrayList();
        String query = "SELECT product.product_id , productname, price_import, product.description ,product.origin,  unit.big_unit ,producttype.typename,product.big_unit FROM product,unit,producttype " +
                "WHERE product.unit_id = unit.unit_id AND product.producttype_id = producttype.producttype_id ORDER BY product.product_id asc";

        try (PreparedStatement preparedStatement = connectDB.databaseLink.prepareStatement(query);
             ResultSet rs = preparedStatement.executeQuery()) {

            while (rs.next()) {
                Product product = new Product();
                product.setProductId(rs.getInt(1));
                product.setProductName(rs.getString(2));
                product.setProductImportPrice(rs.getInt(3));
                product.setProductDescription(rs.getString(4));
                product.setProductOrigin(rs.getString(5));
                product.setProductBigUnit(rs.getString(6));
                product.setProductType(rs.getString(7));
                product.setProductBigUnitQuantities(rs.getInt(8));
                products.add(product);
            }
        } catch (Exception e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
        return products;
    }

    public ObservableList<Product> getAllProductBigQuantity() {
        ObservableList<Product> products = FXCollections.observableArrayList();
        String query = "Select product_id ,product.big_unit from product order by product_id asc";

        try (PreparedStatement preparedStatement = connectDB.databaseLink.prepareStatement(query);
             ResultSet rs = preparedStatement.executeQuery()) {

            while (rs.next()) {
                Product product = new Product();
                product.setProductId(rs.getInt(1));
                product.setProductBigUnitQuantities(rs.getInt(2));
                products.add(product);
            }
        } catch (Exception e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
        return products;
    }
    public ObservableList<Product> searchProduct(String searchString) {
        ObservableList<Product> products = FXCollections.observableArrayList();
        String query = "SELECT pro.product_id , pro.productname, pro.price_import, pro.small_unit , unit.small_unit, " +
                "pro.big_unit, unit.big_unit, unit.value, pro.description,pro.origin, pty.typename  FROM product pro,unit, producttype pty WHERE pro.unit_id = unit.unit_id AND pro.producttype_id = pty.producttype_id " +
                "AND  ( unaccent(pro.productname) ILIKE unaccent(?) ";
        boolean isIdSearch = false;
        try {
            int id = Integer.parseInt(searchString);
            query += " OR pro.product_id = ?";
            isIdSearch = true;
        } catch(NumberFormatException e){
        }
        query+=")";
        try (PreparedStatement statement = connectDB.getPreparedStatement(query)) {
            statement.setString(1,"%"+searchString+"%");
            if (isIdSearch) {
                statement.setInt(2, Integer.parseInt(searchString));
            }
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Product product = new Product();
                product.setProductId(rs.getInt(1));
                product.setProductName(rs.getString(2));
                product.setProductImportPrice(rs.getInt(3));
                product.setProductSmallUnit(rs.getString(5));
                product.setProductSmallUnitQuantities(rs.getInt(4));
                product.setProductBigUnit(rs.getString(7));
                product.setProductBigUnitQuantities(rs.getInt(6));
                product.setProductCoef(rs.getInt(8));
                product.setProductDescription(rs.getString(9));
                product.setProductOrigin(rs.getString(10));
                product.setProductType(rs.getString(11));
                products.add(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return products;
    }

    public List<String> getProductTypes() {
        List<String> productTypes = new ArrayList<>();
        String query = "SELECT typename FROM producttype";

        try (ResultSet rs = connectDB.getResultSet(query)) {
            while (rs.next()) {
                productTypes.add(rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productTypes;
    }

    public int getTypeString(String name) {
        String sqlQuery = "SELECT producttype_id FROM producttype WHERE typename = ?";
        try (PreparedStatement preparedStatement = connectDB.getPreparedStatement(sqlQuery)) {
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("producttype_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int getUnitId(int coef, String smallunit, String big_unit ) {
        String sqlQuery = "SELECT unit_id FROM unit WHERE  small_unit = ? AND big_unit = ? AND value = ?";
        try (PreparedStatement preparedStatement = connectDB.getPreparedStatement(sqlQuery)) {

            preparedStatement.setString(1, smallunit);
            preparedStatement.setString(2, big_unit);
            preparedStatement.setInt(3, coef);
            return connectDB.getId(preparedStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    public boolean addProduct(Product newProduct) {
        int typeid = getTypeString(newProduct.getProductType());
        int uni = getUnitId(newProduct.getProductCoef(), newProduct.getProductSmallUnit(), newProduct.getProductBigUnit());

        String productQuery = "INSERT INTO product (productname, price_import, description, origin, producttype_id, unit_id,big_unit,small_unit) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String existedProductQuery = "INSERT INTO existedproduct (productname, price_import, description, origin, producttype_id, unit_id,big_unit,small_unit) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connectDB.getPreparedStatement(productQuery);
             PreparedStatement preparedStatementExistedProduct = connectDB.getPreparedStatement(existedProductQuery)) {

            preparedStatement.setString(1, newProduct.getProductName());
            preparedStatement.setFloat(2, newProduct.getProductImportPrice());
            preparedStatement.setString(3, newProduct.getProductDescription());
            preparedStatement.setString(4, newProduct.getProductOrigin());
            preparedStatement.setInt(5, typeid);
            preparedStatement.setInt(6, uni);

            preparedStatementExistedProduct.setString(1, newProduct.getProductName());
            preparedStatementExistedProduct.setFloat(2, newProduct.getProductImportPrice());
            preparedStatementExistedProduct.setString(3, newProduct.getProductDescription());
            preparedStatementExistedProduct.setString(4, newProduct.getProductOrigin());
            preparedStatementExistedProduct.setInt(5, typeid);
            preparedStatementExistedProduct.setInt(6, uni);
            if (newProduct.getProductBigUnit().equals(newProduct.getProductSmallUnit())) {
                preparedStatement.setNull(8, Types.INTEGER);
                preparedStatementExistedProduct.setNull(8, Types.INTEGER);
            } else {
                preparedStatementExistedProduct.setInt(8, 0);
                preparedStatement.setInt(8, 0);
            }
            preparedStatementExistedProduct.setInt(7, 0);
            preparedStatement.setInt(7, 0);


            return preparedStatement.executeUpdate() > 0 && preparedStatementExistedProduct.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteProduct(int id) throws ForeignKeyViolationException {
        String sqlQuery = "DELETE FROM product WHERE product_id = ?";
        try (PreparedStatement preparedStatement = connectDB.getPreparedStatement(sqlQuery)) {
            preparedStatement.setInt(1,id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            if (e.getSQLState().equals("23503")) { // Mã lỗi cho vi phạm khóa ngoại
                throw new ForeignKeyViolationException("Không thể xóa sản phẩm vì dữ liệu sản phẩm có mã "+id+" được sử dụng ");
            } else {
                e.printStackTrace();
            }
            return false;
        }
    }

    public boolean updateProduct(Product product) {
        int typeid = getTypeString(product.getProductType());
        int uni = getUnitId(product.getProductCoef(), product.getProductSmallUnit(), product.getProductBigUnit());

        String sqlQuery = "UPDATE product SET productname = ?, price_import = ?, description = ?, origin = ?, unit_id = ?, producttype_id = ? WHERE product_id = ?";
        try (PreparedStatement preparedStatement = connectDB.getPreparedStatement(sqlQuery)) {
            preparedStatement.setString(1, product.getProductName());
            preparedStatement.setLong(2, product.getProductImportPrice());
            preparedStatement.setString(3, product.getProductDescription());
            preparedStatement.setString(4, product.getProductOrigin());
            preparedStatement.setInt(5, uni);
            preparedStatement.setInt(6, typeid);
            preparedStatement.setInt(7, product.getProductId());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getDataQuantity(int productId) {
        ConnectDB connect = new ConnectDB();
        String sqlQuery = "SELECT lv1Quantity FROM Product WHERE ProductId = ?";
        try (Connection connection = connect.getConnection();
             PreparedStatement ps = connection.prepareStatement(sqlQuery)) {
            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("lv1Quantity");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Or handle the case where no data is found
    }
}
