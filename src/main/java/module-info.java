module com.example.se330_pharmacy {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires java.desktop;
    requires annotations;
    requires itextpdf;
    requires java.mail;
    requires org.postgresql.jdbc;


    opens com.example.se330_pharmacy to javafx.fxml;
    exports com.example.se330_pharmacy;
    opens com.example.se330_pharmacy.Controllers to javafx.fxml;
    exports com.example.se330_pharmacy.Models;
    exports com.example.se330_pharmacy.Views;
    opens com.example.se330_pharmacy.Models to javafx.fxml;
}