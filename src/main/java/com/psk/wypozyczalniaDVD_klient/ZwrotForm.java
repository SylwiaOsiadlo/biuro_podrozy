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

public class ZwrotForm {

    private final ObservableList<Zwrot> zwroty = FXCollections.observableArrayList();

    private long lastIdFromDb = 0;

    public VBox getContent(ClientConnection con) {
        // Tworzenie etykiet i pól tekstowych
        Label dvdLabel = new Label("Numer płyty:"); //id płyty
        TextField dvdTextField = new TextField();

        Label clientLabel = new Label("Numer klienta:"); // id klient
        TextField clientTextField = new TextField();

        Label activeLabel = new Label("Status:");
        TextField activeTextField = new TextField();

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

        grid.add(activeLabel, 0, 2);
        grid.add(activeTextField, 1, 2);

        HBox buttonBox = new HBox();

        grid.add(buttonBox, 0, 4, 2, 1);

        buttonBox.getChildren().add(addButton);
        buttonBox.getChildren().add(editButton);
        buttonBox.getChildren().add(deleteButton);

        List<Zwrot> receivedList = (List<Zwrot>) con.requestObject("zwrotList");
        zwroty.addAll(receivedList);

        int lastIndex = receivedList.size() - 1;
        if (lastIndex > -1)
            lastIdFromDb = receivedList.get(lastIndex).getId();

        TableView<Zwrot> tableView = new TableView<>();

        addButton.setOnAction(e -> {
            int dvd = Integer.parseInt(dvdTextField.getText());
            int client = Integer.parseInt(clientTextField.getText());
            String active = activeTextField.getText();

            Zwrot zwrot = new Zwrot(lastIdFromDb, dvd, client, active);
            zwroty.add(zwrot);
            con.sendObject("zwrotAdd", zwrot);

            // Czyść pola tekstowe po dodaniu
            dvdTextField.clear();
            clientTextField.clear();
            activeTextField.clear();
        });

        editButton.setOnAction(e -> {
            int dvd = Integer.parseInt(dvdTextField.getText());
            int client = Integer.parseInt(clientTextField.getText());
            String active = activeTextField.getText();

            Zwrot selectedZwrot = tableView.getSelectionModel().getSelectedItem();
            if (selectedZwrot != null) {
                long selectedId = selectedZwrot.getId();
                Zwrot zwrot = new Zwrot(selectedId, dvd, client, active);

                selectedZwrot.setId_plyta(dvd);
                selectedZwrot.setId_klient(client);
                selectedZwrot.setActive(active);
                tableView.refresh();

                con.sendObject("zwrotEdit", zwrot);
            }

            // Czyść pola tekstowe po dodaniu
            dvdTextField.clear();
            clientTextField.clear();
            activeTextField.clear();
        });

        deleteButton.setOnAction(e -> {
            Zwrot selectedZwrot = tableView.getSelectionModel().getSelectedItem();
            if (selectedZwrot != null) {
                long selectedId = selectedZwrot.getId();
                Zwrot zwrot = new Zwrot(selectedId, 0, 0, "");

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Zapytanie");
                alert.setHeaderText("Czy na pewno chcesz usunąć ten zwrot?");

                ButtonType buttonTypeTak = new ButtonType("Tak");
                ButtonType buttonTypeNie = new ButtonType("Nie");

                alert.getButtonTypes().setAll(buttonTypeTak, buttonTypeNie);

                alert.showAndWait().ifPresent(response -> {
                    if (response == buttonTypeTak) {
                        System.out.println("Wybrano Tak");
                        con.sendObject("zwrotDel", zwrot);

                        zwroty.remove(selectedZwrot);
                        tableView.refresh();
                        alert.close();
                    } else if (response == buttonTypeNie) {
                        System.out.println("Wybrano Nie");
                        alert.close();
                    }
                });
            }

            // Czyść pola tekstowe po dodaniu
            dvdTextField.clear();
            clientTextField.clear();
            activeTextField.clear();
        });

        // Tworzenie tabeli
        TableColumn<Zwrot, Long> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Zwrot, Integer> dvdColumn = new TableColumn<>("Numer płyty");
        dvdColumn.setCellValueFactory(new PropertyValueFactory<>("id_plyta"));

        TableColumn<Zwrot, Integer> clientColumn = new TableColumn<>("Numer klienta");
        clientColumn.setCellValueFactory(new PropertyValueFactory<>("id_klient"));

        TableColumn<Zwrot, String> activeColumn = new TableColumn<>("Status");
        activeColumn.setCellValueFactory(new PropertyValueFactory<>("active"));

        tableView.getColumns().addAll(idColumn, dvdColumn, clientColumn, activeColumn);
        tableView.setItems(zwroty);

        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                dvdTextField.setText(String.valueOf(newValue.getId_plyta()));
                clientTextField.setText(String.valueOf(newValue.getId_klient()));
                activeTextField.setText(newValue.getActive());
            }
        });

        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(10, 10, 10, 10));

        vbox.getChildren().addAll(grid, tableView);
        return vbox;
    }
}
