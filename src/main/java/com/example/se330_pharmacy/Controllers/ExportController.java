package com.example.se330_pharmacy.Controllers;

import com.example.se330_pharmacy.DataAccessObject.ExportDAO;
import com.example.se330_pharmacy.DataAccessObject.ProductDAO;
import com.example.se330_pharmacy.Models.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.Normalizer;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class ExportController implements Initializable {

    private static final int FIXED_QUANTITY = 20;
    private ProductDAO productDAO = new ProductDAO();
    private ExportDAO exportDAO = new ExportDAO();
    Product selected;
    @FXML
    private Button btnAdd;

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnCreateForm1;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnEdit;

    @FXML
    private TextArea taExportReason;

    @FXML
    private TableColumn<ExportItem, Integer> tcExportItemId;

    @FXML
    private TableColumn<ExportItem, String> tcExportItemName;


    @FXML
    private TableColumn<ExportItem, Long> tcExportItemPrice;

    @FXML
    private TableColumn<ExportItem, Integer> tcExportItemQuan;

    @FXML
    private TableColumn<ExportItem, Long> tcExportItemTotal;

    @FXML
    private TableColumn<Product, String> tcProductDesc;

    @FXML
    private TableColumn<Product, Integer> tcProductId;
    @FXML
    private TableColumn<Product,String> tcQuantity;

    @FXML
    private TableColumn<Product, String> tcProductName;

    @FXML
    private TableColumn<Product, String> tcProductOrigin;

    @FXML
    private TableColumn<Product, Long> tcProductPrice;

    @FXML
    private TableColumn<Product, String> tcProductType;

    @FXML
    private TableColumn<Product, String> tcProductUnit;

    @FXML
    private Text textTotalValue;

    @FXML
    private TextField tfProductID;

    @FXML
    private TextField tfProductName;

    @FXML
    private TextField tfProductPrice;

    @FXML
    private TextField tfProductQuantities;

    @FXML
    private TextField tfProductTotal;

    @FXML
    private TextField tfSearch;

    @FXML
    private TableView<ExportItem> tvExportForm;

    @FXML
    private TableView<Product> tvProductTable;

    private ObservableList<ExportItem> exportList;

    @FXML
    private AnchorPane historyExportPane;

    @FXML
    private Button btnBackToExport;

    @FXML
    private TableView<ExportForm> tvExportHistory;

    @FXML
    private TableView<DetailExport> tvDetailExport;
    @FXML
    private AnchorPane ExportPane;

    @FXML
    private TableColumn<ExportForm, String> tcExportDate;

    @FXML
    private TableColumn<ExportForm, String> tcExportEmployee;

    @FXML
    private TableColumn<ExportForm, Integer> tcExportId;

    @FXML
    private TableColumn<ExportForm, Integer> tcExportPrice;

    @FXML
    private TableColumn<DetailExport, Integer> tcExportProductId;

    @FXML
    private TableColumn<DetailExport, String> tcExportProductName;

    @FXML
    private TableColumn<DetailExport, Long> tcExportProductTotal;

    @FXML
    private TableColumn<DetailExport, Integer> tcExportQuantity;

    @FXML
    private TableColumn<ExportForm, String> tcExportReason;

    @FXML
    private TableColumn<ExportForm, Long> tcExportTotal;

    @FXML
    private TableColumn<DetailExport, String> tcExportUnit;

    private boolean isEdit = false;
    private ExportItem itemToDelete;
    private Employee _employee;
    private String message;

    public void initData(Employee employee) {
        _employee = employee;
    }

    @FXML
    void btnHistoryClicked(MouseEvent event) {
        ExportPane.setVisible(false);
        historyExportPane.setVisible(true);
        loadDetailExport();
    }
    @FXML
    void btnBackToExportClicked(MouseEvent event) {
        ExportPane.setVisible(true);
        historyExportPane.setVisible(false);
    }

    @FXML
    void btnAddClicked(MouseEvent event) {
        boolean found = false;
        ExportItem exportItem = new ExportItem();
        exportItem.setExportItemId(Integer.parseInt(tfProductID.getText()));
        exportItem.setExportItemName(tfProductName.getText());
        exportItem.setExportItemPrice(Long.parseLong(tfProductPrice.getText()));
        exportItem.setExportItemQuantity(Integer.parseInt(tfProductQuantities.getText()));
        exportItem.setExportItemTotal(Long.parseLong(tfProductTotal.getText()));

        if(tfProductQuantities.getText().equals("0")) {
            tfProductQuantities.clear();
            showAlert("Warning","Chỉ nhập được nhập số > 0!");
            return;
        }

        if (!isEdit) {
            for (ExportItem item : exportList) {
                if (item.getExportItemId() == exportItem.getExportItemId() && exportItem.getExportItemPrice() == item.getExportItemPrice()) {
                    item.setExportItemQuantity(item.getExportItemQuantity() + exportItem.getExportItemQuantity());
                    item.setExportItemTotal(item.getExportItemTotal() + exportItem.getExportItemTotal());
                    found = true;
                    break;
                }
            }

            if (!found) {
                exportList.add(exportItem);
            }
        } else {
            for (ExportItem item : exportList) {
                if (item.getExportItemId() == exportItem.getExportItemId()) {
                    item.setExportItemQuantity(exportItem.getExportItemQuantity());
                    item.setExportItemTotal(exportItem.getExportItemTotal());
                    break;
                }
            }
            isEdit = false;
        }

        tvExportForm.setItems(exportList);
        tvExportForm.refresh();
        clearField();
        calculateTotal();
        btnAdd.setDisable(true);
        tfProductPrice.setDisable(true);
        tfProductQuantities.setDisable(true);
        btnCancel.setDisable(false);
        btnCreateForm1.setDisable(false);
    }

    private void calculateTotal() {
        long sum = 0;
        for (ExportItem item : exportList) {
            sum += item.getExportItemTotal();
        }
        textTotalValue.setText(STR."\{sum} VND");
    }

    private void clearField() {
        tfProductID.clear();
        tfProductName.clear();
        tfProductPrice.clear();
        tfProductQuantities.clear();
        tfProductTotal.clear();
    }

    @FXML
    void btnCancelClicked(MouseEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Bạn chắc chắn muốn xóa mẫu xuất này?", ButtonType.OK, ButtonType.CANCEL);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            exportList.clear();
            tvExportForm.refresh();
        }
        btnCancel.setDisable(true);
    }

    @FXML
    void btnCreateFormClicked(MouseEvent event) throws IOException {
        if (!taExportReason.getText().isEmpty()) {
            if (addDataToDB()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK, ButtonType.CANCEL);
                Optional<ButtonType> result = alert.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.OK) {
                    printExportForm();
                }
                clearField();
                textTotalValue.setText(" VND");
                taExportReason.setText("");
                tvExportForm.getItems().clear();
                tvExportForm.refresh();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK, ButtonType.CANCEL);
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Bạn chưa điền lí do xuất hàng!", ButtonType.OK, ButtonType.CANCEL);
            alert.showAndWait();
        }
    }
    public static String removeAccentsAndSpaces(String input) {
        // Chuẩn hóa chuỗi, loại bỏ các dấu
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String noAccents = pattern.matcher(normalized).replaceAll("");

        // Loại bỏ các khoảng trắng thừa
        String noSpaces = noAccents.replaceAll("\\s+", "");

        return noSpaces;
    }


    private void printExportForm() throws IOException {
        Document document = new Document();

        String path = STR."ExportForm/\{removeAccentsAndSpaces(taExportReason.getText())}.pdf";
        try {
            PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();

            String fontPath = "notosans-regular.ttf";
            BaseFont baseFont = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

            Font boldFont = new Font(baseFont, 18, Font.BOLD);
            Font regularFont = new Font(baseFont, 12);

            document.add(new Paragraph("Green Pharmacy", boldFont));
            document.add(new Paragraph("Address: 136, Linh Trung, Thủ Đức, TP Thủ Đức", regularFont));
            document.add(new Paragraph(STR."Phone: 1900 1555           Employee: \{_employee.getEmployeeId()},  Name: \{_employee.getEmployeeName()}", regularFont));
            document.add(new Paragraph("EXPORT FORM", boldFont));

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            float[] columnWidths = {2f, 1f, 1f, 1f};
            table.setWidths(columnWidths);

            PdfPCell cell = new PdfPCell(new Paragraph("Product", boldFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Quantities", boldFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Export Price", boldFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Total", boldFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            for (ExportItem item : tvExportForm.getItems()) {

                PdfPCell cellProductName = new PdfPCell(new Paragraph(item.getExportItemName(), regularFont));
                cellProductName.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellProductName.setVerticalAlignment(Element.ALIGN_MIDDLE);

                PdfPCell cellProductQuantities = new PdfPCell(new Paragraph(String.valueOf(item.getExportItemQuantity()), regularFont));
                cellProductQuantities.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellProductQuantities.setVerticalAlignment(Element.ALIGN_MIDDLE);

                PdfPCell cellProductUnit = new PdfPCell(new Paragraph(String.valueOf(item.getExportItemPrice()), regularFont));
                cellProductUnit.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellProductUnit.setVerticalAlignment(Element.ALIGN_MIDDLE);

                PdfPCell cellProductPrice = new PdfPCell(new Paragraph(String.valueOf(item.getExportItemTotal()), regularFont));
                cellProductPrice.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellProductPrice.setVerticalAlignment(Element.ALIGN_MIDDLE);

                table.addCell(cellProductName);
                table.addCell(cellProductQuantities);
                table.addCell(cellProductUnit);
                table.addCell(cellProductPrice);
            }

            document.add(table);

            float total = Float.parseFloat(textTotalValue.getText().split(" ")[0]);
            document.add(new Paragraph("Total: " + String.format("%,.0f", total), boldFont));
            document.add(new Paragraph("Note: ...............................................................", regularFont));

        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            File file = new File(path);
            Desktop.getDesktop().open(file);
            document.close();
        }
    }

    private boolean addDataToDB() {
        String id = exportDAO.addData(String.valueOf(_employee.getEmployeeId()), taExportReason.getText(), textTotalValue.getText().split(" ")[0]);
        if (id != null && !tvExportForm.getItems().isEmpty()) {
            ObservableList<ExportItem> rows = tvExportForm.getItems();
            int sum = 0;
            for (ExportItem row : rows) {
                if (row.getExportItemId() != 0) {
                    sum += row.getExportItemTotal();
                    exportDAO.addDetailData(row.getExportItemId(), id, row.getExportItemQuantity(), row.getExportItemPrice(), row.getExportItemTotal());
                    exportDAO.updateProduct(row.getExportItemQuantity(), row.getExportItemId());
                }
            }
            CreateReceipt(_employee.getEmployeeId(), sum);
            message = "Đã tạo biểu mẫu xuất thành công. Bạn có muốn in mẫu này?";
            return true;
        } else {
            message = "Kiểm tra lại thông tin!";
            return false;
        }
    }



    @FXML
    void btnDeleteClicked(MouseEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Bạn chắc chắn muốn xóa khỏi mẫu xuất?", ButtonType.OK, ButtonType.CANCEL);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Iterator<ExportItem> iterator = exportList.iterator();
            while (iterator.hasNext()) {
                ExportItem item = iterator.next();
                if (item.getExportItemId() == itemToDelete.getExportItemId()) {
                    iterator.remove(); // Sử dụng iterator để xóa phần tử một cách an toàn
                    break;
                }
            }
            tvExportForm.refresh();
            btnDelete.setDisable(true);
            btnEdit.setDisable(true);
        }
        btnDelete.setDisable(true);
        btnEdit.setDisable(true);
    }

    @FXML
    void btnEditClicked(MouseEvent event) {
        tfProductPrice.setDisable(false);
        tfProductQuantities.setDisable(false);
        isEdit = true;
        btnEdit.setDisable(true);
        btnDelete.setDisable(true);
        btnAdd.setDisable(false);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        exportList = FXCollections.observableArrayList();
        setUpHistoryExportRowClicked();
        bindExportItemData();
        setUpProductTable();
        setUpProductTableRowClicked();
        setUpExportItemRowClicked();
        setUpProductSearch();
        tfProductQuantities.textProperty().addListener((observable, oldValue, newValue) -> updateTotalValue());
        tfProductPrice.textProperty().addListener((observable, oldValue, newValue) -> updateTotalValue());
    }

    private void setUpHistoryExportRowClicked() {
        tvExportHistory.setOnMouseClicked(event -> {
            tcExportProductId.setCellValueFactory(new PropertyValueFactory<>("exportProductId"));
            tcExportProductName.setCellValueFactory(new PropertyValueFactory<>("exportProductName"));
            tcExportPrice.setCellValueFactory(new PropertyValueFactory<>("exportProductPrice"));
            tcExportQuantity.setCellValueFactory(new PropertyValueFactory<>("exportProductQuan"));
            tcExportUnit.setCellValueFactory(new PropertyValueFactory<>("exportProductUnit"));
            tcExportProductTotal.setCellValueFactory(new PropertyValueFactory<>("exportTotal"));
            if(event.getClickCount() == 2) {
                ExportForm selected = tvExportHistory.getSelectionModel().getSelectedItem();
                tvDetailExport.setItems(exportDAO.getExportById(selected.getExportFormId()));

            }
        });
    }

    private void loadDetailExport() {
        tcExportId.setCellValueFactory(new PropertyValueFactory<>("exportFormId"));
        tcExportEmployee.setCellValueFactory(new PropertyValueFactory<>("employeeName"));
        tcExportReason.setCellValueFactory(new PropertyValueFactory<>("exportReason"));
        tcExportDate.setCellValueFactory(new PropertyValueFactory<>("exportDate"));
        tcExportTotal.setCellValueFactory(new PropertyValueFactory<>("totalMoney"));

        tvExportHistory.setItems(exportDAO.getAllExportForm());
    }

    private void setUpExportItemRowClicked() {
        tvExportForm.setOnMouseClicked(event -> {
            ExportItem selected = tvExportForm.getSelectionModel().getSelectedItem();
            itemToDelete = new ExportItem(selected);
            btnEdit.setDisable(false);
            btnDelete.setDisable(false);
            if (event.getClickCount() == 2) {
                tfProductID.setText(STR."\{selected.getExportItemId()}");
                tfProductName.setText(STR."\{selected.getExportItemName()}");
                tfProductPrice.setText(STR."\{selected.getExportItemPrice()}");
                tfProductQuantities.setText(STR."\{selected.getExportItemQuantity()}");
                tfProductTotal.setText(STR."\{selected.getExportItemTotal()}");
            }
        });
    }

    private void bindExportItemData() {
        tcExportItemId.setCellValueFactory(new PropertyValueFactory<>("exportItemId"));
        tcExportItemName.setCellValueFactory(new PropertyValueFactory<>("exportItemName"));
        tcExportItemPrice.setCellValueFactory(new PropertyValueFactory<>("exportItemPrice"));
        tcExportItemQuan.setCellValueFactory(new PropertyValueFactory<>("exportItemQuantity"));
        tcExportItemTotal.setCellValueFactory(new PropertyValueFactory<>("exportItemTotal"));
    }

    private void updateTotalValue() {
        try {

            int quantity = Integer.parseInt(tfProductQuantities.getText());
            if (selected.getProductBigUnitQuantities() < quantity) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Số lượng trong kho nhỏ hơn số lượng lấy!");
                alert.showAndWait();
                tfProductQuantities.clear();
                return;
            }
            long price = Long.parseLong(tfProductPrice.getText());
            long totalValue = quantity * price;
            tfProductTotal.setText(String.valueOf(totalValue));
        } catch (NumberFormatException e) {
            // Xử lý nếu người dùng nhập không hợp lệ (không phải số)
            tfProductTotal.setText("Invalid input");
        }
    }

    private void setUpProductTableRowClicked() {
        tvProductTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                selected = tvProductTable.getSelectionModel().getSelectedItem();

                List<Product> productQuantity = productDAO.getAllProductBigQuantity();
                boolean select = false;
                for (Product product : productQuantity) {
                    if (selected.getProductId() == product.getProductId() && product.getProductBigUnitQuantities() < FIXED_QUANTITY) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Số lượng sản phẩm tồn kho này còn khá ít, hãy nhập thêm!", ButtonType.OK);
                        alert.showAndWait();
                        select = false;
                        break;
                    } else {
                        btnAdd.setDisable(false);
                        tfProductPrice.setDisable(false);
                        tfProductQuantities.setDisable(false);
                        select = true;
                    }
                }
                if (select) {
                    tfProductID.setText(STR."\{selected.getProductId()}");
                    tfProductName.setText(selected.getProductName());
                    tfProductPrice.setText(STR."\{selected.getProductImportPrice()}");
                }
            }
        });
    }

    private void setUpProductTable() {

        tcProductId.setCellValueFactory(new PropertyValueFactory<>("productId"));
        tcProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        tcQuantity.setCellValueFactory(new PropertyValueFactory<>("productBigUnitQuantities"));
        tcProductPrice.setCellValueFactory(new PropertyValueFactory<>("productImportPrice"));
        tcProductDesc.setCellValueFactory(new PropertyValueFactory<>("productDescription"));
        tcProductOrigin.setCellValueFactory(new PropertyValueFactory<>("productOrigin"));
        tcProductUnit.setCellValueFactory(new PropertyValueFactory<>("productBigUnit"));
        tcProductType.setCellValueFactory(new PropertyValueFactory<>("productType"));

        tvProductTable.setItems(productDAO.getAllProductExport());

    }

    private void setUpProductSearch() {
        ObservableList<Product> allProducts = productDAO.getAllProductExport();
        FilteredList<Product> filteredList = new FilteredList<>(allProducts);

        tfSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            String searchText = newValue.trim().toLowerCase();

            if (searchText.isEmpty()) {
                // Nếu SearchField trống, hiển thị lại tất cả sản phẩm
                tvProductTable.setItems(allProducts);
            } else {
                // Nếu có nội dung trong SearchField, lọc danh sách sản phẩm
                filteredList.setPredicate(product -> {
                    String lowerCaseFilter = searchText.toLowerCase();
                    return String.valueOf(product.getProductId()).contains(lowerCaseFilter) ||
                            product.getProductName().toLowerCase().contains(lowerCaseFilter);
                });
                tvProductTable.setItems(filteredList);
            }
        });
    }

    private boolean CreateReceipt(int id,int totalPay) {
        String content ="Export ID: " + id, status = "InComplete", note = LocalDate.now().toString();
        return exportDAO.autoCreateReceipt(_employee.getEmployeeId(),content,totalPay,status,note);
    }

    private void showAlert(String tilte,String string) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(tilte);
        alert.setHeaderText(null);
        alert.setContentText(string);
        alert.showAndWait();
    }
}