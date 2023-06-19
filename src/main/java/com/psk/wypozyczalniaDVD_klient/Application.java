package com.psk.wypozyczalniaDVD_klient;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {

    private final ClientConnection clientConnection;

    public Application() {
        clientConnection = new ClientConnection();
        clientConnection.connect("localhost", 8000);
    }

    public static void main(String[] args) {
        //Application app = new Application(args);
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Wypożyczalnia DVD");

        HBox btnNav = new HBox();
        btnNav.setSpacing(5.0f);
        btnNav.setPadding(new Insets(5.0f));

        // Tworzenie przycisku
        Button btnDVD = new Button("Zarządzaj płytami DVD");
        Button btnKlient = new Button("Zarządzaj klientami");
        Button btnWypozycz = new Button("Wypożycz DVD");
        Button btnZwroc = new Button("Zwróć DVD");
        VBox content = new VBox();
        VBox.setVgrow(content, Priority.ALWAYS);

        // Ustawienie akcji po naciśnięciu przycisku
        btnDVD.setOnAction(e -> {
            AlbumForm albumForm = new AlbumForm();

            content.getChildren().clear();
            content.getChildren().add(albumForm.getStage(clientConnection));
        });

        btnNav.getChildren().add(btnDVD);
        btnNav.getChildren().add(btnKlient);
        btnNav.getChildren().add(btnWypozycz);
        btnNav.getChildren().add(btnZwroc);
        // Tworzenie układu dla głównego okna
        VBox layout = new VBox();
        layout.getChildren().add(btnNav);

        Separator separator = new Separator();
        //separator.setStyle("-fx-border-width: 1px; -fx-border-color: black;");

        layout.getChildren().add(separator);
        layout.getChildren().add(content);
        VBox.setVgrow(layout, Priority.ALWAYS);
        Scene scene = new Scene(layout, 600, 450);

        primaryStage.setScene(scene);

        primaryStage.setOnCloseRequest(t -> {
            try {
                clientConnection.closeConnection();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Platform.exit();
            System.exit(0);
        });
        primaryStage.show();
    }
}
