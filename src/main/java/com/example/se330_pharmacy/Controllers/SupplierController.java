package com.example.se330_pharmacy.Controllers;

import com.example.se330_pharmacy.DataAccessObject.SupplierDAO;
import com.example.se330_pharmacy.Models.Employee;
import com.example.se330_pharmacy.Models.Supplier;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class SupplierController implements Initializable {
    @FXML
    private Button btnAdd;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnEdit;

    @FXML
    private TextArea taAddress;

    @FXML
    private TableColumn<Supplier, String> tcSupplierAddress;

    @FXML
    private TableColumn<Supplier, String> tcSupplierEmail;

    @FXML
    private TableColumn<Supplier, String> tcSupplierName;

    @FXML
    private TableColumn<Supplier, String> tcSupplierPhone;

    @FXML
    private TableColumn<Supplier, Integer> tcsupplierId;

    @FXML
    private TextField tfPhone;

    @FXML
    private TextField tfSupplierEmail;

    @FXML
    private TextField tfSupplierID;

    @FXML
    private TextField tfSuppliierName;

    @FXML
    private TableView<Supplier> tvSupplierTable;
    final private SupplierDAO supplierDAO = new SupplierDAO();
    Supplier selected;

    Employee _employee;

    @FXML
    void btnAdd(MouseEvent event) {

        if (btnAdd.getText().equals("Thêm") && areFieldsFilled() && selected !=null) {
            tfSuppliierName.setDisable(false);
            tfPhone.setDisable(false);
            tfSupplierEmail.setDisable(false);
            taAddress.setDisable(false);
            btnEdit.setDisable(true);
            btnDelete.setDisable(true);
            selected = null;
            clearField();
        }
        else if (btnAdd.getText().equals("Thêm") && !areFieldsFilled() && tfSuppliierName.isDisable()) {
            tfSuppliierName.setDisable(false);
            tfPhone.setDisable(false);
            tfSupplierEmail.setDisable(false);
            taAddress.setDisable(false);
        }
        else if (areFieldsFilled()) {
            if (!isValidEmail(tfSupplierEmail.getText())) {
                // Hiển thị thông báo lỗi nếu email không hợp lệ
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Input Error");
                alert.setHeaderText(null);
                alert.setContentText("Hãy nhập email đúng định dạng");
                alert.showAndWait();
                return;  // Dừng quá trình thêm hoặc cập nhật
            }
            else if(btnAdd.getText().equals("Lưu")) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Lưu thông tin đã chỉnh sửa ?", ButtonType.OK, ButtonType.CANCEL);
                Optional<ButtonType> result = alert.showAndWait();
                if(event.getButton() == MouseButton.PRIMARY && result.get() == ButtonType.OK) {
                    selected.setPartnername(tfSuppliierName.getText());
                    selected.setEmail(tfSupplierEmail.getText());
                    selected.setPhonenumber(tfPhone.getText());
                    selected.setAddress(taAddress.getText());


                    Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION,"Lưu thành công!", ButtonType.OK);
                    alert1.showAndWait();

                    new Thread(() -> {
                        supplierDAO.updateSupplier(selected, () -> {
                            Platform.runLater(() -> {
                                tvSupplierTable.refresh();
                                clearField();
                            });
                        });
                    }).start();

                    btnAdd.setText("Thêm");
                    btnAdd.setDisable(false);
                    tfSuppliierName.setDisable(true);
                    tfPhone.setDisable(true);
                    tfSupplierEmail.setDisable(true);
                    taAddress.setDisable(true);
                }
            } else {
                Supplier newSupplier = new Supplier( tfSuppliierName.getText(), taAddress.getText(), tfPhone.getText(), tfSupplierEmail.getText());
                new Thread(() -> {
                    supplierDAO.insertSupplier(newSupplier, () -> {
                        Platform.runLater(() -> {
                            tvSupplierTable.setItems(supplierDAO.getSuppliers());
                            clearField();
                        });
                    });
                }).start();
                Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION,"Thêm thành công!", ButtonType.OK);
                alert1.showAndWait();
                btnAdd.setDisable(false);
                tfSuppliierName.setDisable(true);
                tfPhone.setDisable(true);
                tfSupplierEmail.setDisable(true);
                taAddress.setDisable(true);
            }
        } else {
            // Display an error message if fields are not filled
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input Error");
            alert.setHeaderText(null);
            alert.setContentText("Hãy điền thông tin đầy đủ trước khi lưu!");
            alert.showAndWait();
        }

    }

    private void validatePhoneNumberInput() {
        tfPhone.textProperty().addListener((observable, oldValue, newValue) -> {
            // Loại bỏ bất kỳ ký tự không phải là số
            String formattedPhoneNumber = newValue.replaceAll("[^\\d]", "");

            // Kiểm tra điều kiện:
            // 1. Bắt đầu bằng 0.
            // 2. Chiều dài tối đa là 10 ký tự.
            if (formattedPhoneNumber.length() == 0 || formattedPhoneNumber.startsWith("0")) {
                if (formattedPhoneNumber.length() <= 10) {
                    tfPhone.setText(formattedPhoneNumber);
                } else {
                    // Nếu chiều dài vượt quá 10 ký tự, cắt chuỗi thành 10 ký tự đầu tiên
                    tfPhone.setText(formattedPhoneNumber.substring(0, 10));
                }
            } else {
                // Nếu không bắt đầu bằng 0, giữ nguyên giá trị cũ
                tfPhone.setText(oldValue);
            }
        });
    }

    private void validateEmailInput() {
        tfSupplierEmail.textProperty().addListener((observable, oldValue, newValue) -> {
            if (isValidEmail(newValue)) {
                tfSupplierEmail.setStyle("");  // Nếu email hợp lệ, bỏ đi bất kỳ kiểu cảnh báo nào
            } else {
                tfSupplierEmail.setStyle("-fx-border-color: red;");  // Nếu email không hợp lệ, đặt viền màu đỏ để cảnh báo
            }
        });
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
        return email.matches(emailRegex);
    }

    private void clearField() {
        tfSupplierID.clear();
        tfPhone.clear();
        tfSupplierEmail.clear();
        tfSuppliierName.clear();
        taAddress.clear();
    }

    private boolean areFieldsFilled() {
        return
                !tfPhone.getText().isEmpty() &&
                !tfSupplierEmail.getText().isEmpty() &&
                !tfSuppliierName.getText().isEmpty() &&
                !taAddress.getText().isEmpty();
    }

    @FXML
    void btnDetele(MouseEvent event) {
        Alert alert2 = new Alert(Alert.AlertType.CONFIRMATION, "Bạn chắc chắn muốn xóa?", ButtonType.OK, ButtonType.CANCEL);
        Optional<ButtonType> result = alert2.showAndWait();
        if(result.get() == ButtonType.OK){
            if (selected != null) {
                new Thread(() -> {
                    supplierDAO.deleteSupplier(selected, () -> {
                        Platform.runLater(() -> {
                            tvSupplierTable.setItems(supplierDAO.getSuppliers());
                            clearField();
                        });
                    });
                }).start();
                Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION, "Xóa thành công!", ButtonType.OK);
                alert1.showAndWait();
            }
        }
        btnDelete.setDisable(true);
        btnEdit.setDisable(true);
        btnAdd.setDisable(false);
    }
    @FXML
    void btnEdit(MouseEvent event) {
        btnAdd.setText("Lưu");
        btnAdd.setDisable(false);
        btnEdit.setDisable(true);
        btnDelete.setDisable(true);
        tfPhone.setDisable(false);
        tfSuppliierName.setDisable(false);
        tfSupplierEmail.setDisable(false);
        taAddress.setDisable(false);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpSupplier();
        validatePhoneNumberInput();
        validateEmailInput();
        tvSupplierTable.setOnMouseClicked(event -> {
            if(event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2 ) {
                selected = tvSupplierTable.getSelectionModel().getSelectedItem();

                if(selected!=null) {
                    tfSupplierID.setText(STR."\{selected.getPartner_id()}");
                    tfSuppliierName.setText(selected.getPartnername());
                    tfSupplierEmail.setText(selected.getEmail());
                    tfPhone.setText(selected.getPhonenumber());
                    taAddress.setText(selected.getAddress());
                    btnEdit.setDisable(false);
                    btnDelete.setDisable(false);
                    btnAdd.setDisable(false);
                }
            }
        });
    }

    private void setUpSupplier() {
        bindDataColumn();
        ObservableList<Supplier> suppliers = supplierDAO.getSuppliers();
        tvSupplierTable.setItems(suppliers);
    }

    private void bindDataColumn() {
        tcsupplierId.setCellValueFactory(new PropertyValueFactory<>("partner_id"));
        tcSupplierName.setCellValueFactory(new PropertyValueFactory<>("partnername"));
        tcSupplierAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        tcSupplierPhone.setCellValueFactory(new PropertyValueFactory<>("phonenumber"));
        tcSupplierEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
    }


    public void initData(Employee employee) {
        _employee = employee;
    }
}
