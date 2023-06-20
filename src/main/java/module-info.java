module com.psk.psk {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    opens com.psk.wypozyczalniaDVD_klient;
    requires lombok;
    requires java.persistence;
    requires java.sql;

    exports com.psk.wypozyczalniaDVD_klient;
}