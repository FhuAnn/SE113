package com.example.se330_pharmacy.DataAccessObject;

import com.example.se330_pharmacy.ForeignKeyViolationException;
import com.example.se330_pharmacy.Models.ConnectDB;
import com.example.se330_pharmacy.Models.Report;
import com.example.se330_pharmacy.Models.Unit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UnitDAO {
    ConnectDB connectDB = ConnectDB.getInstance();
    public ObservableList<Unit> getUnitData() {
        ObservableList<Unit> list = FXCollections.observableArrayList();
        String query = "SELECT unit_id, value, big_unit, small_unit FROM unit ";
        try(ResultSet rs = connectDB.getResultSet(query)) {
            while(rs.next()) {
                Unit unit = new Unit(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getString(3),
                        rs.getString(4)
                );
                list.add(unit);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    /*    public boolean Update(int value, String big_unit,String small_unit,int id) {
            String query = "UPDATE unit SET value = ?,big_unit = ?,small_unit = ? WHERE unit_id = ? ";
            try(PreparedStatement statement = connectDB.getPreparedStatement(query)) {
                statement.setInt(1,value);
                statement.setString(1,big_unit);
                statement.setString(1,small_unit);
                statement.setInt(4,id);
                return statement.executeUpdate()>0;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        }*/
    public boolean AddToDB(int value,String big_unit,String small_unit) {
        String query = "INSERT INTO unit(value, big_unit, small_unit) VALUES(?,?,?) ";
        try(PreparedStatement statement = connectDB.getPreparedStatement(query)) {
            statement.setInt(1,value);
            statement.setString(2,big_unit);
            statement.setString(3,small_unit);
            return statement.executeUpdate()>0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public Boolean  Delete(int id) throws ForeignKeyViolationException {
        String query = "DELETE FROM unit WHERE unit_id = ? ";
        try(PreparedStatement statement = connectDB.getPreparedStatement(query)) {
            statement.setInt(1,id);
            return statement.executeUpdate()>0;
        } catch (SQLException e) {
            if (e.getSQLState().equals("23503")) { // Mã lỗi cho vi phạm khóa ngoại
                throw new ForeignKeyViolationException("Không thể xóa đơn vị có mã "+id+" vì tồn tại sản phẩm có đơn vị này.");
            } else {
                e.printStackTrace();
            }
        }
        return false;
    }
}
