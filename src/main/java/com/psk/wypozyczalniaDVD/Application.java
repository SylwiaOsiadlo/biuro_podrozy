package com.psk.wypozyczalniaDVD;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Objects;

public class Application extends javafx.application.Application {

    private final ClientConnection clientConnection;

    private static final Logger logger = LogManager.getLogger(Application.class);

    public Application() {
        clientConnection = new ClientConnection();
        clientConnection.connect("localhost", 8080);
    }

    public static void main(String[] args) {
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

        String imagePath = Objects.requireNonNull(getClass().getResource("/appLogo.png")).toExternalForm();
        Image image = new Image(imagePath);

        ImageView imageView = new ImageView(image);
        content.getChildren().add(imageView);
        imageView.setTranslateX(200);
        imageView.setTranslateY(100);

        // Ustawienie akcji po naciśnięciu przycisku
        btnDVD.setOnAction(e -> {
            AlbumForm albumForm = new AlbumForm();

            content.getChildren().clear();
            content.getChildren().add(albumForm.getContent(clientConnection));
        });

        btnKlient.setOnAction(e -> {
            KlientForm klientForm = new KlientForm();

            content.getChildren().clear();
            content.getChildren().add(klientForm.getContent(clientConnection));
        });

        btnWypozycz.setOnAction(e -> {
            WypozyczenieForm wypozyczenieForm = new WypozyczenieForm();

            content.getChildren().clear();
            content.getChildren().add(wypozyczenieForm.getContent(clientConnection));

        });

        btnZwroc.setOnAction(e -> {
            ZwrotForm zwrotForm = new ZwrotForm();

            content.getChildren().clear();
            content.getChildren().add(zwrotForm.getContent(clientConnection));

        });
        btnNav.getChildren().add(btnDVD);
        btnNav.getChildren().add(btnKlient);
        btnNav.getChildren().add(btnWypozycz);
        btnNav.getChildren().add(btnZwroc);
        // Tworzenie układu dla głównego okna
        VBox layout = new VBox();
        layout.getChildren().add(btnNav);

        Separator separator = new Separator();

        layout.getChildren().add(separator);
        layout.getChildren().add(content);
        VBox.setVgrow(layout, Priority.ALWAYS);
        Scene scene = new Scene(layout, 800, 600);

        primaryStage.setScene(scene);

        primaryStage.setOnCloseRequest(t -> {
            try {
                clientConnection.sendCommand("bye");
                clientConnection.closeConnection();
            } catch (IOException e) {
                logger.error(e.getMessage());
                throw new RuntimeException(e);
            }
            Platform.exit();
            System.exit(0);
        });
        primaryStage.show();
    }
}
