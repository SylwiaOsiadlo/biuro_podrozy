package com.psk.wypozyczalniaDVD_klient;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;

import java.util.List;

public class WypozyczenieForm {

    private final ObservableList<Wypozyczenie> wypozyczenia = FXCollections.observableArrayList();

    private long lastIdFromDb = 0;

    public VBox getContent(ClientConnection con) {
        // Tworzenie etykiet i pól tekstowych
        Label dvdLabel = new Label("Numer płyty:"); //id płyty
        TextField dvdTextField = new TextField();

        Label clientLabel = new Label("Numer klienta:"); // id klient
        TextField clientTextField = new TextField();

        Label dataLabel = new Label("Data wypożyczenia:");
        TextField dataTextField = new TextField();


        Button addButton = new Button("Dodaj");
        Button editButton = new Button("Edytuj");
        Button deleteButton = new Button("Usuń");

        // Tworzenie siatki i dodawanie komponentów
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(5);
        grid.setHgap(5);

        grid.add(dvdLabel, 0, 0);
        grid.add(dvdTextField, 1, 0);

        grid.add(clientLabel, 0, 1);
        grid.add(clientTextField, 1, 1);

        grid.add(dvdLabel, 0, 2);
        grid.add(dvdTextField, 1, 2);

        HBox buttonBox = new HBox();

        grid.add(buttonBox, 0, 4, 2, 1);

        buttonBox.getChildren().add(addButton);
        buttonBox.getChildren().add(editButton);
        buttonBox.getChildren().add(deleteButton);

        List<Wypozyczenie> receivedList = (List<Wypozyczenie>) con.requestObject("wypozyczenieList");
        wypozyczenia.addAll(receivedList);

        int lastIndex = receivedList.size() - 1;
        if (lastIndex > -1)
            lastIdFromDb = receivedList.get(lastIndex).getId();

        TableView<Wypozyczenie> tableView = new TableView<>();

        addButton.setOnAction(e -> {
            int dvd = Integer.parseInt(dvdTextField.getText());
            int client = Integer.parseInt(clientTextField.getText());
            int date = dataTextField.getText();

            Wypozyczenie wypozyczenie = new Wypozyczenie(lastIdFromDb, id_plyta, genre, quantity, cena);
            wypozyczenie.add(wypozyczenia);
            con.sendObject("wypozyczenieAdd", wypozyczenie);

            // Czyść pola tekstowe po dodaniu
            dvdTextField.clear();
            clientTextField.clear();
            dataTextField.clear();
        });

        editButton.setOnAction(e -> {
            int dvd = Integer.parseInt(dvdTextField.getText());
            int client = Integer.parseInt(clientTextField.getText());
            int date = dataTextField.getText();

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
            dvdTextField.clear();
            clientTextField.clear();
            dataTextField.clear();
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
            dvdTextField.clear();
            clientTextField.clear();
            dataTextField.clear();
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
