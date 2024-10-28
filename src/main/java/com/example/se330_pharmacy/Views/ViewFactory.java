package com.example.se330_pharmacy.Views;

import com.example.se330_pharmacy.Controllers.*;
import com.example.se330_pharmacy.Models.Employee;
import com.example.se330_pharmacy.Models.Payslip;
import com.example.se330_pharmacy.Models.Receipt;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Scale;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.io.IOException;

public class ViewFactory {

    Stage stageMenu = null;
    Stage stageAddReceiptAccountant = null;
    Stage stageAddPayslipAccountant = null;
    Stage stageProfile =null;
    public ViewFactory(){}

    public void showLoginWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/se330_pharmacy/Fxml/Login.fxml"));
        createStage(loader);
    }

    public void showMenuWindow(Employee employee) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/se330_pharmacy/Fxml/Menu.fxml"));
        Pane pane = loader.load();
        VBox root = new VBox(pane);
        Dimension resolution = Toolkit.getDefaultToolkit().getScreenSize();
        double width = resolution.getWidth();
        double height = resolution.getHeight();
        double w = width/1600;  // your window width
        double h = height/900;  // your window height
        Scale scale = new Scale(w, h, 0, 0);
        root.getTransforms().add(scale);
        Scene scene = new Scene(root);
        stageMenu = new Stage(StageStyle.UNDECORATED);
        stageMenu.setScene(scene);
        stageMenu.setMaximized(true);
        MenuController menuController = loader.getController();
        menuController.initData(employee);
        stageMenu.show();
    }

    public void showAddReceiptWindow(Payslip payslip, int _idCharger, String _employnameCharger, String _vitricharger, /*PaySlipController paySlipController, ReceiptController receiptController,*/ Receipt receipt) {
    }
    public void showAddPayslipWindow(Payslip payslip/* PaySlipController paySlipController*/) {
    }
    private Stage createStage(FXMLLoader loader) {
        Scene scene = null;
        try {
            scene = new Scene(loader.load());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
        return stage;
    }
    public void showProfileWindow (String id,String name,String username,String pos)
    {
        if(stageProfile==null)
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/se330_pharmacy/Fxml/Profile.fxml"));
            stageProfile=createStage(loader);
            ProfileController profileController = loader.getController();
            profileController.initData(id,name,username,pos);
        }
        else
        {
            stageProfile.toFront();
        }
    }
    public void closeStage(Stage stage)
    {
        stage.close();
        if(stageMenu!=null &&!stageMenu.isShowing()) // nếu menu đóng thì đóng hết những window đang mở
        {
            if(stageAddReceiptAccountant!=null) stageAddReceiptAccountant.close();
            if(stageAddPayslipAccountant!=null) stageAddPayslipAccountant.close();
            if(stageProfile!=null) stageProfile.close();
        }
        //khi 1 stage nào đó đóng thì cập nhật tình hình các stage khác
        if(stageAddReceiptAccountant!=null && !stageAddReceiptAccountant.isShowing()) stageAddReceiptAccountant=null;  // nếu khác null nhưng ko còn show thì cập nhật về null
        if(stageAddPayslipAccountant!=null && !stageAddPayslipAccountant.isShowing()) stageAddPayslipAccountant=null;
        if(stageProfile!=null && !stageProfile.isShowing()) stageProfile=null;
    }
    public void minimizeStage(Stage stage)
    {
        stage.setIconified(true);
    }
}
