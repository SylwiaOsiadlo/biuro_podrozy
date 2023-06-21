package com.psk.wypozyczalniaDVD_klient;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;

import java.util.List;

public class AlbumForm {

    private final ObservableList<Album> albums = FXCollections.observableArrayList();

    public VBox getStage(ClientConnection con) {
        // Tworzenie etykiet i pól tekstowych
        Label nameLabel = new Label("Nazwa płyty:");
        TextField nameTextField = new TextField();

        Label genreLabel = new Label("Gatunek:");
        TextField genreTextField = new TextField();

        Label quantityLabel = new Label("Ilość sztuk:");
        TextField quantityTextField = new TextField();

        Label cenaLabel = new Label("Cena:");
        TextField cenaTextField = new TextField();

        Button addButton = new Button("Dodaj");
        Button editButton = new Button("Edytuj");
        Button deleteButton = new Button("Usuń");

        // Tworzenie siatki i dodawanie komponentów
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(5);
        grid.setHgap(5);

        grid.add(nameLabel, 0, 0);
        grid.add(nameTextField, 1, 0);

        grid.add(genreLabel, 0, 1);
        grid.add(genreTextField, 1, 1);

        grid.add(quantityLabel, 0, 2);
        grid.add(quantityTextField, 1, 2);

        grid.add(cenaLabel, 0, 3);
        grid.add(cenaTextField, 1, 3);

        HBox buttonBox = new HBox();

        grid.add(buttonBox, 0, 4, 2, 1);

        buttonBox.getChildren().add(addButton);
        buttonBox.getChildren().add(editButton);
        buttonBox.getChildren().add(deleteButton);

        List<Album> receivedList = (List<Album>) con.requestObject("plytyList");
        albums.addAll(receivedList);
        TableView<Album> tableView = new TableView<>();

        addButton.setOnAction(e -> {
            String name = nameTextField.getText();
            String genre = genreTextField.getText();
            int quantity = Integer.parseInt(quantityTextField.getText());
            float cena = Float.parseFloat(cenaTextField.getText());

            Album album = new Album(name, genre, quantity, cena);
            albums.add(album);
            con.sendObject("plytyAdd", album);

            // Czyść pola tekstowe po dodaniu
            nameTextField.clear();
            genreTextField.clear();
            quantityTextField.clear();
        });

        editButton.setOnAction(e -> {
            String name = nameTextField.getText();
            String genre = genreTextField.getText();
            int quantity = Integer.parseInt(quantityTextField.getText());
            float cena = Float.parseFloat(cenaTextField.getText());

            Album selectedAlbum = tableView.getSelectionModel().selectedItemProperty().get();
            long selectedId = selectedAlbum.getId();
            Album album = new Album(selectedId, name, genre, quantity, cena);

            selectedAlbum.setName(name);
            selectedAlbum.setGenre(genre);
            selectedAlbum.setQuantity(quantity);
            selectedAlbum.setCena(cena);
            tableView.refresh();

            con.sendObject("plytyEdit", album);

            // Czyść pola tekstowe po dodaniu
            nameTextField.clear();
            genreTextField.clear();
            quantityTextField.clear();
            cenaTextField.clear();
        });

        deleteButton.setOnAction(e -> {

            Album selectedAlbum = tableView.getSelectionModel().selectedItemProperty().get();
            long selectedId = selectedAlbum.getId();
            Album album = new Album(selectedId, "", "", 0, 0);

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Zapytanie");
            alert.setHeaderText("Czy na pewno chcesz usunąć tę płytę?");

            ButtonType buttonTypeTak = new ButtonType("Tak");
            ButtonType buttonTypeNie = new ButtonType("Nie");

            alert.getButtonTypes().setAll(buttonTypeTak, buttonTypeNie);

            alert.showAndWait().ifPresent(response -> {
                if (response == buttonTypeTak) {
                    System.out.println("Wybrano Tak");
                    con.sendObject("plytyDel", album);

                    albums.remove(selectedAlbum);
                    tableView.refresh();
                    alert.close();
                } else if (response == buttonTypeNie) {
                    System.out.println("Wybrano Nie");
                    alert.close();
                }
            });

            // Czyść pola tekstowe po dodaniu
            nameTextField.clear();
            genreTextField.clear();
            quantityTextField.clear();
            cenaTextField.clear();
        });

        // Tworzenie tabeli
        TableColumn<Album, String> nameColumn = new TableColumn<>("Nazwa płyty");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Album, String> genreColumn = new TableColumn<>("Gatunek");
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));

        TableColumn<Album, Integer> quantityColumn = new TableColumn<>("Ilość sztuk");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        TableColumn<Album, Integer> cenaColumn = new TableColumn<>("Cena");
        cenaColumn.setCellValueFactory(new PropertyValueFactory<>("cena"));

        TableColumn<Album, Integer> wypozuczeniaColumn = new TableColumn<>("Wypożyczenia");
        wypozuczeniaColumn.setCellValueFactory(new PropertyValueFactory<>("wypozyczenia"));

        tableView.getColumns().addAll(nameColumn, genreColumn, quantityColumn, cenaColumn, wypozuczeniaColumn);
        tableView.setItems(albums);

        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                nameTextField.setText(newValue.getName());
                genreTextField.setText(newValue.getGenre());
                quantityTextField.setText(String.valueOf(newValue.getQuantity()));
                cenaTextField.setText(String.valueOf(newValue.getCena()));
            }
        });

        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(10, 10, 10, 10));

        vbox.getChildren().addAll(grid, tableView);
        return vbox;
    }
}
