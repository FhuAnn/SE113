package com.example.se330_pharmacy.Controllers;

import com.example.se330_pharmacy.DataAccessObject.PayslipDAO;
import com.example.se330_pharmacy.DataAccessObject.ReceiptDAO;
import com.example.se330_pharmacy.Models.Model;
import com.example.se330_pharmacy.Models.Payslip;
import com.example.se330_pharmacy.Models.Receipt;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.swing.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AddReceiptController implements Initializable {

    public ComboBox<String> cbStatusReceipt;
    public Button btnExport;
    public Button btnCancel;
    public Text lbl_createDate;
    public Text lbl_totalPay_red,lbl_totalPay_green;
    public Text lbl_thangLuong,lbl_title;
    public Text lbl_hoTen;
    public Text lbl_maNhanVien;
    public Text lbl_thongTinYours;
    public Text lbl_viTri,lbl_maHoaDon,lbl_showStinkOrStonk;
    public TextArea ta_content;
    public TextArea ta_note;
    ReceiptController receiptController_init ;
    ReceiptDAO receiptDAO ;
    PayslipDAO payslipDAO;
    Payslip payslip;
    Receipt receipt;
    int idCharger;
    PaySlipController paySlipController_init;
    public void initData(Payslip _payslip,int _idCharger,String _employnameCharger,String _vitricharger,PaySlipController paySlipController, ReceiptController receiptController, Receipt _receipt)
    {
        if(receiptController==null) { // goi tu PaySlip
            payslip=_payslip;
            idCharger=_idCharger;
            paySlipController_init = paySlipController;
            lbl_maHoaDon.setText("Default");
            lbl_thongTinYours.setText(_idCharger+" - " + _employnameCharger+ " - " + _vitricharger);
            lbl_maNhanVien.setText(String.valueOf(_payslip.getEmployee_id()));
            lbl_hoTen.setText(_payslip.getTenNhanVien());
            lbl_viTri.setText(_payslip.getViTriLamViec());

            lbl_title.setText("Tháng lương: ");
            lbl_thangLuong.setText(String.valueOf(_payslip.getCreateDate().toLocalDate().getMonthValue()));
            lbl_createDate.setText(String.valueOf(LocalDate.now()));
            lbl_showStinkOrStonk.setText("Tổng trả");
            lbl_totalPay_red.setText("- "+_payslip.getTotalPay());
            lbl_totalPay_red.setVisible(true);
            lbl_totalPay_green.setVisible(false);
            ta_content.setText("Thanh toán lương tháng "+lbl_thangLuong.getText()+ " cho nhân viên: "+ _payslip.getPayslip_id() +_payslip.getEmployee_id()+"-"+_payslip.getTenNhanVien());
        } else {// goi tu Receipt
            receipt= _receipt;
            idCharger=_idCharger;
            lbl_maHoaDon.setText(String.valueOf(receipt.getReceipt_id()));
            receiptController_init = receiptController;
            lbl_thongTinYours.setText(_idCharger+" - " + _employnameCharger+ " - " + _vitricharger);
            lbl_maNhanVien.setText(String.valueOf(receipt.getEmployee_id()));
            lbl_hoTen.setText(receipt.getTenNhanVien());
            lbl_viTri.setText(receipt.getViTriNhanVien());

            if(payslipDAO.isReceiptOfPayslip(_receipt.getReceipt_id())) {
                lbl_title.setText("Tháng lương");
                lbl_showStinkOrStonk.setText("Tổng trả");
                lbl_totalPay_red.setText("- "+receipt.getTotalPay());
                lbl_totalPay_red.setVisible(true);
                lbl_totalPay_green.setVisible(false);
                lbl_thangLuong.setText(String.valueOf(_receipt.getCreateDate().toLocalDate().getMonthValue()));
            } else  {
                lbl_title.setText("Hoá đơn");
                String typeReceipt = receipt.getContent().split(" ")[0];
                if(typeReceipt.equals("Import")) {
                    lbl_showStinkOrStonk.setText("Tổng trả");
                    lbl_totalPay_red.setText("-"+receipt.getTotalPay());
                    lbl_totalPay_red.setVisible(true);
                    lbl_totalPay_green.setVisible(false);
                } else {
                    lbl_showStinkOrStonk.setText("Tổng nhận");
                    lbl_totalPay_green.setText("+ "+receipt.getTotalPay());
                    lbl_totalPay_green.setVisible(true);
                    lbl_totalPay_red.setVisible(false);
                }
                lbl_thangLuong.setText(_receipt.getContent());
            }
            lbl_createDate.setText(receipt.getCreateDate().toString());
            ta_content.setText(receipt.getContent());
            if(receipt.getStatus().equals("InComplete")) {
                cbStatusReceipt.setDisable(false);
                ta_note.setDisable(false);
            } else {
                cbStatusReceipt.setDisable(true);
                ta_note.setDisable(true);
                cbStatusReceipt.setValue("Completed");
            }
            ta_content.setDisable(true);
            btnExport.setText("Lưu");
        }

    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setUp();
        setAction();
    }

    private void setUp() {
        receiptController_init = new ReceiptController();
        receiptDAO = new ReceiptDAO();
        payslipDAO = new PayslipDAO();
        ObservableList<String> statusList = FXCollections.observableArrayList("InComplete", "Completed");
        cbStatusReceipt.setItems(statusList);
    }

    private void setAction() {
        btnExport.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ExportOrSave();
            }
        });
        btnCancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                    Stage stage = (Stage) btnCancel.getScene().getWindow();
                    Model.getInstance().getViewFactory().closeStage(stage);
            }
        });
    }

    private void ExportOrSave() {

            if(CheckFilled()&& btnExport.getText().equals("Xuất")) {
                    Receipt receipt = new Receipt();
                    receipt.setEmployee_id(payslip.getEmployee_id());
                    receipt.setContent(ta_content.getText());
                    receipt.setTotalPay(payslip.getTotalPay());
                    receipt.setNote(ta_note.getText());
                    receipt.setStatus(cbStatusReceipt.getValue());
                    receipt.setPersoncharge_id(idCharger);
                    int receipt_id_return = receiptDAO.AddReceiptToDB(receipt);
                    if (receipt_id_return > 0) {
                        if (payslipDAO.UpdatePayslipCompleted(payslip.getPayslip_id(), receipt_id_return)) {
                            paySlipController_init.LoadListPayslip();
                            showAlert("Warning", "Thêm dữ liệu thành công");
                            Model.getInstance().getViewFactory().closeStage((Stage) btnExport.getScene().getWindow());
                        }
                    }
            }
            else { // nếu là Lưu
                if(CheckFilled() && ShowYesNoAlert("lưu hoá đơn có mã " +receipt.getReceipt_id())==JOptionPane.YES_OPTION) {
                    if(!ta_note.getText().isEmpty()||!cbStatusReceipt.getValue().equals("Incomplete")) {
                        receipt.setNote(ta_note.getText());
                        receipt.setStatus(cbStatusReceipt.getValue());
                        receipt.setPersoncharge_id(idCharger);
                        if(receiptDAO.UpdateReceiptToDB(receipt)) {
                            receiptController_init.LoadListReceipt();
                            Model.getInstance().getViewFactory().closeStage((Stage) btnExport.getScene().getWindow());
                            showAlert("Warning","Cập nhật dữ liệu thành công");
                        } else {
                            showAlert("Warning","Error");
                        }
                    }
                } else {}

            }
    }

    private boolean CheckFilled() { // tra ve 0 khi khong ton tai textfield nao duoc fill

        if(ta_content.getText().isEmpty()) {
            showAlert("Warning","Chưa nhập đầy đủ thông tin!");
            return false;
        }
        if(cbStatusReceipt.getValue()==null) {
            showAlert("Warning","Chưa nhập đầy đủ thông tin!");
            return false;
        }
        return true;
    }
    @FXML
    void close(MouseEvent event) {
        Stage s = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(s);
    }
    private int ShowYesNoAlert(String string) {
        JFrame frame = new JFrame("Table Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        return JOptionPane.showConfirmDialog(frame, "Có phải bạn muốn "+string+" ?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
    }
    private void showAlert(String tilte,String string) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(tilte);
        alert.setHeaderText(null);
        alert.setContentText(string);
        alert.showAndWait();
    }
}
