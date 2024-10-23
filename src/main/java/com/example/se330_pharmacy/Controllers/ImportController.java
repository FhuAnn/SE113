package com.example.se330_pharmacy.Controllers;

import com.example.se330_pharmacy.DataAccessObject.ProductDAO;
import com.example.se330_pharmacy.DataAccessObject.ReceiptDAO;
import com.example.se330_pharmacy.DataAccessObject.SupplierDAO;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import com.example.se330_pharmacy.DataAccessObject.ImportDAO;
import com.example.se330_pharmacy.Models.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static com.almasb.fxgl.dsl.FXGLForKtKt.showMessage;

public class ImportController implements Initializable {
    public TableView<Product> tbl_ProductTable;
    public TableColumn<Product,Integer> col_idProduct;
    public TableColumn<Product,String> col_nameProduct;
    public TableColumn<Product,Integer> col_priceProduct;
    public TableColumn<Product,String> col_descriptionProduct;
    public TableColumn<Product,String> col_originProduct;
    public TableColumn<Product,String> col_unitProduct;
    public TableColumn<Product,String> col_typeProduct;
    public TableColumn<Product,Integer> col_quantityProduct;
    public TableView<Import> tbl_DetailImportForm;
    public TableColumn<Import,Integer> col_idDetail;
    public TableColumn<Import,String> col_nameDetail;
    public TableColumn<Import,Integer> col_priceDetail;
    public TableColumn<Import,String> col_quantityDetail;
    public TableColumn<Import,String> col_totalDetail;
    public TableView<Supplier> tbl_Supplier;
    public TableColumn<Supplier,String> col_namePartner;
    public TableColumn<Product,String> col_address;
    public TableColumn<Product,Integer> col_phonenumberPartner;
    public TableView<Import> tbl_historyImport;
    public TableColumn<Import,Integer> col_idImport;
    public TableColumn<Import,String> col_employnameImport;
    public TableColumn<Import,String> col_supplierImport;
    public TableColumn<Import,String> col_dateImport;
    public TextField tfProductID;
    public TextField tfProductName;
    public TextField tfProductPrice;
    public TextField tfProductQuantities;
    public TextField tfProductTotal;
    public TextField tf_supplier;
    public Button btnAdd;
    public Button btnEdit;
    public Button btnCancel;
    public Button btnCreateForm;
    public Button btnDelete;
    public Button btnShow;
    public Button btnShowHistory;
    public TitledPane tp_left, tp_ImportHistory;
    public TextField tfFind;
    public Pane panelResultSupplier,panelImport;
    public Text lblTotalPay;
    public HBox hboxFind;
    public Label lbl_noContent;
    String message;
    private final ImportDAO importDAO = new ImportDAO();
    private final ProductDAO productDAO = new ProductDAO();
    private final ReceiptDAO receiptDAO = new ReceiptDAO();
    private final SupplierDAO supplierDAO = new SupplierDAO();
    private ObservableList<Product> ListProducts;
    private ObservableList<Supplier> ListSuppliers;
    Employee employee_init;
    Supplier supplierBeforeClicked;
    @FXML
    ImageView imgLogo;
    public void InitData(Employee _employee){
        employee_init = _employee;
    }
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SetUp();
        LoadImport();
        loadHistoryImport();
        AddListenner();
    }

    private void loadHistoryImport() {
        tbl_DetailImportForm.getItems().clear();
        ObservableList<Import> imports = importDAO.getHistoryImport();
        tbl_historyImport.setItems(imports);
    }

    private void LoadImport() {
        loadProducts();
        loadSupplier();
    }

    private void SetUp() {
        panelResultSupplier.setVisible(false);

        col_idProduct.setCellValueFactory(new PropertyValueFactory<>("productId"));
        col_nameProduct.setCellValueFactory(new PropertyValueFactory<>("productName"));
        col_priceProduct.setCellValueFactory(new PropertyValueFactory<>("productImportPrice"));
        col_descriptionProduct.setCellValueFactory(new PropertyValueFactory<>("productDescription"));
        col_originProduct.setCellValueFactory(new PropertyValueFactory<>("productOrigin"));
        col_unitProduct.setCellValueFactory(new PropertyValueFactory<>("productBigUnit"));
        col_typeProduct.setCellValueFactory(new PropertyValueFactory<>("productType"));
        col_quantityProduct.setCellValueFactory(new PropertyValueFactory<>("productBigUnitQuantities"));

        col_idDetail.setCellValueFactory(new PropertyValueFactory<>("productId"));
        col_nameDetail.setCellValueFactory(new PropertyValueFactory<>("productName"));
        col_priceDetail.setCellValueFactory(new PropertyValueFactory<>("importPrice"));
        col_quantityDetail.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        col_totalDetail.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

        col_namePartner.setCellValueFactory(new PropertyValueFactory<>("partnername"));
        col_address.setCellValueFactory(new PropertyValueFactory<>("address"));
        col_phonenumberPartner.setCellValueFactory(new PropertyValueFactory<>("phonenumber"));


        col_idImport.setCellValueFactory(new PropertyValueFactory<>("importId"));
        col_employnameImport.setCellValueFactory(new PropertyValueFactory<>("employName"));
        col_supplierImport.setCellValueFactory(new PropertyValueFactory<>("supplierName"));;
        col_dateImport.setCellValueFactory(new PropertyValueFactory<>("formDate"));
    }


    private void AddListenner() {
        tbl_ProductTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount()==2 ) {
                    if(!tbl_Supplier.getItems().isEmpty()&&CheckNotExistProductInImportTable(tbl_ProductTable.getSelectionModel().getSelectedItem().getProductId()))
                    {
                        FillProductTableToTextField();
                    } else showAlert("Warning",message);
                }
            }
        });
        tfFind.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.isEmpty()) tbl_ProductTable.setItems(ListProducts);
            }
        });
        tfFind.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                String searchString = tfFind.getText().trim();
                if (isValidInput(searchString)) {
                    SearchProduct(searchString);
                }
            }
        });
        tbl_DetailImportForm.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount()==2 &&tbl_DetailImportForm.getHeight()!=424) {
                    FillImportTableToTextField();
                }
            }
        });
        tbl_Supplier.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount()==1) {
                    if(!tbl_Supplier.getSelectionModel().isEmpty()) {
                        supplierBeforeClicked= tbl_Supplier.getSelectionModel().getSelectedItem();
                        tf_supplier.setText(supplierBeforeClicked.getPartnername());
                    }
                }
            }
        });
        tf_supplier.textProperty().addListener((observable,oldValue, newValue) -> {
            if(newValue.isEmpty()) tbl_Supplier.setItems(ListSuppliers);
        });
        tf_supplier.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                String searchString = tf_supplier.getText().trim();
                if (isValidInput(searchString)) {
                    SearchSupplier(searchString);
                }
            }
        });
        tfProductQuantities.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!newValue.isEmpty()) {
                    if (!newValue.matches("\\d*")) {
                        tfProductQuantities.setText(newValue.replaceAll("[^\\d]", ""));
                        showAlert("Warning","Chỉ nhập được nhập số!");
                    }
                    else {
                        try {
                            int total = Integer.parseInt(newValue)* Integer.parseInt(tfProductPrice.getText());
                            tfProductTotal.setText(String.valueOf(total));
                        } catch (NumberFormatException e) {
                            tfProductTotal.setText("Tổng tiền");
                        }

                    }
                }
            }
        });
        tbl_historyImport.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount()==2) {
                    int import_id = tbl_historyImport.getSelectionModel().getSelectedItem().getImportId();
                    ObservableList<Import> listDetailImport= importDAO.getDetailFromImportId(import_id);
                    tbl_DetailImportForm.setItems(listDetailImport);
                    if(listDetailImport.isEmpty()) lbl_noContent.setVisible(true);
                    else lbl_noContent.setVisible(false);
                }
            }
        });
    }

    private void SearchProduct(String searchString) {
        ObservableList<Product> lists = importDAO.searchProductData(searchString);
        tbl_ProductTable.setItems(lists);
    }

    private boolean CheckNotExistProductInImportTable(int id) {
        ObservableList<Import> listproducts = tbl_DetailImportForm.getItems();
        for(Import import_ : listproducts) {
            if(import_.getProductId() == id)  {
                message="Bạn thêm sản phẩm này vào danh sách!";
                return false;
            }
        }
        return true;
    }

    private void SearchSupplier(String searchString) {
        ObservableList<Supplier> listResult = importDAO.getSupplierIdByName(searchString);
        tbl_Supplier.setItems(listResult);
    }
    private void FillImportTableToTextField() {
        tfProductQuantities.setDisable(true);
        Import importProduct = tbl_DetailImportForm.getSelectionModel().getSelectedItem();
        tfProductID.setText(String.valueOf(importProduct.getImportId()));
        tfProductName.setText(importProduct.getProductName());
        tfProductPrice.setText(String.valueOf(importProduct.getImportPrice()));
        tfProductQuantities.setText(String.valueOf(importProduct.getQuantity()));
        tfProductTotal.setText(String.valueOf(importProduct.getTotal()));
    }
    private void FillProductTableToTextField() {
        tfProductQuantities.setDisable(false);
        btnEdit.setText("Sửa");
        btnEdit.setId("edit");
        Product product = tbl_ProductTable.getSelectionModel().getSelectedItem();
        tfProductID.setText(String.valueOf(product.getProductId()));
        tfProductName.setText(product.getProductName());
        tfProductPrice.setText(String.valueOf(product.getProductImportPrice()));
        tfProductQuantities.clear();
        tfProductTotal.clear();
    }
    public void btnEditClicked(MouseEvent mouseEvent) {
        if(!tfProductID.getText().isEmpty()) {
            if(btnEdit.getText().equals("Sửa")) {
                tfProductQuantities.setDisable(false);
                btnEdit.setId("add");
                btnEdit.setText("Lưu");
            } else {
                int sequence = ShowYesNoAlert("lưu");
                if(sequence==JOptionPane.YES_OPTION) {
                    UpdateImportTable();
                    LoadTotalPayFinal();
                    tfProductQuantities.setDisable(true);
                    btnEdit.setId("edit");
                    btnEdit.setText("Sửa");
                } else {
                }
            }
        }
    }

    private void LoadTotalPayFinal() {
        ObservableList<Import> items = tbl_DetailImportForm.getItems();
        int total = 0;
        for(Import detailImport : items) {
            total+= detailImport.getTotalPrice();
        }
        lblTotalPay.setText(total+" VND");
    }

    private void UpdateImportTable() {
        int delete_pos= DeleteRowInImportTable(tbl_DetailImportForm.getItems(),tbl_DetailImportForm.getSelectionModel().getSelectedItem().getProductId());
        Import import_ = new Import(
                Integer.parseInt(tfProductID.getText()),
                tfProductName.getText(),Integer.parseInt(tfProductPrice.getText()),
                Integer.parseInt(tfProductQuantities.getText()),
                Integer.parseInt(tfProductTotal.getText()));
        tbl_DetailImportForm.getItems().add(delete_pos,import_);
    }

    private int DeleteRowInImportTable(ObservableList<Import> items, int productId) {
        int index =0,pos_delete=0;
        for(Import detailImport : items)
        {
            if(detailImport.getProductId()==productId) {
                pos_delete=index;
                break;
            }
            index++;
        }
        items.remove(pos_delete);
        return pos_delete;
    }

    public void btnAddClicked(MouseEvent mouseEvent) {
        String id = tfProductID.getText();
        String productname = tfProductName.getText();
        String price = tfProductPrice.getText();
        String quantity = tfProductQuantities.getText();
        String total = tfProductTotal.getText();
        if(Integer.parseInt(quantity)==0) {
            tfProductQuantities.clear();
            showAlert("Warning","Chỉ nhập được nhập số > 0!");
        }else if(!id.isEmpty() && !productname.isEmpty() && !price.isEmpty() && !quantity.isEmpty() && !total.isEmpty()) {
            Import detailImport_ = new Import(Integer.parseInt(id),productname,Integer.parseInt(price),Integer.parseInt(quantity),Integer.parseInt(total));
            tbl_DetailImportForm.getItems().add(detailImport_);
            lbl_noContent.setVisible(true);
            if(tbl_DetailImportForm.getItems().isEmpty()) lbl_noContent.setVisible(true); else  lbl_noContent.setVisible(false);
            LoadTotalPayFinal();
            clearInformation();
        } else {
            showAlert("Warning","Kiểm tra đảm bảo đầy đủ thông tin!");
        }
    }

    public void btnCancelClicked(MouseEvent mouseEvent) {
        int sequence = ShowYesNoAlert("huỷ");
        if(sequence==JOptionPane.YES_OPTION) {
            clearInformation();
            tfProductQuantities.setDisable(false);
            tbl_DetailImportForm.getItems().clear();
            if(tbl_DetailImportForm.getItems().isEmpty()) lbl_noContent.setVisible(true); else  lbl_noContent.setVisible(false);
        }
    }

    public void btnCreateFormClicked(MouseEvent mouseEvent) {
        if(!tbl_DetailImportForm.getItems().isEmpty()) {
            if(tf_supplier.getText().isEmpty()) {
                showAlert("Warning","Bạn chưa chọn nhà cung cấp!");
                return;
            }
            int sequence = ShowYesNoAlert("nhập hàng");
            if(sequence==JOptionPane.YES_OPTION) {
                String string = lblTotalPay.getText();
                String[] splits = string.split(" ");
                int totalPay = Integer.parseInt(splits[0]);

                Import import_ = new Import(
                        employee_init.getEmployeeId(),
                        supplierBeforeClicked.getPartner_id(),
                        totalPay,
                        LocalDateTime.now().toString());
                int id = importDAO.addImportData(import_);
                if(id>0) {
                    if(AddDetailImportToDB(id)){
                        if(CreateReceipt(id,totalPay))
                            showAlert("Notification","Thông tin nhập hàng được ghi nhận!");
                        if(ShowYesNoAlert("in hoá đơn")==JOptionPane.YES_OPTION) printBill();
                        else {}
                        clearInformation();
                        loadProducts();
                        tbl_DetailImportForm.getItems().clear();
                    } else System.out.println("Error");
                }
            } else {};

        }
    }

    private boolean CreateReceipt(int id,int totalPay) {
        String content ="Import ID: " + id, status = "InComplete", note = LocalDate.now().toString();
        return receiptDAO.autoCreateReceipt(employee_init.getEmployeeId(),content,totalPay,status,note);
    }

    private boolean AddDetailImportToDB(int id) {
        ObservableList<Import> listImport = tbl_DetailImportForm.getItems();
        for(Import import_: listImport){
            import_.setImportId(id);
            if(!importDAO.AddDetailtoDB(import_)) return false;
            if(!importDAO.updateProduct(import_.getQuantity(),import_.getProductId())) return false;
        }
        return true;
    }

    public void clearInformation() {
        tfProductID.clear();
        tfProductName.clear();
        tfProductPrice.clear();
        tfProductQuantities.clear();
        tfProductTotal.clear();
        tf_supplier.clear();
    }
    private void loadProducts() {
        ListProducts = productDAO.getAllProduct();
        tbl_ProductTable.setItems(ListProducts);
    }
    private void loadSupplier() {
        ListSuppliers= supplierDAO.getSuppliers();
        tbl_Supplier.setItems(ListSuppliers);
    }
    public void handleButtonShow(ActionEvent event) {
        if(panelResultSupplier.isVisible()) {
            panelResultSupplier.setVisible(false);

            btnShow.setId("edit");
            btnShow.setText("Xem");
        } else {
            tbl_Supplier.setItems(ListSuppliers);
            tf_supplier.promptTextProperty().setValue("Nhập id hoặc tên nhà cung cấp!");
            panelResultSupplier.setVisible(true);
            btnShow.setId("delete");
            btnShow.setText("X");
        }
    }
    public void btnDeleteClicked(MouseEvent mouseEvent) {
        if (!tbl_DetailImportForm.getSelectionModel().isEmpty()) {
            Import import_ = tbl_DetailImportForm.getSelectionModel().getSelectedItem();
            int sequence = ShowYesNoAlert("xoá " +import_.getProductName());
            if(sequence==JOptionPane.YES_OPTION) {
                tbl_DetailImportForm.getItems().remove(import_);
                if(tbl_DetailImportForm.getItems().isEmpty()) lbl_noContent.setVisible(true); else  lbl_noContent.setVisible(false);
            } else {}
        }
    }

    private void showAlert(String tilte,String string) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(tilte);
        alert.setHeaderText(null);
        alert.setContentText(string);
        alert.showAndWait();
    }

    private int ShowYesNoAlert(String string) {
        JFrame frame = new JFrame("Xác nhận");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        return JOptionPane.showConfirmDialog(frame, "Bạn muốn "+string+"?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
    }

    public void HandleButtonHistory(ActionEvent event) {
        loadHistoryImport();
        if(tp_ImportHistory.isVisible()) {
            panelImport.setVisible(true);
            tp_ImportHistory.setVisible(false);
            btnShowHistory.setText("Xem lịch sử");
            hboxFind.setVisible(true);
            tp_left.setLayoutY(7);
            tp_left.setPrefHeight(252);
            tp_left.setText("Danh sách nhập");
            tbl_DetailImportForm.setPrefHeight(212);
        } else {
            panelImport.setVisible(false);
            tp_ImportHistory.setVisible(true);
            btnShowHistory.setText("Quay về nhập hàng");
            hboxFind.setVisible(false)  ;
            tp_left.setLayoutY(312);
            tp_left.setPrefHeight(460);
            tp_left.setText("Chi tiết nhập hàng");
            tbl_DetailImportForm.setPrefHeight(424);
        }
    }
    private boolean isValidInput(String input) {
        String regex = ".*[a-zA-Z\\d\\u00C0-\\u1FFF\\u2C00-\\uD7FF]+.*";
        return input.matches(regex);
    }
    private void printBill() {
        Document document = new Document();

        String fileName = "Import_Bill_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".pdf";
        String directoryPath = "Bill/";
        String filePath = directoryPath + fileName;

        try {
            // Create directories if they don't exist
            File directory = new File(directoryPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            String fontPath = "notosans-regular.ttf";
            BaseFont baseFont = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

            Font boldFont = new Font(baseFont, 15, Font.BOLD);
            Font regularFont = new Font(baseFont, 13, Font.BOLD);

            document.add(new Paragraph("Green Pharmacy", boldFont));
            document.add(new Paragraph("Address: 136, Linh Trung, Thủ Đức, TP Thủ Đức", regularFont));
            document.add(new Paragraph("Phone: 1900 1555           Employee: " + employee_init.getEmployeeId(), regularFont));
            document.add(new Paragraph("IMPORT BILL", boldFont));

            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            float[] columnWidths = {2f, 1f, 1f, 1f, 1f};
            table.setWidths(columnWidths);

            // Table headers
            table.addCell(new PdfPCell(new Phrase("Product ID", boldFont)));
            table.addCell(new PdfPCell(new Phrase("Product Name", boldFont)));
            table.addCell(new PdfPCell(new Phrase("Price", boldFont)));
            table.addCell(new PdfPCell(new Phrase("Quantity", boldFont)));
            table.addCell(new PdfPCell(new Phrase("Total", boldFont)));

            // Table data
            for (Import product : tbl_DetailImportForm.getItems()) {
                table.addCell(new PdfPCell(new Phrase(String.valueOf(product.getProductId()), regularFont)));
                table.addCell(new PdfPCell(new Phrase(product.getProductName(), regularFont)));
                table.addCell(new PdfPCell(new Phrase(String.valueOf(product.getImportPrice()), regularFont)));
                table.addCell(new PdfPCell(new Phrase(String.valueOf(product.getQuantity()), regularFont)));
                table.addCell(new PdfPCell(new Phrase(String.valueOf(product.getTotalPrice()), regularFont)));
            }

            document.add(table);

            float total = getTotalImport();
            document.add(new Paragraph("Total: " + String.format("%,.0f", total), boldFont));
            document.add(new Paragraph("Date: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")), regularFont));

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to generate PDF bill");
        } finally {
            document.close();

            // Open the generated PDF file
            try {
                File file = new File(filePath);
                Desktop.getDesktop().open(file);
            } catch (IOException ex) {
                ex.printStackTrace();
                showAlert("Error", "Failed to open PDF file");
            }
        }
    }
    private float getTotalImport() {
        float total = 0;
        for (Import product : tbl_DetailImportForm.getItems()) {
            total += product.getTotalPrice();
        }
        return total;
    }
}