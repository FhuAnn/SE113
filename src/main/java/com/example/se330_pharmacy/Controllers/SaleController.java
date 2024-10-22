package com.example.se330_pharmacy.Controllers;

import com.example.se330_pharmacy.DataAccessObject.BillDAO;
import com.example.se330_pharmacy.DataAccessObject.DetailBillDAO;
import com.example.se330_pharmacy.DataAccessObject.ProductDAO;
import com.example.se330_pharmacy.Models.*;
import com.itextpdf.text.pdf.BaseFont;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.converter.IntegerStringConverter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;


import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import static java.lang.StringTemplate.STR;

public class SaleController implements Initializable {

    private Employee employee;
    public void initData(Employee _employee) {
        employee=_employee;
    }
    private final ProductDAO productDAO = new ProductDAO();

    @FXML
    private RadioButton radBigUnit;
    @FXML
    private RadioButton radSmallUnit;
    @FXML
    private AnchorPane paneBigUnit;
    @FXML
    private AnchorPane paneSmallUnit;
    @FXML
    private Label bigAmountSoldOut;
    @FXML
    private Label smallAmountSoldOut;
    @FXML
    private Label bigAmount;
    @FXML
    private Label smallAmount;
    @FXML
    private Button btnAdd;

    @FXML
    private Button btnAllBill;

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnCreateBill;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnSelling;

    @FXML
    private Label smallUnit;

    @FXML
    private Tab tabCart;

    @FXML
    private Tab tabDetailInvoice;

    @FXML
    private Tab tabInvoiceList;

    @FXML
    private Tab tabProduct;

    @FXML
    private Text textTotalInvoice;

    @FXML
    private TextField tfCustomName;

    @FXML
    private TextField tfPhoneNumber;

    @FXML
    private TextField tfQuantity;

    @FXML
    private TextField tfSearchByIdAndName;

    @FXML
    private TableColumn<Product, String> tcProductBigUnit;

    @FXML
    private TableColumn<Product, Integer> tcProductBigUnitQuantities;

    @FXML
    private TableColumn<Product, Integer> tcProductCoef;

    @FXML
    private TableColumn<Product, Integer> tcProductId;

    @FXML
    private TableColumn<CartItem, Integer> tcProductIdCart;

    @FXML
    private TableColumn<Product, String> tcProductName;

    @FXML
    private TableColumn<CartItem, String> tcProductNameCart;

    @FXML
    private TableColumn<Product, Integer> tcProductPrice;

    @FXML
    private TableColumn<CartItem, String> tcProductPriceCart;

    @FXML
    private TableColumn<CartItem, Integer> tcProductQuantitiesCart;

    @FXML
    private TableColumn<Product, String> tcProductSmallUnit;

    @FXML
    private TableColumn<Product, Integer> tcProductSmallUnitQuantities;

    @FXML
    private TableColumn<CartItem, String> tcProductUnitCart;


    @FXML
    private TableView<CartItem> tvCart;

    @FXML
    private TableView<DetailBill> tvDetailInvoice;

    @FXML
    private TableView<Bill> tvInvoiceList;

    @FXML
    private TableView<Product> tvProduct;

    private String productName;
    private int productId;
    private String productBigPrice;
    private final List<CartItem> cartList = new ArrayList<>();
    private boolean IsPressedCreateBill;
    private String _unitName;
    private String _coef;
    private CartItem cartItemSelected;
    @FXML
    private TabPane tabPane;
    private final BillDAO billDAO = new BillDAO() ;
    private final DetailBillDAO detailBillDAO = new DetailBillDAO();
    private String bigQuantity;
    private String smallQuantity;
    private String message;
    @FXML
    private TabPane tabPaneCart;
    private Bill billSelected;
    @FXML
    private TableColumn<Bill, String> tcBillCusName;

    @FXML
    private TableColumn<Bill, String> tcBillCusNumber;

    @FXML
    private TableColumn<Bill, Date> tcBillDate;

    @FXML
    private TableColumn<Bill, Integer> tcBillId;

    @FXML
    private TableColumn<Bill, String> tcBillValue;

    @FXML
    private TableColumn<DetailBill, String> tcDetailProductName;

    @FXML
    private TableColumn<DetailBill, Float> tcDetailProductPrice;

    @FXML
    private TableColumn<DetailBill, Integer> tcDetailProductQuan;

    @FXML
    private TableColumn<DetailBill, String> tcDetailProductUnit;

    @FXML
    void btnSaleClicked(MouseEvent event) {
        tabProduct.setDisable(false);
        tabCart.setDisable(false);
        tabInvoiceList.setDisable(true);
        tabDetailInvoice.setDisable(true);
        btnSelling.setDisable(true);
        btnAllBill.setDisable(false);
        tabPane.getSelectionModel().select(tabProduct);
        tabPaneCart.getSelectionModel().select(tabCart);
        tfSearchByIdAndName.setPromptText("Tìm theo tên hoặc theo mã");
        tfPhoneNumber.setDisable(false);
        tfCustomName.setDisable(false);
    }
    @FXML
    void btnViewAllBillClicked(MouseEvent event) {
        tabProduct.setDisable(true);
        tabCart.setDisable(true);
        tabInvoiceList.setDisable(false);
        tabDetailInvoice.setDisable(false);
        btnAdd.setDisable(true);
        btnDelete.setDisable(true);
        btnCancel.setDisable(true);
        btnCreateBill.setDisable(true);
        btnSelling.setDisable(false);
        btnAllBill.setDisable(true);

        tfPhoneNumber.setDisable(true);
        tfCustomName.setDisable(true);
        tabPane.getSelectionModel().select(tabInvoiceList);
        new Thread(() -> {
            Platform.runLater(() -> {
                getBillData();
            });
        }).start();
        tfSearchByIdAndName.setPromptText("Tìm theo tên hoặc theo số điện thoại");
        tabPaneCart.getSelectionModel().select(tabDetailInvoice);
    }

    private void getBillData() {
        bindBillData();
        tvInvoiceList.setItems(billDAO.getBillData());
    }



    @FXML
    void btnCancelClicked(MouseEvent event) {
        int result = JOptionPane.showConfirmDialog(null,"Xóa đơn hàng này ?", "Thông báo", JOptionPane.YES_NO_OPTION);
        if(result == JOptionPane.YES_OPTION) {
            clearData();
            btnCancel.setDisable(true);
        }
    }
    @FXML
    void btnAddClicked(MouseEvent event) {
        btnCancel.setDisable(false);
        btnCreateBill.setDisable(false);
        paneSmallUnit.setVisible(true);

        if(addProductToTvCart()) {
            radBigUnit.setSelected(false);
            radSmallUnit.setSelected(false);
            calculatePrice();
            clearInfomation();
        }
        radSmallUnit.setText("");
        radBigUnit.setText("");
        smallAmountSoldOut.setVisible(false);
        bigAmountSoldOut.setVisible(false);
        btnAdd.setDisable(true);
        radBigUnit.setDisable(true);
        radSmallUnit.setDisable(true);
        tfQuantity.setDisable(true);
    }
    @FXML
    void btnDeleteClicked(MouseEvent event) {

        cartList.remove(cartItemSelected);
        tvCart.refresh();
        calculatePrice();

    }
    @FXML
    void btnCreateBillClicked(MouseEvent event) throws SQLException, IOException {

        if(!tvCart.getItems().isEmpty()) {

            if(addBillToDB()) {
                tvProduct.setItems(productDAO.getAllProduct());
                tvProduct.refresh();

                int result = JOptionPane.showConfirmDialog(null,message,"Thông báo",JOptionPane.YES_NO_OPTION,JOptionPane.INFORMATION_MESSAGE);

                if( result == JOptionPane.YES_OPTION) {
                    printBill();
                }
                clearData();
                btnCreateBill.setDisable(false);
                btnDelete.setDisable(false);
                btnAdd.setDisable(false);
                btnCancel.setDisable(false);
                IsPressedCreateBill = true;
            }else {
                JOptionPane.showConfirmDialog(null,message,"Cảnh báo", JOptionPane.YES_OPTION,JOptionPane.WARNING_MESSAGE);
            }

        }else {
            JOptionPane.showConfirmDialog(null,"Chưa thêm sản phẩm nào, hãy thêm vào giỏ hàng!","Cảnh báo", JOptionPane.YES_OPTION,JOptionPane.WARNING_MESSAGE);
        }

    }

    private void clearData() {
        clearInfomation();
        tfCustomName.clear();
        tfPhoneNumber.clear();
        textTotalInvoice.setText("");
        tvCart.getItems().clear();
        tvCart.refresh();
    }

    private boolean addBillToDB() throws SQLException {
        boolean add = false;
        String id = billDAO.addBill(String.valueOf(employee.getEmployeeId()),tfCustomName.getText(),tfPhoneNumber.getText(),textTotalInvoice.getText().split(" ")[0]);

        //auto create receipt
        String contentReceipts = "Bill ID: " + id;
        String status = "Completed";
        billDAO.autoCreateReceipts(String.valueOf(employee.getEmployeeId()),contentReceipts,textTotalInvoice.getText().split(" ")[0],status,"");
        //end

        if(!tvCart.getItems().isEmpty()) {
            for (CartItem cartItem : tvCart.getItems()) {
                if (!String.valueOf(cartItem.getProductId()).isBlank()) {
                    detailBillDAO.addDetailData(id, cartItem.getProductId()+"",cartItem.getProductPrice(),
                            STR."\{cartItem.getProductQuantities()}" , cartItem.getProductUnit());

                    List<Unit> units = billDAO.getUnit(id);
                    String whatUnit = "small_unit";
                    if(cartItem.getProductUnit().equals(units.getFirst().getSmallUnit())){
                        whatUnit ="small_unit";
                    } else if (cartItem.getProductUnit().equals(units.getFirst().getBigUnit())){
                        whatUnit = "big_unit";
                    }
                    bigQuantity = STR."\{billDAO.getBigQuan(cartItem.getProductId())}";
                    smallQuantity = STR."\{billDAO.getSmallQuan(cartItem.getProductId())}";

                    calculateUpdateData(STR."\{cartItem.getProductId()}",STR."\{cartItem.getProductQuantities()}",whatUnit);

                    billDAO.updateProduct(STR."\{cartItem.getProductId()}",bigQuantity,smallQuantity);
                }
            }
            message = "Tạo đơn thành công. Bạn muốn in hóa đơn không?";
            add = true;
        }
        else {
            message = "Chưa có sản phẩm nào được thêm vào giỏ hàng. Hãy thử lại !";
            add = false;
        }

        return add;
    }

    public void calculateUpdateData(String id,String quantity, String unit) throws SQLException {
        String _coef = STR."\{billDAO.getCoef(Integer.parseInt(id))}";
        if(unit.equals("big_unit"))// Nếu bán Box/Bottle
        {
            bigQuantity = STR."\{Integer.parseInt(bigQuantity) - Integer.parseInt(quantity)}";//Box= box -a
        }
        else //Nếu bán pill
        {
            if (Integer.parseInt(quantity) < Integer.parseInt(_coef)) //A dưới giá trị Coef
            {
                if (Integer.parseInt(quantity) <= Integer.parseInt(smallQuantity))//--------------------------- Trường hợp 1 : A < giá trị data.Pill
                {
                    smallQuantity = STR."\{Integer.parseInt(smallQuantity) - Integer.parseInt(quantity)}"; // Pil=Pill -a
                }
                else //---------------------------------------------------------------------------------------Trường hợp 2 A >= giá trị data.Pill
                {
                    smallQuantity = STR."\{Integer.parseInt(smallQuantity) + Integer.parseInt(_coef) - Integer.parseInt(quantity)}"; // Pill = Pill + coef - a
                    bigQuantity = STR."\{Integer.parseInt(bigQuantity) - 1}";//Box= box -1
                }
            }
            else// A trên giá trị Coef
            {
                if ((Integer.parseInt(quantity) % Integer.parseInt(_coef)) <= Integer.parseInt(smallQuantity)) //Trường hợp 3: nếu a % coef dưới giá trị dataBill
                {
                    smallQuantity = STR."\{Integer.parseInt(smallQuantity) - (Integer.parseInt(quantity) % Integer.parseInt(_coef))}"; // Pil = Pil- a %coef
                    bigQuantity = STR."\{Integer.parseInt(bigQuantity) - (Integer.parseInt(quantity) / Integer.parseInt(_coef))}";//Box= box -a/Coef
                }
                else //--------------------------------------------------------------------------------------- Trường hợp 4: a%coef  hơn hoặc bằng giá trị data.Pill
                {
                    smallQuantity = STR."\{Integer.parseInt(smallQuantity) + Integer.parseInt(_coef) - (Integer.parseInt(quantity) % Integer.parseInt(_coef))}"; // Pil = Pil + coef- a %coef
                    bigQuantity = STR."\{Integer.parseInt(bigQuantity) - (Integer.parseInt(quantity) / Integer.parseInt(_coef)) - 1}";//BOX = box - a\coef-1
                }
            }
        }
    }

    private void clearInfomation() {
        productId = 0;
        productName = "";
        productBigPrice = "";
        tfQuantity.setText("");
        _unitName = "";
        bigAmount.setText("");
        smallAmount.setText("");
    }

    private void calculatePrice() {

        int total = 0;
        for (CartItem cartItem : tvCart.getItems()) {
            total += Integer.parseInt(cartItem.getProductPrice()) * Integer.parseInt(String.valueOf(cartItem.getProductQuantities()));
        }

        textTotalInvoice.setText(STR."\{total} VND");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadProductData();
        setUpProductRowClickListener();
        setUpSelectedRadioButton();
        validateQuantitesInput();
        bindCartData();
        setUpEditCartEvent();
        setUpCartRowClick();
        tabProduct.setDisable(false);
        tabCart.setDisable(false);
        tabPane.getSelectionModel().select(tabProduct);
        setUpProductSearch();
        setUpTabs();
        validatePhoneNumberInput();
        setUpBillRowClick();
    }

    private void setUpBillRowClick() {
        tvInvoiceList.setRowFactory(tv -> {
            TableRow<Bill> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    billSelected = row.getItem();
                    bindDetailBillData();
                    new Thread(() -> {
                        Platform.runLater(() -> {
                            tvDetailInvoice.setItems(detailBillDAO.getDetailBill(String.valueOf(billSelected.getBillId())).sorted());
                        });
                    }).start();
                }
            });
            return row;
        });
    }

    private void bindDetailBillData() {
        tcDetailProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        tcDetailProductQuan.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        tcDetailProductUnit.setCellValueFactory(new PropertyValueFactory<>("unit"));
        tcDetailProductPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    private void bindBillData() {
        tcBillId.setCellValueFactory(new PropertyValueFactory<>("billId"));
        tcBillCusName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        tcBillCusNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        tcBillValue.setCellValueFactory(new PropertyValueFactory<>("billValue"));
        tcBillDate.setCellValueFactory(new PropertyValueFactory<>("dateBill"));
    }

    private void validatePhoneNumberInput() {
        tfPhoneNumber.textProperty().addListener((observable, oldValue, newValue) -> {
            // Loại bỏ bất kỳ ký tự không phải là số
            String formattedPhoneNumber = newValue.replaceAll("[^\\d]", "");

            // Kiểm tra điều kiện:
            // 1. Bắt đầu bằng 0.
            // 2. Chiều dài tối đa là 10 ký tự.
            if (formattedPhoneNumber.length() == 0 || formattedPhoneNumber.startsWith("0")) {
                if (formattedPhoneNumber.length() <= 10) {
                    tfPhoneNumber.setText(formattedPhoneNumber);
                } else {
                    // Nếu chiều dài vượt quá 10 ký tự, cắt chuỗi thành 10 ký tự đầu tiên
                    tfPhoneNumber.setText(formattedPhoneNumber.substring(0, 10));
                }
            } else {
                // Nếu không bắt đầu bằng 0, giữ nguyên giá trị cũ
                tfPhoneNumber.setText(oldValue);
            }
        });
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

    private void printBill() throws IOException {
        Document document = new Document();

        String path = STR."Bill/\{removeAccentsAndSpaces(tfCustomName.getText())}.pdf";
        try {
            PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();

            String fontPath = "notosans-regular.ttf";
            BaseFont baseFont = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

            Font boldFont = new Font(baseFont, 15, Font.BOLD);
            Font regularFont = new Font(baseFont, 13,Font.BOLD);

            document.add(new Paragraph("Green Pharmacy", boldFont));
            document.add(new Paragraph("Address: 136, Linh Trung, Thủ Đức, TP Thủ Đức", regularFont));
            document.add(new Paragraph("Phone: 1900 1555           Employee: " + String.valueOf(employee.getEmployeeId()), regularFont));
            document.add(new Paragraph("RETAIL BILL", boldFont));

            document.add(new Paragraph("Customer: " + tfCustomName.getText(), boldFont));
            document.add(new Paragraph("Phone: " + tfPhoneNumber.getText(), regularFont));

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

            cell = new PdfPCell(new Paragraph("Unit", boldFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Unit Price", boldFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            for (CartItem product : tvCart.getItems()) {

                PdfPCell cellProductName = new PdfPCell(new Paragraph(product.getProductName(), regularFont));
                cellProductName.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellProductName.setVerticalAlignment(Element.ALIGN_MIDDLE);

                PdfPCell cellProductQuantities = new PdfPCell(new Paragraph(String.valueOf(product.getProductQuantities()), regularFont));
                cellProductQuantities.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellProductQuantities.setVerticalAlignment(Element.ALIGN_MIDDLE);

                PdfPCell cellProductUnit = new PdfPCell(new Paragraph(String.valueOf(product.getProductUnit()), regularFont));
                cellProductUnit.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellProductUnit.setVerticalAlignment(Element.ALIGN_MIDDLE);

                PdfPCell cellProductPrice = new PdfPCell(new Paragraph(String.valueOf(product.getProductPrice()), regularFont));
                cellProductPrice.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellProductPrice.setVerticalAlignment(Element.ALIGN_MIDDLE);

                table.addCell(cellProductName);
                table.addCell(cellProductQuantities);
                table.addCell(cellProductUnit);
                table.addCell(cellProductPrice);
            }

            document.add(table);

            float total = Float.parseFloat(textTotalInvoice.getText().split(" ")[0]);
            document.add(new Paragraph("Total: " + String.format("%,.0f", total), boldFont));
            document.add(new Paragraph("Note: ...............................................................", regularFont));

        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            File file = new File(path);
            Desktop.getDesktop().open(file);
            document.close();
        }
    }

    private void setUpTabs() {
        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            if (newTab == tabProduct) {
                setUpProductSearch();
            } else if (newTab == tabInvoiceList) {
                setUpBillSearch();
            }
        });
    }

    private void setUpBillSearch() {
        ObservableList<Bill> allBills = billDAO.getBillData();
        FilteredList<Bill> filteredList = new FilteredList<>(allBills);

        tfSearchByIdAndName.textProperty().addListener((observable, oldValue, newValue) -> {
            String searchText = newValue.trim().toLowerCase();

            if (searchText.isEmpty()) {
                // Nếu SearchField trống, hiển thị lại tất cả sản phẩm
                tvInvoiceList.setItems(allBills);
            } else {
                // Nếu có nội dung trong SearchField, lọc danh sách sản phẩm
                filteredList.setPredicate(bill -> {
                    String lowerCaseFilter = searchText.toLowerCase();
                    return bill.getCustomerName().toLowerCase().contains(lowerCaseFilter) ||
                            bill.getPhoneNumber().toLowerCase().contains(lowerCaseFilter);
                });
                tvInvoiceList.setItems(filteredList);
            }
        });
    }

    private void setUpProductSearch() {
        ObservableList<Product> allProducts = productDAO.getAllProduct();
        FilteredList<Product> filteredList = new FilteredList<>(allProducts);

        tfSearchByIdAndName.textProperty().addListener((observable, oldValue, newValue) -> {
            String searchText = newValue.trim().toLowerCase();

            if (searchText.isEmpty()) {
                // Nếu SearchField trống, hiển thị lại tất cả sản phẩm
                tvProduct.setItems(allProducts);
            } else {
                // Nếu có nội dung trong SearchField, lọc danh sách sản phẩm
                filteredList.setPredicate(product -> {
                    String lowerCaseFilter = searchText.toLowerCase();
                    return String.valueOf(product.getProductId()).contains(lowerCaseFilter) ||
                            product.getProductName().toLowerCase().contains(lowerCaseFilter);
                });

                tvProduct.setItems(filteredList);
            }
        });
    }

    private void setUpCartRowClick() {

        tvCart.setRowFactory(tv -> {
            TableRow<CartItem> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    cartItemSelected = row.getItem();

                    btnAdd.setDisable(true);
                    btnDelete.setDisable(false);
                }
            });
            return row;
        });

    }

    private void bindCartData() {
        tcProductIdCart.setCellValueFactory(new PropertyValueFactory<>("productId"));
        tcProductNameCart.setCellValueFactory(new PropertyValueFactory<>("productName"));
        tcProductPriceCart.setCellValueFactory(new PropertyValueFactory<>("productPrice"));
        tcProductQuantitiesCart.setCellValueFactory(new PropertyValueFactory<>("productQuantities"));
        tcProductUnitCart.setCellValueFactory(new PropertyValueFactory<>("productUnit"));
    }

    private void setUpEditCartEvent() {
        tcProductQuantitiesCart.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        tcProductQuantitiesCart.setOnEditCommit(event -> {
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setProductQuantities(String.valueOf(event.getNewValue()));
            calculatePrice();
        });
    }

    private void validateQuantitesInput() {
        tfQuantity.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                //check nguoi dung chi nhap so
                if (!newValue.matches("\\d*")) {
                    tfQuantity.setText(newValue.replaceAll("[^\\d]", ""));
                }
                //kiem tra neu quantity khac null
                if (!tfQuantity.getText().equals("")) {
                    int temp = Integer.parseInt(tfQuantity.getText());
                    if (temp > 0) {
                        //kiem tra neu radBigUnit da chon va temp > big amount va small amount
                        if ((radBigUnit.isSelected() && temp > Integer.parseInt(bigAmount.getText().split(" ")[0])) ||
                                (radSmallUnit.isSelected() && temp > Integer.parseInt(smallAmount.getText().split(" ")[0]))) {

                            Alert alert = new Alert(Alert.AlertType.WARNING, "Cảnh báo");
                            alert.setContentText("Nhập số ít hơn số lượng sản phẩm có sẵn.");
                            alert.showAndWait();
                            tfQuantity.setText("");
                            btnAdd.setDisable(true);
                        }
                        btnAdd.setDisable(false);
                    }
                }
            }
        });
    }

    private void setUpSelectedRadioButton() {
        ToggleGroup toggleGroup = new ToggleGroup();
        radSmallUnit.setToggleGroup(toggleGroup);
        radBigUnit.setToggleGroup(toggleGroup);

    }

    private void loadProductData() {
        bindProductData();
        new Thread(() -> {
            ObservableList<Product> data = productDAO.getAllProduct();

            Platform.runLater(() -> {
                tvProduct.setItems(data);
            });
        }).start();

    }

    private void bindProductData() {
        tcProductId.setCellValueFactory(new PropertyValueFactory<>("productId"));
        tcProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        tcProductPrice.setCellValueFactory(new PropertyValueFactory<>("productImportPrice"));
        tcProductSmallUnitQuantities.setCellValueFactory(new PropertyValueFactory<>("productSmallUnitQuantities"));
        tcProductSmallUnit.setCellValueFactory(new PropertyValueFactory<>("productSmallUnit"));
        tcProductBigUnitQuantities.setCellValueFactory(new PropertyValueFactory<>("productBigUnitQuantities"));
        tcProductBigUnit.setCellValueFactory(new PropertyValueFactory<>("productBigUnit"));
        tcProductCoef.setCellValueFactory(new PropertyValueFactory<>("productCoef"));
    }

    //bat su kien khi click 1 hang trong product thi se lay thong tin ra
    private void setUpProductRowClickListener() {
        tvProduct.setRowFactory(tv -> {
            TableRow<Product> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    Product productSelected = row.getItem();
                    //lay gia tri ti le de so sanh
                    _coef = productSelected.getProductCoef() + "";
                    // Khi lay duoc thong tin se enable cac component
                    radBigUnit.setDisable(false);
                    radSmallUnit.setDisable(false);
                    radBigUnit.setSelected(false);
                    radSmallUnit.setSelected(false);
                    tfCustomName.setDisable(false);
                    btnAdd.setDisable(false);
                    bigAmount.setDisable(true);
                    smallAmount.setDisable(true);
                    tfQuantity.setDisable(false);

                    //neu lay duoc thong tin tu tvProduct
                    if (retrieveProductData(row.getIndex(),
                            productSelected.getProductId(),
                            productSelected.getProductName(),
                            ("" + productSelected.getProductImportPrice()),
                            productSelected.getProductBigUnitQuantities() + "",
                            productSelected.getProductBigUnit(),
                            productSelected.getProductSmallUnitQuantities() + "",
                            productSelected.getProductSmallUnit(),
                            productSelected.getProductCoef() + "")) {

                        // neu don vi nho cung la don vi lon vi du : dvn Bottle, dvl Bottle
                        // neu khong thi ca 2 pane big small cung hien thi
                        paneSmallUnit.setVisible(!radSmallUnit.getText().equals(radBigUnit.getText()));

                        //kiem tra neu don vi lon het hang
                        if (checkSoldOutBigUnit()) {

                            radBigUnit.setSelected(false);
                            bigAmountSoldOut.setVisible(true);
                            radBigUnit.setDisable(true);
                            bigAmount.setVisible(false);

                            // kiem tra da tao bill chua
                            if (!IsPressedCreateBill) {
                                bigAmountSoldOut.setVisible(false);
                                bigAmount.setText(bigAmount.getText() + " Hiện còn");
                                bigAmount.setVisible(true);
                            }

                            btnAdd.setDisable(true);

                        } else {
                            radBigUnit.setDisable(false);
                            bigAmountSoldOut.setVisible(false);
                            btnAdd.setDisable(false);
                            bigAmount.setText(bigAmount.getText() + " Hiện còn");
                            bigAmount.setVisible(true);
                        }

                        // kiem tra don vi nho het hang
                        if (checkSoldOutSmallUnit()) {
                            radSmallUnit.setSelected(false);
                            smallAmountSoldOut.setVisible(true);
                            radSmallUnit.setDisable(true);
                            smallAmount.setVisible(false);

                            // kiem tra da tao bill chua
                            if (!IsPressedCreateBill) {
                                smallAmountSoldOut.setVisible(false);
                                smallAmount.setText(smallAmount.getText() + " Hiện còn");
                                smallAmount.setVisible(true);
                            }
                            btnAdd.setDisable(false);
                        } else {
                            radSmallUnit.setDisable(false);
                            radSmallUnit.setSelected(true);
                            smallAmountSoldOut.setVisible(false);
                            btnAdd.setDisable(false);
                            smallAmount.setText(smallAmount.getText() + " Hiện còn");
                            smallAmount.setVisible(true);
                        }
                    }
                }
            });
            return row;
        });
    }

    private boolean retrieveProductData(int index, int id, String name, String price, String bigValue, String bigUnit, String smallValue, String smallUnit, String coef) {
        if (index != -1) {
            clearInfomation();
            productId = id;
            productName = name;
            productBigPrice = price;
            radBigUnit.setText(bigUnit);
            bigAmount.setText(bigValue);
            radSmallUnit.setText(smallUnit);
            if (smallValue != "") {
                smallAmount.setText(STR."\{Integer.parseInt(smallValue) + Integer.parseInt(bigValue) * Integer.parseInt(coef)}");
            }
            int totalCartAmount = 0;
            int total1Unit = 0;
            int remain = 0;
            int remain1Unit = 0;
            if(!tvCart.getItems().isEmpty()) {
                for (CartItem cartItem : tvCart.getItems()) {
                    if(cartItem.getProductId() == productId && cartItem.getProductUnit().equals(radBigUnit.getText()) && !bigUnit.equals(smallUnit)) {
                        totalCartAmount += cartItem.getProductQuantities()*Integer.parseInt(_coef);
                    }else if (cartItem.getProductId() == productId && cartItem.getProductUnit().equals(radSmallUnit.getText()) && !bigUnit.equals(smallUnit)) {
                        totalCartAmount += cartItem.getProductQuantities();
                    } else if (cartItem.getProductId() == productId && cartItem.getProductUnit().equals(radBigUnit.getText()) && bigUnit.equals(smallUnit)) {
                        total1Unit += cartItem.getProductQuantities();
                    }
                    remain = Integer.parseInt(smallAmount.getText())- totalCartAmount;
                    remain1Unit = Integer.parseInt(bigAmount.getText()) - total1Unit;

                }
                smallAmount.setText(STR."\{remain}");

                if (bigUnit.equals(smallUnit)) {
                    bigAmount.setText(STR."\{remain1Unit}");
                } else {
                    bigAmount.setText(STR."\{remain / Integer.parseInt(_coef)}");
                }
            }

        }
        return true;
    }

    //lv1 = big, lv2 = small

    private boolean checkSoldOutBigUnit() {
        return Integer.parseInt(bigAmount.getText().split(" ")[0]) <= 0;
    }

    private boolean checkSoldOutSmallUnit() {
        return smallAmount.getText().equals("(Không xác định)") ||
                smallAmount.getText().split(" ")[0].isEmpty() ||
                Integer.parseInt(smallAmount.getText()) <= 0;
    }

    private boolean addProductToTvCart() {

        boolean add = false;

        String _smallQuantities;
        String _bigQuantities;

        String bigUnit = radBigUnit.getText();
        String smallUnit = radSmallUnit.getText();

        CartItem cartItem = new CartItem();
        cartItem.setProductId(productId);
        cartItem.setProductName(productName);
        cartItem.setProductQuantities(tfQuantity.getText());

        if (radBigUnit.isSelected()) {
            _unitName = radBigUnit.getText();
            cartItem.setProductPrice(productBigPrice);
        } else if (radSmallUnit.isSelected()) {
            _unitName = radSmallUnit.getText();
            cartItem.setProductPrice(STR."\{Integer.parseInt(productBigPrice) / Integer.parseInt(_coef)}");
        }

        cartItem.setProductUnit(_unitName);

        List<CartItem> itemsToAdd = new ArrayList<>();

        // neu tfQuantity = 0 thi return false
        if(cartItem.getProductQuantities() == 0) {
            return false;
        }
        // neu cart dang trong thi them ngay
        else {
            // tao bien foundItem de check item da co trong cart chua
            boolean foundItem = false;
            // duyet cart
            for (CartItem item : cartList) {

                // neu co item trong cart va item do co don vi lon
                if (item.getProductId() == cartItem.getProductId() &&
                        item.getProductUnit().equals(cartItem.getProductUnit()) &&
                        cartItem.getProductUnit().equals(bigUnit)) {

                    // thi se cong vao item hien co
                    item.setProductQuantities(STR."\{item.getProductQuantities() + cartItem.getProductQuantities()}");
                    tvCart.refresh();
                    foundItem =true;
                    add = true;

                    // neu co item trong cart va item do co don vi nho
                } else if (item.getProductId() == cartItem.getProductId() &&
                        item.getProductUnit().equals(cartItem.getProductUnit()) &&
                        cartItem.getProductUnit().equals(smallUnit)) {

                    // thi se cong vao item hien co
                    item.setProductQuantities(STR."\{item.getProductQuantities() + cartItem.getProductQuantities()}");
                    add = true;
                    foundItem =true;
                    tvCart.refresh();
                    // kiem tra neu quantity > coef
                    if(item.getProductQuantities() >= Integer.parseInt(_coef)) {

                        // xu ly bigQuan va smallQuan
                        _bigQuantities = STR."\{item.getProductQuantities() / Integer.parseInt(_coef)}";
                        _smallQuantities = STR."\{item.getProductQuantities() % Integer.parseInt(_coef)}";

                        cartItem.setProductQuantities(_bigQuantities);
                        cartItem.setProductUnit(bigUnit);
                        cartItem.setProductPrice(productBigPrice);
                        item.setProductQuantities(_smallQuantities);

                        // neu sau xu ly co quantity = 0 thi se remove

                        // tao bien found de kiem tra item don vi lon co trong cart chua
                        boolean found = false;
                        for (CartItem item1 : cartList) {
                            // neu co trong cart thi se + vao
                            if(item1.getProductId() == cartItem.getProductId() &&
                                    item1.getProductUnit().equals(cartItem.getProductUnit()) &&
                                    cartItem.getProductUnit().equals(bigUnit)) {
                                item1.setProductQuantities(STR."\{item1.getProductQuantities() + cartItem.getProductQuantities()}");
                                tvCart.refresh();
                                found = true;
                                break;
                            }
                        }

                        // khong co thi se them moi
                        if (!found) {
                            itemsToAdd.add(cartItem);
                        }
                    }
                }
            }

            // neu khong co item co san trong cart thi se them moi
            if(!foundItem) {

                if(cartItem.getProductQuantities()>= Integer.parseInt(_coef) && !smallUnit.equals(bigUnit)) {

                    _bigQuantities = STR."\{cartItem.getProductQuantities() / Integer.parseInt(_coef)}";
                    _smallQuantities = STR."\{cartItem.getProductQuantities() % Integer.parseInt(_coef)}";

                    CartItem smallUnitItem = new CartItem();
                    smallUnitItem.setProductId(cartItem.getProductId());
                    smallUnitItem.setProductUnit(smallUnit);
                    smallUnitItem.setProductPrice(STR."\{Integer.parseInt(productBigPrice) / Integer.parseInt(_coef)}");
                    smallUnitItem.setProductQuantities(_smallQuantities);
                    smallUnitItem.setProductName(cartItem.getProductName());

                    cartItem.setProductUnit(bigUnit);
                    cartItem.setProductQuantities(_bigQuantities);
                    cartItem.setProductPrice(productBigPrice);



                    if(smallUnitItem.getProductQuantities() != 0 ){
                        cartList.add(smallUnitItem);
                        add = true;
                    }

                    boolean toAdd = true;
                    for (CartItem item : cartList) {
                        if(item.getProductId()==cartItem.getProductId() &&
                                item.getProductUnit().equals(cartItem.getProductUnit())){
                            item.setProductQuantities(STR."\{item.getProductQuantities() + cartItem.getProductQuantities()}");
                            tvCart.refresh();
                            toAdd = false;
                            add = true;
                        }
                    }
                    if(toAdd){
                        cartList.add(cartItem);
                    }
                } else {
                    itemsToAdd.add(cartItem);
                    add = true;
                }
            }

            cartList.addAll(itemsToAdd);
            for (CartItem item : cartList) {
                if(item.getProductQuantities() == 0) {
                    cartList.remove(item);
                }
            }
            tvCart.setItems(FXCollections.observableList(cartList));
        }
        return add;
    }

}