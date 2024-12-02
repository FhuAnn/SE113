package com.example.se330_pharmacy.Controllers;

import com.example.se330_pharmacy.DataAccessObject.EmployeeDAO;
import com.example.se330_pharmacy.DataAccessObject.PayslipDAO;
import com.example.se330_pharmacy.Models.Employee;
import com.example.se330_pharmacy.Models.Model;
import com.example.se330_pharmacy.Models.Payslip;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class PaySlipController implements Initializable {

    public DatePicker dateTimePickerPayslip;
    public ComboBox<String> cbStatusPayslip;
    public TitledPane tlt_Employee;
    public TableView<Employee> employeeTableView;
    public TableView<Payslip> tblPaySlip;
    public TableColumn<Employee, Integer> idColumn;
    public TableColumn<Employee, String> nameColumn;
    public TableColumn<Employee, String> positionColumn;
    public TableColumn<Payslip,Integer> col_maPhieuLuong;
    public TableColumn <Payslip,String> col_tenNhanVien;
    public TableColumn<Payslip,String> col_noiDung;
    public TableColumn<Payslip,Double> col_tongTra;
    public TableColumn<Payslip,String> col_trangThai;
    public TableColumn<Payslip, String> col_ngayLap;
    public Button btnExport;
    public Button btnEdit;
    public Button btnDelete;
    public Button btnAdd;
    public Button btnTatCa ;
    PayslipDAO payslipDAO;
    EmployeeDAO employeeDAO;
    Employee employee;
    ObservableList<Payslip> payslips;
    ObservableList<Employee> employees;

    public PaySlipController() {
    }

    public void initData(Employee _employee)
    {
        employee=_employee;
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Setup();
        SetupTableView();
        LoadListPayslip();
        SetActionListener();
    }
    private void SetActionListener() {
        cbStatusPayslip.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                tlt_Employee.setVisible(false);
                FilterByDate();
                FilterByStatus();
            }
        });
        dateTimePickerPayslip.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                tlt_Employee.setVisible(false);
                FilterByDate();
                FilterByStatus();
            }
        });
        btnExport.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                tlt_Employee.setVisible(false);
                if(!tblPaySlip.getSelectionModel().isEmpty())  {
                    Payslip payslip = tblPaySlip.getSelectionModel().getSelectedItem();
                    if(payslip.getStatus().equals("InComplete")) {
                        Model.getInstance().getViewFactory().showAddReceiptWindow(payslip,employee.getEmployeeId(),employee.getEmployeeName(),employee.getEmployeePosition(),PaySlipController.this,null,null);
                        LoadListPayslip();
                    }
                    else {
                        showAlert("Warning","Đã thanh toán phiếu lương \""+payslip.getPayslip_id()+"\"!");
                    }
                }
            }
        });
        btnTatCa.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                tlt_Employee.setVisible(false);
                dateTimePickerPayslip.setValue(LocalDate.now());
                cbStatusPayslip.setValue(null);
                LoadListPayslip();
                FilterByDate();
                FilterByStatus();
            }
        });
        btnAdd.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                tlt_Employee.setVisible(false);
                Model.getInstance().getViewFactory().showAddPayslipWindow(null,PaySlipController.this);
            }
        });
        btnEdit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                tlt_Employee.setVisible(false);
                if(!tblPaySlip.getSelectionModel().isEmpty()&&tblPaySlip.getSelectionModel().getSelectedItem().getStatus().equals("InComplete")) Model.getInstance().getViewFactory().showAddPayslipWindow(tblPaySlip.getSelectionModel().getSelectedItem(),PaySlipController.this);
            }
        });
    }
    private void SetupTableView() {
        col_maPhieuLuong.setCellValueFactory(new PropertyValueFactory<>("payslip_id"));
        col_tenNhanVien.setCellValueFactory(new PropertyValueFactory<>("tenNhanVien"));
        col_noiDung.setCellValueFactory(new PropertyValueFactory<>("content"));
        col_ngayLap.setCellValueFactory(new PropertyValueFactory<>("createDate"));
        col_tongTra.setCellValueFactory(new PropertyValueFactory<>("totalPay"));
        col_trangThai.setCellValueFactory(new PropertyValueFactory<>("status"));

        idColumn.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("employeeName"));
        positionColumn.setCellValueFactory(new PropertyValueFactory<>("employeePosition"));
    }
    private void loadEmployeeData() {
        employees = employeeDAO.getAllEmployees();
        employeeTableView.setItems(employees);
    }
    private void Setup() {
        payslipDAO = new PayslipDAO();
        employeeDAO=new EmployeeDAO();
        dateTimePickerPayslip.setValue(LocalDate.from(LocalDateTime.now()));
        ObservableList<String> statusList = FXCollections.observableArrayList("InComplete", "Completed");
        cbStatusPayslip.setItems(statusList);
        loadEmployeeData();
    }

    private void FilterByDate() {
        if(payslips.isEmpty()) return;
        ObservableList<Payslip> filtered = FXCollections.observableArrayList();
        tblPaySlip.setItems(FXCollections.observableArrayList());
        for (Payslip payslip : payslips) {
            if (payslip.getCreateDate().toLocalDate().getYear() == dateTimePickerPayslip.getValue().getYear()) {
                if(payslip.getCreateDate().toLocalDate().getMonthValue() <= dateTimePickerPayslip.getValue().getMonthValue())
                    filtered.add(payslip);
            }
        }
        tblPaySlip.setItems(filtered);
        if(filtered.isEmpty())
        {
            showAlert("Warning", STR."Không có phiếu lương trong thang \{dateTimePickerPayslip.getValue().getMonthValue()} \\ \{dateTimePickerPayslip.getValue().getYear()} !");
        }
    }
    private void FilterByStatus() {
        if(tblPaySlip.getItems().isEmpty()||cbStatusPayslip.getValue()==null) return;
        ObservableList<Payslip> filtered = FXCollections.observableArrayList();
        ObservableList<Payslip> tblPayslipCurrent = tblPaySlip.getItems();
        for (Payslip payslip : tblPayslipCurrent) {
            if (payslip.getStatus().equals(cbStatusPayslip.getValue())) {
                filtered.add(payslip);
            }
        }
        tblPaySlip.setItems(filtered);
        if (filtered.isEmpty()) {
            showAlert("Warning", "Không có phiếu lương có trạng thái " + cbStatusPayslip.getValue());
        }
    }

    public void LoadListPayslip() {
        payslips = payslipDAO.GetPaySlipData();
        tblPaySlip.setItems(payslips);
        if(payslips.isEmpty())
        {
            showAlert("Warning","Danh sách trống!");
        }
    }

    private void showAlert(String tilte,String string) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(tilte);
        alert.setHeaderText(null);
        alert.setContentText(string);
        alert.showAndWait();
    }

    public void handleButtonShowList(ActionEvent event) {
        tlt_Employee.setVisible(true);
    }

    public void close(MouseEvent mouseEvent) {
        tlt_Employee.setVisible(false);
    }
}
