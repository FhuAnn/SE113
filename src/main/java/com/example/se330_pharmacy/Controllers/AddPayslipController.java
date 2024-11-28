package com.example.se330_pharmacy.Controllers;

import com.example.se330_pharmacy.DataAccessObject.PayslipDAO;
import com.example.se330_pharmacy.Models.Model;
import com.example.se330_pharmacy.Models.Payslip;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.swing.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AddPayslipController implements Initializable { // su dung chung cho them va sua

    public TextArea ta_content;
    public TextField tf_tongTra;
    public DatePicker dp_date;
    public ComboBox<String> cbStatus;
    public Button btnAdd;
    public Button btnCancel;
    public TextField tf_ghiChu;
    public TextField tf_maNhanVien;
    public TextField tf_phieuLuongId;
    public Text lbl_id;
    Payslip payslip_init;
    String editedString ;
    int index;
    PayslipDAO payslipDAO;
    PaySlipController paySlipController;
    public void initData(Payslip _payslip,PaySlipController _paySlipController)
    {
        paySlipController=_paySlipController;
        if(_payslip!=null)  // sua
        {
            lbl_id.setVisible(true);
            tf_phieuLuongId.setVisible(true);

            payslip_init=_payslip;
            btnAdd.setDisable(false);
            btnAdd.setText("Lưu");
            tf_maNhanVien.setDisable(true);
            ta_content.setDisable(true);
            tf_tongTra.setDisable(false);
            tf_ghiChu.setDisable(false);
            tf_maNhanVien.setText(String.valueOf(payslip_init.getEmployee_id()));
            tf_tongTra.setText(String.valueOf(payslip_init.getTotalPay()));
            tf_phieuLuongId.setText(String.valueOf(payslip_init.getPayslip_id()));
            ta_content.setText(payslip_init.getContent());
            tf_ghiChu.setText(payslip_init.getNote());
            cbStatus.setValue(payslip_init.getStatus());
            dp_date.setValue(payslip_init.getCreateDate().toLocalDate());
            editedString = tf_ghiChu.getText()+tf_tongTra.getText();
        } else {  // them
            lbl_id.setVisible(false);
            tf_phieuLuongId.setVisible(false);
            tf_maNhanVien.setDisable(false);
            tf_ghiChu.setDisable(false);
            ta_content.setDisable(false);
            tf_tongTra.setDisable(false);
            cbStatus.setValue("InComplete");
            dp_date.setValue(LocalDate.now());
            btnAdd.setText("Thêm");
            btnAdd.setDisable(false);
            ta_content.setText("Thanh toán lương tháng ?");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SetUp();
        SetAddListener();
    }
    private void addListenerTextChanged(TextField...tfs) {
        for(TextField tf : tfs)
        {
            tf.textProperty().addListener((observable,oldValues,newValue)->{
                if (!newValue.matches("\\d*")) {
                    tf.setText(newValue.replaceAll("[^\\d]", ""));
                    showAlert("Warning","Chỉ được nhập số !");
                    }
            });
        }
    }
    private void SetAddListener() {
        btnAdd.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                    if(CheckFilled()) {
                        AddToDatabase();
                    }
                    else showAlert("Warning","Chưa điền đầy đủ thông tin!");
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

    private boolean CheckFilled() {
        if(tf_maNhanVien.getText().isEmpty()) return false;
        if(ta_content.getText().isEmpty()) return false;
        if(tf_tongTra.getText().isEmpty()) return false;
        return true;
    }

    private void AddToDatabase() {
        if(payslip_init==null) {
            int sequence =  ShowYesNoAlert("Thêm phiếu lương ?");
            if(sequence == JOptionPane.YES_OPTION)
            {
                Payslip payslip = new Payslip();
                payslip.setEmployee_id(Integer.parseInt(tf_maNhanVien.getText()));
                payslip.setContent(ta_content.getText());
                payslip.setTotalPay(Integer.parseInt(tf_tongTra.getText()));
                payslip.setStatus("InComplete");
                payslip.setNote(tf_ghiChu.getText());
                if(payslipDAO.AddPaySlip(payslip)){
                    showAlert("Warning","Thêm vào cơ sở dữ liệu thành công!");
                    paySlipController.LoadListPayslip();
                    Stage stage =(Stage) btnAdd.getScene().getWindow();
                    Model.getInstance().getViewFactory().closeStage(stage);
                }
            } else {}
        }
        else {
            if(!editedString.equals(tf_ghiChu.getText()+tf_tongTra.getText())) {
                int sequence = ShowYesNoAlert("cập nhật phiếu lương có tên " + payslip_init.getPayslip_id() + "-" + payslip_init.getTenNhanVien());
                if (sequence == JOptionPane.YES_OPTION) {
                    payslip_init.setNote(tf_ghiChu.getText());
                    payslip_init.setTotalPay(Integer.parseInt(tf_tongTra.getText()));
                    if (payslipDAO.UpdatePaySlip(payslip_init)) {
                        showAlert("Warning", "Cập nhật cơ sở dữ liệu thành công!");
                        paySlipController.LoadListPayslip();
                    }
                }
            }
            Stage stage = (Stage) btnCancel.getScene().getWindow();
            Model.getInstance().getViewFactory().closeStage(stage);
        }
    }

    private void SetUp() {
        payslipDAO = new PayslipDAO();
        dp_date.setDisable(true);
        addListenerTextChanged(tf_maNhanVien,tf_tongTra);
    }

    @FXML
    void close(MouseEvent event) {
        Stage s = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(s);
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
        return JOptionPane.showConfirmDialog(frame, "Có phải bạn muốn "+string+" ?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
    }
}
