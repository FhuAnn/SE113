package com.example.se330_pharmacy.Controllers;

import com.example.se330_pharmacy.Models.Employee;
import com.example.se330_pharmacy.Models.Model;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class MenuController {

    @FXML
    private Button btnEmployee;

    @FXML
    private Button btnExport;

    @FXML
    private Button btnImport;

    @FXML
    private Button btnLogout;

    @FXML
    private MenuItem btnPayslip;

    @FXML
    private Button btnProduct;

    @FXML
    private MenuItem btnReceipt;

    @FXML
    private Button btnReport;

    @FXML
    private Button btnSale;

    @FXML
    private Pane mainPane;

    public MenuButton btnAccountant;
    public Pane paneProgress;
    @FXML
    private Text titleTextField;
    public Employee employee;

    public void initData(Employee _employee) {
        employee = _employee;
        btnSale.setDisable(true);
        btnEmployee.setDisable(true);
        btnImport.setDisable(true);
        btnExport.setDisable(true);
        btnAccountant.setDisable(true);
        btnReport.setDisable(true);
        btnProduct.setDisable(true);
        switch (employee.getEmployeePosition().trim()) {
            case "Bán hàng":
                btnSale.setDisable(false);
                break;
            case "Kế toán":
                btnAccountant.setDisable(false);
                break;
            case "Quản lí kho":
                btnImport.setDisable(false);
                btnExport.setDisable(false);
                break;
            case "Chủ tiệm":
                btnSale.setDisable(false);
                btnEmployee.setDisable(false);
                btnImport.setDisable(false);
                btnExport.setDisable(false);
                btnAccountant.setDisable(false);
                btnReport.setDisable(false);
                btnProduct.setDisable(false);
                break;
        }
    }

    @FXML
    void btnEmployeeClicked(ActionEvent event) throws IOException {
        titleTextField.setText("Nhân viên");
        setMainPane("/com/example/se330_pharmacy/Fxml/Employee.fxml");
    }

    @FXML
    void btnExportClicked(ActionEvent event) throws IOException {
        titleTextField.setText("Xuất hàng");
        FXMLLoader loader = setMainPane("/com/example/se330_pharmacy/Fxml/Export.fxml");
//        ExportController exportController = loader.getController();
//        exportController.initData(employee);
    }

    @FXML
    void btnSupplierClicked(MouseEvent event) throws IOException {
        titleTextField.setText("Nhà cung cấp");
        FXMLLoader loader = setMainPane("/com/example/se330_pharmacy/Fxml/Supplier.fxml");
//        SupplierController supplierController = loader.getController();
//        supplierController.initData(employee);
    }

    @FXML
    void btnImportClicked(ActionEvent event) throws IOException {
        titleTextField.setText("Nhập hàng");
        FXMLLoader loader = setMainPane("/com/example/se330_pharmacy/Fxml/Import.fxml");
//        ImportController importController = loader.getController();
//        importController.InitData(employee);
    }

    @FXML
    void btnLogoutClicked(MouseEvent event) {
        closeMenu(event);
    }

    @FXML
    void btnPayslipClicked(ActionEvent event) throws IOException {
        titleTextField.setText("Phiếu lương");
        FXMLLoader loader = setMainPane("/com/example/se330_pharmacy/Fxml/Accountant_PaySlip.fxml");
//        PaySlipController paySlipController = loader.getController();
//        paySlipController.initData(employee);
    }

    @FXML
    void btnProductCicked(ActionEvent event) throws IOException {
        titleTextField.setText("Sản phẩm");
        setMainPane("/com/example/se330_pharmacy/Fxml/Product.fxml");
    }

    @FXML
    void btnReceiptClicked(ActionEvent event) throws IOException {
        titleTextField.setText("Hóa đơn");
        FXMLLoader loader = setMainPane("/com/example/se330_pharmacy/Fxml/Accountant_Receipt.fxml");
//        ReceiptController receiptController = loader.getController();
//        receiptController.initData(employee);
    }

    @FXML
    void btnSaleClicked(ActionEvent event) throws IOException {
        titleTextField.setText("Bán hàng");
        FXMLLoader loader = setMainPane("/com/example/se330_pharmacy/Fxml/Sale.fxml");
//        SaleController saleController = loader.getController();
//        saleController.initData(employee);
    }

    @FXML
    void btnReportClicked(ActionEvent event) {
        titleTextField.setText("Báo cáo");
        paneProgress.setVisible(true);
        new Thread(() -> {
            try {
                Thread.sleep(700);
                Platform.runLater(() -> {
                    try {
                        setMainPane("/com/example/se330_pharmacy/Fxml/Report.fxml");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                Platform.runLater(() -> {
                    paneProgress.setVisible(false);
                });
            }
        }).start();
    }

    @FXML
    void closeMenu(MouseEvent event) {
        Stage s = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Đăng Xuất");
        confirmationAlert.setContentText("Bạn có muốn đăng xuất không?");

        ButtonType okButton = new ButtonType("OK");
        ButtonType cancelButton = ButtonType.CANCEL;

        confirmationAlert.getButtonTypes().setAll(okButton, cancelButton);

        Optional<ButtonType> result = confirmationAlert.showAndWait();

        if (result.isPresent() && result.get() == okButton) {
            Model.getInstance().getViewFactory().showLoginWindow();
            // Đóng cửa sổ hiện tại
            s.close();
        }
    }

    @FXML
    void minimizeMenu(MouseEvent event) {
        Stage s = (Stage) ((Node) event.getSource()).getScene().getWindow();
        s.setIconified(true);
    }

    public FXMLLoader setMainPane(String resource) throws IOException {
        mainPane.getChildren().clear();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(resource));
        Parent reportSceneRoot = loader.load();
        mainPane.getChildren().add(reportSceneRoot);
        return loader;
    }

    public void ProfileEmploy_Clicked(MouseEvent mouseEvent) {
        String id = String.valueOf(this.employee.getEmployeeId());
        String name = this.employee.getEmployeeName();
        String username = this.employee.getEmployeeUsername();
        String pos = this.employee.getEmployeePosition();
        Model.getInstance().getViewFactory().showProfileWindow(id, name, username, pos);
    }
}
