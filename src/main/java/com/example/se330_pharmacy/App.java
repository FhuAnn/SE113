package com.example.se330_pharmacy;

import com.example.se330_pharmacy.Models.Employee;
import com.example.se330_pharmacy.Models.Model;


import javafx.application.Application;


import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Model.getInstance().getViewFactory().showLoginWindow();
        /*Model.getInstance().getViewFactory().showMenuWindow(new Employee());*/
    }
}
