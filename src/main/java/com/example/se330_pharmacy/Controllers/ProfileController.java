package com.example.se330_pharmacy.Controllers;

import com.example.se330_pharmacy.Models.Model;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ProfileController {
    @FXML
    public Label employee_id_lbl;
    @FXML
    public Label employname_lbl;
    @FXML
    public Label username_lbl;
    @FXML
    public Label position_lbl;
    private Stage stage;
    public Pane lbl_header;
    private double xOffset = 0;
    private double yOffset =0;
    public ProfileController()
    {
    }
    public void initData(String _id,String _name,String _username,String _pos)
    {
        employee_id_lbl.setText(_id);
        employname_lbl.setText(_name);
        username_lbl.setText(_username);
        position_lbl.setText(_pos);
        lbl_header.setOnMousePressed(mouseEvent -> {
            xOffset = mouseEvent.getSceneX();
            yOffset = mouseEvent.getSceneY();
        });

        lbl_header.setOnMouseDragged(mouseEvent -> {
            Stage stage = (Stage) lbl_header.getScene().getWindow();
            stage.setX(mouseEvent.getScreenX()-xOffset);
            stage.setY(mouseEvent.getScreenY()-yOffset);
        });
    }

    public void  closeProfileView(MouseEvent mouseEvent) {
        stage = (Stage) employee_id_lbl.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(stage);
    }

    public void minimizeProfile(MouseEvent mouseEvent) {
        Model.getInstance().getViewFactory().minimizeStage((Stage)lbl_header.getScene().getWindow() );
    }
}
