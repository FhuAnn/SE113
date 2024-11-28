package com.example.se330_pharmacy.Controllers;

import com.example.se330_pharmacy.DataAccessObject.ReceiptDAO;
import com.example.se330_pharmacy.Models.Employee;
import com.example.se330_pharmacy.Models.Model;
import com.example.se330_pharmacy.Models.Receipt;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ReceiptController implements Initializable {
    public DatePicker dateTimePickerReceipt;
    public ComboBox<String> cbStatusReceipt;
    public TableView<Receipt> tblReceipt;
    public TableColumn<Receipt,Integer> col_maHoaDon;
    public TableColumn<Receipt,String> col_noiDung;
    public TableColumn<Receipt,Double> col_tongTra;
    public TableColumn<Receipt,String> col_trangThai;
    public TableColumn<Receipt,String> col_ngayLap;
    public TableColumn<Receipt,String> col_nguoiTra;
    public TableColumn<Receipt, String> col_tenNhanVien;
    public Button btnTatCa;
    public Button btnEdit;
    ReceiptDAO receiptDAO;
    ObservableList<Receipt> receipts;
    Employee employee_init;
    public void initData(Employee _employee) {
        employee_init = _employee;
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Setup();
        SetUpTableView();
        SetListener();
        LoadListReceipt();
    }

    public void LoadListReceipt() {
        receipts=receiptDAO.GetReceiptsData();
        tblReceipt.setItems(receipts);
        if(receipts.isEmpty())
        {
            showAlert("Warning","Danh sách trống!");
        }
    }

    private void SetListener() {
        btnTatCa.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dateTimePickerReceipt.setValue(LocalDate.now());
                cbStatusReceipt.setValue(null);
                FilterByDate();
                FilterByStatus();
                LoadListReceipt();
            }
        });
        dateTimePickerReceipt.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FilterByDate();
                FilterByStatus();
            }
        });
        cbStatusReceipt.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FilterByDate();
                FilterByStatus();
            }
        });
        btnEdit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Receipt receipt = tblReceipt.getSelectionModel().getSelectedItem();
                if(receipt!=null) {
                    Model.getInstance().getViewFactory().showAddReceiptWindow(null,employee_init.getEmployeeId(),employee_init.getEmployeeName(),employee_init.getEmployeePosition(),null,ReceiptController.this,receipt);
                }
            }
        });
    }

    private void FilterByStatus() {
            if(tblReceipt.getItems().isEmpty()||cbStatusReceipt.getValue()==null) return;
            ObservableList<Receipt> filtered = FXCollections.observableArrayList();
            ObservableList<Receipt> tblPayslipCurrent = tblReceipt.getItems();
            for (Receipt receipt : tblPayslipCurrent) {
                if (receipt.getStatus().equals(cbStatusReceipt.getValue())) {
                    filtered.add(receipt);
                }
            }
            tblReceipt.setItems(filtered);
            if (filtered.isEmpty()) {
                showAlert("Warning", "Không có phiếu lương có trạng thái " + cbStatusReceipt.getValue());
            }
    }

    private void Setup() {
        receiptDAO = new ReceiptDAO();
        ObservableList<String> statusList = FXCollections.observableArrayList("InComplete", "Completed");
        cbStatusReceipt.setItems(statusList);
        dateTimePickerReceipt.setValue(LocalDate.now());

    }

    private void FilterByDate() {
        if(receipts.isEmpty()) return;
        ObservableList<Receipt> filtered = FXCollections.observableArrayList();
        tblReceipt.setItems(FXCollections.observableArrayList());
        for (Receipt receipt : receipts) {
            if (receipt.getCreateDate().toLocalDate().getYear() == dateTimePickerReceipt.getValue().getYear()) {
                if(receipt.getCreateDate().toLocalDate().getMonthValue() <= dateTimePickerReceipt.getValue().getMonthValue())
                    filtered.add(receipt);
            }
        }
        tblReceipt.setItems(filtered);
        if(filtered.isEmpty())
        {
            showAlert("Warning", STR."Không có phiếu lương trong \{dateTimePickerReceipt.getValue().getMonth()}\\\{dateTimePickerReceipt.getValue().getYear()}!");
        }
    }

    private void SetUpTableView() {
        col_maHoaDon.setCellValueFactory(new PropertyValueFactory<>("receipt_id"));
        col_tenNhanVien.setCellValueFactory(new PropertyValueFactory<>("tenNhanVien"));
        col_noiDung.setCellValueFactory(new PropertyValueFactory<>("content"));
        col_ngayLap.setCellValueFactory(new PropertyValueFactory<>("createDate"));
        col_tongTra.setCellValueFactory(new PropertyValueFactory<>("totalPay"));
        col_trangThai.setCellValueFactory(new PropertyValueFactory<>("status"));
        col_nguoiTra.setCellValueFactory(new PropertyValueFactory<>("tenNguoiTra"));
    }

    private void showAlert(String tilte,String string) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(tilte);
        alert.setHeaderText(null);
        alert.setContentText(string);
        alert.showAndWait();
    }
}
