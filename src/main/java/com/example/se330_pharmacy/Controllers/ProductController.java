package com.example.se330_pharmacy.Controllers;

import com.example.se330_pharmacy.DataAccessObject.ProductDAO;
import com.example.se330_pharmacy.DataAccessObject.UnitDAO;
import com.example.se330_pharmacy.ForeignKeyViolationException;
import com.example.se330_pharmacy.Models.Product;
import com.example.se330_pharmacy.Models.Unit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import javax.swing.*;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static com.almasb.fxgl.dsl.FXGLForKtKt.showMessage;

public class ProductController implements Initializable {
    public Button btnAdd;
    public Button btnMini;
    public TextField tf_coef;
    public TextField tf_bigUnit;
    public TextField tf_smallUnit;
    public TextField tfProductId;
    public TextField tfProductName;
    public TextField tfProductPrice;
    public ComboBox<String> cbProductType;
    public TextField tfProductDescription;
    public TextField tfProductOrigin;
    public ComboBox<String> cbUnit;
    public Button btnDelete;
    public Button btnEdit;
    public TableView<Product> tblProduct;
    public TextField tfFind;
    public TableColumn<Product,Integer> tcProductId;
    public TableColumn<Product,String> tcProductName;
    public TableColumn<Product,Integer> tcProductPrice;
    public TableColumn<Product,String> tcProductDescription;
    public TableColumn<Product,String> tcProductOrigin;
    public TableColumn<Product,String> tcProductUnitSale;
    public TableColumn<Product,String> tcProductUnitImport;
    public TableColumn<Product,String> tcProductType;
    public TableColumn<Product,Integer> tcProductCoef;
    public TableView<Unit> tbl_Unit;
    public TableColumn<Unit,Integer> tcValue;
    public TableColumn<Unit,Integer> tcUnitId;
    public TableColumn<Unit,String> tcSmallUnit;
    public TableColumn<Unit,String> tcBigUnit;
    public Pane panel_Unit;
    public Text lblValue,lblBigUnit,lblSmallUnit,lblOn,lblBigUnitMini,lbl_Coef;
    public HBox hboxUnit;
    private UnitDAO unitDAO = new UnitDAO();

    private final ProductDAO productDAO = new ProductDAO();

    private ObservableList<Product> productList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SetUp();
        AddListener();
        loadProductTypes();
        loadUnits();
        loadProducts();
        loadUnitTable();
    }

    private void loadUnitTable() {
        ObservableList<Unit> units = unitDAO.getUnitData();
        tbl_Unit.setItems(units);
    }

    private void AddListener() {
        tblProduct.setOnMouseClicked(event -> {
            if(event.getClickCount()==2) {
                SetDisable(true);
                hboxUnit.setLayoutX(40);
                hboxUnit.setLayoutY(225);//255
                cbUnit.setVisible(false);
                hboxUnit.setVisible(true);
                btnAdd.setText("Thêm");
                btnAdd.setId("add");
                btnEdit.setText("Sửa");
                btnEdit.setId("edit");
                FillTableToTextField(tblProduct.getSelectionModel().getSelectedItem());
            }
        });
        cbUnit.setOnAction(event -> {
            String selectedValue = cbUnit.getValue();
            if(selectedValue != null && !selectedValue.isEmpty()) {
                if(!selectedValue.equals("Thêm khác")) {
                    panel_Unit.setVisible(false);
                    try {
                        String[] units = cbUnit.getValue().split("/");
                        String[] value_smallunit = units[0].split(" ");
                        lblSmallUnit.setText(value_smallunit[1]);
                        lblValue.setText(value_smallunit[0]);
                        lblBigUnit.setText(units[1]);
                        if(lblSmallUnit.getText().equals(lblBigUnit.getText())) {
                            lblValue.setVisible(false);
                            lblOn.setVisible(false);
                            lblBigUnit.setVisible(false);
                        } else  {
                            lblValue.setVisible(true);
                            lblOn.setVisible(true);
                            lblBigUnit.setVisible(true);
                        }
                    } catch (Exception e ) {
                        lblValue.setVisible(false);
                        lblOn.setVisible(false);
                        lblValue.setText("1");
                        lblBigUnit.setText(selectedValue);
                        lblBigUnit.setVisible(false);
                        lblSmallUnit.setText(selectedValue);
                        lblSmallUnit.setVisible(true);
                    }
                } else {
                    panel_Unit.setVisible(true);
                }
            }
        });
        tf_coef.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                tf_coef.setText(newValue.replaceAll("[^\\d]", ""));
            }
            if(newValue.equals("0")) tf_coef.setText(newValue.replaceAll("0", "1"));
        });
        tf_bigUnit.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.isEmpty()&&newValue.equals(tf_smallUnit.getText())) {
                int sequence = ShowYesNoAlert("đơn vị có đơn vị nhập và đơn vị xuất giống nhau, hệ số được sửa thành 1");
                if(sequence==JOptionPane.YES_OPTION) {
                    tf_coef.setText("1");
                    tf_coef.setDisable(true);
                }
            } else {
                tf_coef.setDisable(false);
                tf_coef.setText("2");
            }
        });
        tf_smallUnit.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.isEmpty()&&newValue.equals(tf_bigUnit.getText())) {
                int sequence = ShowYesNoAlert("đơn vị có đơn vị nhập và đơn vị xuất giống nhau, hệ số được sửa thành 1");
                if(sequence==JOptionPane.YES_OPTION) {
                    tf_coef.setText("1");
                    tf_coef.setDisable(true);
                }
            }else {
                tf_coef.setDisable(false);
                tf_coef.setText("2");
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
        tfFind.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.isEmpty()) {
                tblProduct.setItems(productList);
            }
        });
    }


    private void FillTableToTextField(Product product) {
        tfProductId.setText(String.valueOf(product.getProductId()));
        tfProductName.setText(product.getProductName());
        tfProductPrice.setText(String.valueOf(product.getProductImportPrice()));
        tfProductDescription.setText(product.getProductDescription());
        tfProductOrigin.setText(product.getProductOrigin());
        cbProductType.setValue(product.getProductType());
        //cbUnit.setValue(product.toString());
        lblValue.setText(String.valueOf(product.getProductCoef()));
        lblSmallUnit.setText(product.getProductSmallUnit());
        lblBigUnit.setText(product.getProductBigUnit());
        if(product.getProductSmallUnit().equals(product.getProductBigUnit())) {
            lblValue.setVisible(false);
            lblOn.setVisible(false);
            lblBigUnit.setVisible(false);
            cbUnit.setValue(product.getProductSmallUnit());
        } else  {
            lblValue.setVisible(true);
            lblOn.setVisible(true);
            lblBigUnit.setVisible(true);
            cbUnit.setValue(product.toString());
        }
    }

    private void SetDisable(boolean bool) {
        tfProductName.setDisable(bool);
        tfProductPrice.setDisable(bool);
        tfProductDescription.setDisable(bool);
        tfProductOrigin.setDisable(bool);
        cbProductType.setDisable(bool);
        cbUnit.setDisable(bool);
    }

    private void SetUp() {
        tcProductId.setCellValueFactory(new PropertyValueFactory<>("productId"));
        tcProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        tcProductPrice.setCellValueFactory(new PropertyValueFactory<>("productImportPrice"));
        tcProductDescription.setCellValueFactory(new PropertyValueFactory<>("productDescription"));
        tcProductOrigin.setCellValueFactory(new PropertyValueFactory<>("productOrigin"));
        tcProductUnitSale.setCellValueFactory(new PropertyValueFactory<>("productSmallUnit"));
        tcProductUnitImport.setCellValueFactory(new PropertyValueFactory<>("productBigUnit"));
        tcProductType.setCellValueFactory(new PropertyValueFactory<>("productType"));
        tcProductCoef.setCellValueFactory(new PropertyValueFactory<>("productCoef"));

        tcUnitId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tcValue.setCellValueFactory(new PropertyValueFactory<>("value"));
        tcSmallUnit.setCellValueFactory(new PropertyValueFactory<>("smallUnit"));
        tcBigUnit.setCellValueFactory(new PropertyValueFactory<>("bigUnit"));
    }

    public void btnAddClicked(MouseEvent mouseEvent) {
        if(btnEdit.getText().equals("Lưu")) return;
        if(btnAdd.getText().equals("Thêm")) {
            btnAdd.setText("Lưu");
            btnAdd.setId("save");
            clearInformation();
            hboxUnit.setLayoutX(40);
            hboxUnit.setLayoutY(255);//225
            lblValue.setVisible(true);
            lblSmallUnit.setVisible(true);
            lblBigUnit.setVisible(true);
            lblOn.setVisible(true);
            cbUnit.setVisible(true);
            lblValue.setText("Giá trị");
            lblSmallUnit.setText("đơn vị bé");
            lblOn.setText("trên");
            lblBigUnit.setText("đơn vị lớn");
            hboxUnit.setLayoutX(40);
            hboxUnit.setLayoutY(255);//225
            SetDisable(false);
        } else {//Lưu
            if (checkInformation()) {
                int sequence = ShowYesNoAlert("thêm "+ tfProductName.getText());
                if(sequence==JOptionPane.YES_OPTION) {
                    Product newProduct = new Product();
                    newProduct.setProductName(tfProductName.getText());
                    newProduct.setProductImportPrice((int) Float.parseFloat(tfProductPrice.getText()));
                    newProduct.setProductDescription(tfProductDescription.getText());
                    newProduct.setProductOrigin(tfProductOrigin.getText());
                    newProduct.setProductType(cbProductType.getValue());
                    newProduct.setProductCoef(Integer.parseInt(lblValue.getText()));
                    newProduct.setProductSmallUnit(lblSmallUnit.getText());
                    newProduct.setProductBigUnit(lblBigUnit.getText());
                    if (productDAO.addProduct(newProduct)) {
                        showAlert("Warning","Thêm sản phẩm thành công!");
                        btnAdd.setText("Thêm");
                        btnAdd.setId("add");
                        clearInformation();
                        cbUnit.setVisible(true);
                        clearInformation();
                        loadProducts();
                    } else {
                        showAlert("Warning","Thất bại");
                    }
                } else {}
            } else {
                showAlert("Warning","Thông tin đã nhập chưa đầy đủ!");
            }
        }

    }

    public void btnEditClicked(MouseEvent mouseEvent) {
        if(btnAdd.getText().equals("Lưu")) return;
        if(btnEdit.getText().equals("Sửa")) {
            SetDisable(false);
            btnEdit.setText("Lưu");
            btnEdit.setId("save");
            cbUnit.setVisible(true);
/*            lblValue.setText("Giá trị");
            lblSmallUnit.setText("đơn vị bé");
            lblOn.setText("trên");
            lblBigUnit.setText("đơn vị lớn");*/
            hboxUnit.setLayoutX(40);
            hboxUnit.setLayoutY(255);//225
            if(!lblSmallUnit.getText().equals(lblBigUnit.getText())) {
                lblValue.setVisible(true);
                lblBigUnit.setVisible(true);
                lblOn.setVisible(true);
            }
        } else {
            if (checkInformationEdit()) {
                int sequence = ShowYesNoAlert("sửa thông tin sản phẩm "+tfProductName.getText());
                if(sequence==JOptionPane.YES_OPTION) {
                    Product updatedProduct = new Product();
                    updatedProduct.setProductId(Integer.parseInt(tfProductId.getText()));
                    updatedProduct.setProductName(tfProductName.getText());
                    updatedProduct.setProductImportPrice((int) Float.parseFloat(tfProductPrice.getText()));
                    updatedProduct.setProductDescription(tfProductDescription.getText());
                    updatedProduct.setProductOrigin(tfProductOrigin.getText());
                    updatedProduct.setProductType(cbProductType.getValue());
                    if(lblValue.isVisible()) {
                        updatedProduct.setProductCoef(Integer.parseInt(lblValue.getText()));
                        updatedProduct.setProductSmallUnit(lblSmallUnit.getText());
                        updatedProduct.setProductBigUnit(lblBigUnit.getText());
                    } else {
                        updatedProduct.setProductCoef(1);
                        updatedProduct.setProductSmallUnit(lblSmallUnit.getText());
                        updatedProduct.setProductBigUnit(lblSmallUnit.getText());
                    }

                    if (productDAO.updateProduct(updatedProduct)) {
                        showAlert("Notification","Cập nhật sản phẩm thành công!");
                        btnEdit.setText("Sửa");
                        btnEdit.setId("edit");
                        clearInformation();
                        loadProducts();
                    } else {
                        showAlert("Warning","Thất bại");
                    }
                }
            } else {
                showAlert("Warning","Thông tin đã nhập chưa đầy đủ!");
            }
        }

    }

    public void btnDeleteClicked(MouseEvent mouseEvent) {
        if (!tfProductId.getText().isEmpty()) {
            int sequence = ShowYesNoAlert("xoá sản phẩm "+tfProductName.getText());
            if(sequence==JOptionPane.YES_OPTION) {
                try {
                    if(productDAO.deleteProduct(Integer.parseInt(tfProductId.getText()))) {
                        showAlert("Notification","Xoá sản phẩm "+tfProductName.getText()+" thành công!");
                        clearInformation();
                        loadProducts();
                    } else showAlert("Warning","Thất bại!");
                } catch (ForeignKeyViolationException e ){
                    showAlert("Warning",e.getMessage());
                }

            }
        }
    }

    private void loadProductTypes() {
        List<String> productTypes = productDAO.getProductTypes();
        cbProductType.setItems(FXCollections.observableArrayList(productTypes));
    }

    private void loadUnits() {
        if(!cbUnit.getItems().isEmpty()) cbUnit.getItems().clear();
        ObservableList<Unit>  units = unitDAO.getUnitData();
        for(Unit unit : units) {
            cbUnit.getItems().add(unit.toString());
        }
        cbUnit.getItems().add("Thêm khác");
    }

    private void loadProducts() {
        productList = productDAO.getAllProduct();
        tblProduct.setItems(productList);
    }

    private boolean checkInformation() {
        return !tfProductName.getText().isEmpty() &&
                !tfProductPrice.getText().isEmpty() &&
                !tfProductDescription.getText().isEmpty() &&
                !tfProductOrigin.getText().isEmpty() &&
                cbUnit.getValue() != null &&
                cbProductType.getValue() != null;
    }

    private boolean checkInformationEdit() {
        return  !tfProductName.getText().isEmpty() &&
                !tfProductPrice.getText().isEmpty() &&
                !tfProductDescription.getText().isEmpty() &&
                !tfProductOrigin.getText().isEmpty() &&
                cbUnit.getValue() != null &&
                cbProductType.getValue() != null;
    }

    private void clearInformation() {
        tfProductId.clear();
        tfProductName.clear();
        tfProductPrice.clear();
        tfProductDescription.clear();
        tfProductOrigin.clear();
        cbUnit.setValue(null);
        cbProductType.setValue(null);
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

    public void handleCancel(ActionEvent event) {
        SetDisable(true);
        clearInformation();
        btnEdit.setText("Sửa");
        btnEdit.setId("edit");
        btnAdd.setText("Thêm");
        btnAdd.setId("add");
        cbUnit.setVisible(true);
        lblValue.setText("Giá trị");
        lblSmallUnit.setText("đơn vị bé");
        lblOn.setText("trên");
        lblBigUnit.setText("đơn vị lớn");
        hboxUnit.setLayoutX(40);
        hboxUnit.setLayoutY(255);//225
    }

    public void handleAddUnit(ActionEvent event) {
        SetTFMiniDisable(false);
        clearTFMini();
        btnMini.setText("Thêm");
        btnMini.setId("add");
        lblBigUnitMini.setVisible(true);
        tf_bigUnit.setVisible(true);
        lbl_Coef.setVisible(true);
        tf_coef.setVisible(true);
    }

    public void handleDeleteUnit(ActionEvent event) {
        SetTFMiniDisable(true);
        clearTFMini();
        btnMini.setText("Xoá");
        btnMini.setId("delete");
        lblBigUnitMini.setVisible(true);
        tf_bigUnit.setVisible(true);
        lbl_Coef.setVisible(true);
        tf_coef.setVisible(true);
    }
    public void handleSimpleUnitName(ActionEvent event) {
        SetTFMiniDisable(false);
        clearTFMini();
        btnMini.setText("Thêm");
        btnMini.setId("add");
        lblBigUnitMini.setVisible(false);
        tf_bigUnit.setVisible(false);
        lbl_Coef.setVisible(false);
        tf_coef.setVisible(false);
    }
    private void SetTFMiniDisable(boolean bool) {
        tf_coef.setDisable(bool);
        tf_bigUnit.setDisable(bool);
        tf_smallUnit.setDisable(bool);
    }
    private void clearTFMini(){
        tf_coef.clear();
        tf_bigUnit.clear();
        tf_smallUnit.clear();
    }

    public void close(MouseEvent mouseEvent) {
        panel_Unit.setVisible(false);
    }

    public void handleClickedUnitTBL(MouseEvent mouseEvent) {
        if(!btnMini.getText().equals("Thêm")) {
            Unit unit = tbl_Unit.getSelectionModel().getSelectedItem();
            tf_smallUnit.setText(unit.getSmallUnit());
            if(unit.getBigUnit().equals(unit.getSmallUnit())) {
                lbl_Coef.setVisible(false);
                tf_coef.setVisible(false);
                tf_bigUnit.setVisible(false);
                lblBigUnitMini.setVisible(false);
            } else {
                tf_coef.setText(String.valueOf(unit.getValue()));
                tf_bigUnit.setText(unit.getBigUnit());
                lbl_Coef.setVisible(true);
                tf_coef.setVisible(true);
                tf_bigUnit.setVisible(true);
                lblBigUnitMini.setVisible(true);
            }

        }
    }

    public void btnMiniClicked(MouseEvent mouseEvent) {
        if(btnMini.getText().equals("Thêm")) {
            if(checkInformationUnit()) {
                Unit unit = new Unit();
                unit.setSmallUnit(tf_smallUnit.getText());
                if(!tf_bigUnit.isVisible()||tf_smallUnit.getText().equals(tf_bigUnit.getText())){
                    unit.setValue(1);
                    unit.setBigUnit(tf_smallUnit.getText());
                }
                else {
                    unit.setValue(Integer.parseInt(tf_coef.getText()));
                    unit.setBigUnit(tf_bigUnit.getText());
                }
                int sequence = ShowYesNoAlert("Thêm đơn vị " + unit.toString());
                if(sequence==JOptionPane.YES_OPTION) {
                    if(unitDAO.AddToDB(unit.getValue(),unit.getSmallUnit(),unit.getBigUnit())){
                        showAlert("Notification","Thêm thành công!");
                        clearInformation();
                        loadUnits();
                        loadUnitTable();
                    } else showAlert("Warning","Thất bại!");
                } else {}
            } else {
                showAlert("Warning","Kiểm tra thông tin nhập!");
            }
        }
        else if (btnMini.getText().equals("Xoá")) {
            Unit unit = tbl_Unit.getSelectionModel().getSelectedItem();
            if(unit!=null){
                int sequence = ShowYesNoAlert("xoá" );
                if(sequence==JOptionPane.YES_OPTION) {
                    try {
                        if(unitDAO.Delete(unit.getId())) {
                            showAlert("Notification","Xoá thành công!");
                            loadUnits();
                            loadUnitTable();
                            clearInformation();
                        } else showAlert("Warning","Thất bại!");
                    } catch (ForeignKeyViolationException e) {
                        showAlert("Warning",e.getMessage());
                    }
                } {}
            }
        }
    }
    private void SearchProduct(String searchString) {
        ObservableList<Product> lists = productDAO.searchProduct(searchString);
        tblProduct.setItems(lists);
    }
    private boolean isValidInput(String input) {
        String regex = ".*[a-zA-Z\\d\\u00C0-\\u1FFF\\u2C00-\\uD7FF]+.*";
        return input.matches(regex);
    }
    private boolean checkInformationUnit() {
        return !tf_coef.getText().isEmpty() && ((!tf_smallUnit.getText().isEmpty() &&
                !tf_bigUnit.getText().isEmpty())||(!tf_bigUnit.isVisible()&&!tf_smallUnit.getText().isEmpty()));
    }
}
