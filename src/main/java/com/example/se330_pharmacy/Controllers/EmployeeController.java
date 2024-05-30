package com.example.se330_pharmacy.Controllers;

import com.example.se330_pharmacy.DataAccessObject.EmployeeDAO;
import com.example.se330_pharmacy.Models.Employee;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import javax.swing.*;
import java.net.URL;
import java.text.Normalizer;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static java.util.Collections.replaceAll;

public class EmployeeController implements Initializable {
    public Button btnDeleteEmployee;
    public Button btnAddEmployee;
    public Button btnEditEmployee;
    public Button btnCancel;
    public ComboBox<String> cb_position;
    @FXML
    private TextField tfEmployee;
    @FXML
    private TextField tf_addName;
    @FXML
    private TextField tf_addcitizenId;
    @FXML
    private TextField tf_addAddress;
    @FXML
    private TextField tf_addPhoneNum;
    @FXML
    private TextField tf_addEmail;
    @FXML
    private TextField tf_addPosition;
    public TextField tf_maNV;
    @FXML
    private TableView<Employee> employeeTableView;
    @FXML
    private TableColumn<Employee, Integer> idColumn;
    @FXML
    private TableColumn<Employee, String> nameColumn;
    @FXML
    private TableColumn<Employee, String> citizenIdColumn;
    @FXML
    private TableColumn<Employee, String> addressColumn;
    @FXML
    private TableColumn<Employee, String> phoneNumColumn;
    @FXML
    private TableColumn<Employee, String> emailColumn;
    @FXML
    private TableColumn<Employee, String> positionColumn;
    @FXML
    private TableColumn<Employee, String> usernameColumn;

    private EmployeeDAO employeeDAO = new EmployeeDAO();
    private ObservableList<Employee> employees;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configureTableColumns();
        loadEmployeeData();
        setOnOffAddDeleteBtn();
        btnDeleteEmployee.setOnAction(event -> handleDeleteAction());
        btnAddEmployee.setOnAction(event -> handleAddAction());
        btnEditEmployee.setOnAction(event -> handleEditAction()); // Add this line to handle edit action
        btnCancel.setOnAction(event -> handleCancel());
        tfEmployee.setOnKeyPressed(event -> handleSearchKeyPressed(event));
        ObservableList<String> statusList = FXCollections.observableArrayList("Bán hàng", "Kế toán","Quản lí kho");
        cb_position.setItems(statusList);
        addSelectionListener(); // Add this line to handle selection changes
        SetTextChanged();
    }

    private void SetTextChanged() {
        tf_addPhoneNum.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                tf_addPhoneNum.setText(newValue.replaceAll("[^\\d]", oldValue));
                showAlert("Warning","Chỉ được nhập số");
            } else {
                if (!newValue.isEmpty() && newValue.charAt(0) != '0') tf_addPhoneNum.setText(oldValue);
            }

        });
        tf_addcitizenId.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                tf_addcitizenId.setText(newValue.replaceAll("[^\\d]", ""));
                showAlert("Warning", "Chỉ được nhập số");
            }
        });
    }

    private void setOnOffAddDeleteBtn() {
        btnDeleteEmployee.setDisable(true); // Bắt đầu bằng việc vô hiệu hóa nút Delete

    }

    private void configureTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("employeeName"));
        citizenIdColumn.setCellValueFactory(new PropertyValueFactory<>("employeeCitizenId"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("employeeAddress"));
        phoneNumColumn.setCellValueFactory(new PropertyValueFactory<>("employeePhoneNumber"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("employeeEmail"));
        positionColumn.setCellValueFactory(new PropertyValueFactory<>("employeePosition"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("employeeUsername"));
    }

    private void loadEmployeeData() {
        employees = employeeDAO.getAllEmployees();
        employeeTableView.setItems(employees);
    }

    @FXML
    private void handleSearchAction() {
        String searchText = normalizeString(tfEmployee.getText().trim().toLowerCase());
        if (!searchText.isEmpty()) {
            ObservableList<Employee> filteredEmployees = FXCollections.observableArrayList(
                    employees.stream()
                            .filter(emp -> normalizeString(String.valueOf(emp.getEmployeeId()).toLowerCase()).startsWith(searchText)
                                    || normalizeString(emp.getEmployeeName().toLowerCase()).contains(searchText))
                            .collect(Collectors.toList())
            );
            employeeTableView.setItems(filteredEmployees);
        } else {
            employeeTableView.setItems(employees);
        }
    }

    private void handleDeleteAction() {
        Employee selectedEmployee = employeeTableView.getSelectionModel().getSelectedItem();
        if (selectedEmployee != null) {
            int sequence = ShowYesNoAlert("xoá "+selectedEmployee.getEmployeeName()+"");
            if(sequence==JOptionPane.YES_OPTION) {
                if (employeeDAO.deleteEmployee(selectedEmployee.getEmployeeId()))  {
                    employees.remove(selectedEmployee);
                    loadEmployeeData();
                }
            } else {}
        }
    }

    @FXML
    private void handleSearchKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            handleSearchAction();
        }
    }

    private void handleAddAction() {
        if(btnAddEmployee.getText().equals("Lưu")) {
            int id = Integer.parseInt(tf_maNV.getText());
            String name = tf_addName.getText();
            String citizenId = tf_addcitizenId.getText();
            String address = tf_addAddress.getText();
            String phoneNum = tf_addPhoneNum.getText();
            String email = tf_addEmail.getText();
            String position = tf_addPosition.getText();
            String username = tf_addEmail.getText();

            if (!name.isEmpty() && !citizenId.isEmpty() && !address.isEmpty() && !phoneNum.isEmpty() && !email.isEmpty() && !position.isEmpty()) {
                int sequence = ShowYesNoAlert("lưu "+name+"");
                if(sequence==JOptionPane.YES_OPTION) {
                    Employee employee = new Employee(id,name, citizenId, address, phoneNum, email, position, username);
                    if (employeeDAO.updateEmployee(employee)) {
                        btnAddEmployee.setText("Thêm");
                        cb_position.setVisible(false);
                        tf_addPosition.setVisible(true);
                        loadEmployeeData();
                        clearAddEmployeeFields();
                    } else {
                        showAlert("Warning", "Error!");
                    }
                } else {}
            }
        } else {
            String name = tf_addName.getText();
            String citizenId = tf_addcitizenId.getText();
            String address = tf_addAddress.getText();
            String phoneNum = tf_addPhoneNum.getText();
            String email = tf_addEmail.getText();
            String position = cb_position.getValue();
            String username = tf_addEmail.getText();

            if (!name.isEmpty() && !citizenId.isEmpty() && !address.isEmpty() && !phoneNum.isEmpty() && !email.isEmpty() && !cb_position.getValue().isEmpty()) {
                int sequence = ShowYesNoAlert("thêm "+name+"");
                if(sequence==JOptionPane.YES_OPTION) {
                    Employee newEmployee = new Employee(name, citizenId, address, phoneNum, email, position, username);
                    if(employeeDAO.addEmployee(newEmployee)) {
                        loadEmployeeData();
                        clearAddEmployeeFields();
                    }
                } else {}
            }
        }

    }

    private void handleEditAction() {
            Employee selectedEmployee = employeeTableView.getSelectionModel().getSelectedItem();
            if (selectedEmployee != null) {
                btnAddEmployee.setText("Lưu");
                btnAddEmployee.setDisable(false);
                SetDisable(false);
            }
    }
    private void handleCancel() {
        clearAddEmployeeFields();
        SetDisable(false);
        btnAddEmployee.setDisable(false);
        btnDeleteEmployee.setDisable(true);
        btnEditEmployee.setDisable(true);
        cb_position.setVisible(true);
        tf_addPosition.setVisible(false);
        btnAddEmployee.setText("Thêm");
    }
    private void addSelectionListener() {
            employeeTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                boolean employeeSelected = newValue != null;
                SetDisable(true);
                cb_position.setVisible(false);
                tf_addPosition.setVisible(true);
                btnDeleteEmployee.setDisable(false);
                btnAddEmployee.setDisable(true);
                btnEditEmployee.setDisable(false);
                if (employeeSelected) {
                    // Hiển thị thông tin nhân viên lên các TextField
                    tf_addName.setText(newValue.getEmployeeName());
                    tf_addcitizenId.setText(newValue.getEmployeeCitizenId());
                    tf_addAddress.setText(newValue.getEmployeeAddress());
                    tf_addPhoneNum.setText(newValue.getEmployeePhoneNumber());
                    tf_addEmail.setText(newValue.getEmployeeEmail());
                    tf_addPosition.setText(newValue.getEmployeePosition());
                    tf_maNV.setText(String.valueOf(newValue.getEmployeeId()));
                } else {
                clearAddEmployeeFields();
            }
        });
        tfEmployee.textProperty().addListener((observable, oldValue,newValue )-> {
            if(tfEmployee.getText().isEmpty()) {
                employeeTableView.setItems(employees);
            }
        });
    }

    private void SetDisable(boolean bool) {
        tf_addAddress.setDisable(bool);
        tf_addEmail.setDisable(bool);
        tf_addPosition.setDisable(bool);
        tf_addcitizenId.setDisable(bool);
        tf_addPhoneNum.setDisable(bool);
        tf_addName.setDisable(bool);
    }

    private void clearAddEmployeeFields() {
        tf_addName.clear();
        tf_addcitizenId.clear();
        tf_addAddress.clear();
        tf_addPhoneNum.clear();
        tf_addEmail.clear();
        tf_addPosition.clear();
        tf_maNV.clear();
    }
    private static String normalizeString(String str) {
        return Normalizer.normalize(str, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }
    private void showAlert(String tilte,String string) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(tilte);
        alert.setHeaderText(null);
        alert.setContentText(string);
        alert.showAndWait();
    }
    private int ShowYesNoAlert(String string) {
        JFrame frame = new JFrame("Table Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        return JOptionPane.showConfirmDialog(frame, "Có phải bạn muốn "+string+"?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
    }
}


