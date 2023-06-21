package com.psk.wypozyczalniaDVD_klient;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;

import java.time.LocalDateTime;
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

        Label zwrotLabel = new Label("Data zwrotu planowana:");
        TextField zwrotTextField = new TextField();

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

        grid.add(dataLabel, 0, 2);
        grid.add(dataTextField, 1, 2);

        grid.add(zwrotLabel, 0, 3);
        grid.add(zwrotTextField, 1, 3);

        HBox buttonBox = new HBox();

        grid.add(buttonBox, 0, 5, 2, 1);

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
            LocalDateTime dataWypozyczenia = LocalDateTime.parse(dataTextField.getText());
            LocalDateTime dataZwrotuPlanowana = LocalDateTime.parse(zwrotTextField.getText());

            Wypozyczenie wypozyczenie = new Wypozyczenie(lastIdFromDb, dvd, client, dataWypozyczenia, dataZwrotuPlanowana);
            wypozyczenia.add(wypozyczenie);
            con.sendObject("wypozyczenieAdd", wypozyczenie);

            // Czyść pola tekstowe po dodaniu
            dvdTextField.clear();
            clientTextField.clear();
            dataTextField.clear();
            zwrotTextField.clear();
        });

        editButton.setOnAction(e -> {
            int dvd = Integer.parseInt(dvdTextField.getText());
            int client = Integer.parseInt(clientTextField.getText());
            LocalDateTime dataWypozyczenia = LocalDateTime.parse(dataTextField.getText());
            LocalDateTime dataZwrotuPlanowana = LocalDateTime.parse(zwrotTextField.getText());

            Wypozyczenie selectedWypozyczenie = tableView.getSelectionModel().getSelectedItem();
            if (selectedWypozyczenie != null) {
                long selectedId = selectedWypozyczenie.getId();
                Wypozyczenie wypozyczenie = new Wypozyczenie(selectedId, dvd, client, dataWypozyczenia, dataZwrotuPlanowana);

                selectedWypozyczenie.setId_plyta(dvd);
                selectedWypozyczenie.setId_klient(client);
                selectedWypozyczenie.setData_w(dataWypozyczenia);
                selectedWypozyczenie.setData_z(dataZwrotuPlanowana);
                tableView.refresh();

                con.sendObject("wypozyczenieEdit", wypozyczenie);
            }

            // Czyść pola tekstowe po edycji
            dvdTextField.clear();
            clientTextField.clear();
            dataTextField.clear();
            zwrotTextField.clear();
        });

        deleteButton.setOnAction(e -> {
            Wypozyczenie selectedWypozyczenie = tableView.getSelectionModel().getSelectedItem();
            if (selectedWypozyczenie != null) {
                long selectedId = selectedWypozyczenie.getId();
                Wypozyczenie wypozyczenie = new Wypozyczenie(selectedId, 0, 0, null, null);

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Zapytanie");
                alert.setHeaderText("Czy na pewno chcesz usunąć to wypożyczenie?");

                ButtonType buttonTypeTak = new ButtonType("Tak");
                ButtonType buttonTypeNie = new ButtonType("Nie");

                alert.getButtonTypes().setAll(buttonTypeTak, buttonTypeNie);

                alert.showAndWait().ifPresent(response -> {
                    if (response == buttonTypeTak) {
                        System.out.println("Wybrano Tak");
                        con.sendObject("wypozyczenieDel", wypozyczenie);

                        wypozyczenia.remove(selectedWypozyczenie);
                        tableView.refresh();
                        alert.close();
                    } else if (response == buttonTypeNie) {
                        System.out.println("Wybrano Nie");
                        alert.close();
                    }
                });
            }

            // Czyść pola tekstowe po usunięciu
            dvdTextField.clear();
            clientTextField.clear();
            dataTextField.clear();
            zwrotTextField.clear();
        });

        // Tworzenie tabeli
        TableColumn<Wypozyczenie, Long> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Wypozyczenie, Integer> dvdColumn = new TableColumn<>("Numer płyty");
        dvdColumn.setCellValueFactory(new PropertyValueFactory<>("id_plyta"));

        TableColumn<Wypozyczenie, Integer> clientColumn = new TableColumn<>("Numer klienta");
        clientColumn.setCellValueFactory(new PropertyValueFactory<>("id_klient"));

        TableColumn<Wypozyczenie, LocalDateTime> dataColumn = new TableColumn<>("Data wypożyczenia");
        dataColumn.setCellValueFactory(new PropertyValueFactory<>("data_w"));

        TableColumn<Wypozyczenie, LocalDateTime> zwrotColumn = new TableColumn<>("Data zwrotu planowana");
        zwrotColumn.setCellValueFactory(new PropertyValueFactory<>("data_z"));

        tableView.getColumns().addAll(idColumn, dvdColumn, clientColumn, dataColumn, zwrotColumn);
        tableView.setItems(wypozyczenia);

        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                dvdTextField.setText(String.valueOf(newValue.getId_plyta()));
                clientTextField.setText(String.valueOf(newValue.getId_klient()));
                dataTextField.setText(newValue.getData_w().toString());
                zwrotTextField.setText(newValue.getData_z().toString());
            }
        });

        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(10, 10, 10, 10));

        vbox.getChildren().addAll(grid, tableView);
        return vbox;
    }
}
