package com.psk.wypozyczalniaDVD_klient;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Application extends javafx.application.Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Wypożyczalnia DVD");

        // Tworzenie przycisku
        Button button = new Button("Zarządzaj płytami DVD");

        // Ustawienie akcji po naciśnięciu przycisku
        button.setOnAction(e -> {
            AlbumForm albumForm = new AlbumForm();

            primaryStage.setScene(albumForm.getStage());
            //albumForm.openStage();
        });

        // Tworzenie układu dla głównego okna
        VBox layout = new VBox();
        layout.getChildren().add(button);
        Scene scene = new Scene(layout, 600, 450);

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
