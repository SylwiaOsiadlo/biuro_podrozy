package com.psk.wypozyczalniaDVD;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;

import java.util.List;

public class KlientForm {

    private final ObservableList<Klient> klienci = FXCollections.observableArrayList();

    private long lastIdFromDb = 0;

    public VBox getContent(ClientConnection con) {
        // Tworzenie etykiet i pól tekstowych
        Label nameLabel = new Label("Imię:");
        TextField nameTextField = new TextField();

        Label surLabel = new Label("Nazwisko:");
        TextField surnameTextField = new TextField();

        Label telLabel = new Label("Nr tel.:");
        TextField telTextField = new TextField();

        Label cityLabel = new Label("Miasto:");
        TextField cityTextField = new TextField();

        Label ulicaLabel = new Label("Ulica:");
        TextField ulicaTextField = new TextField();

        Label nrDomuLabel = new Label("Nr domu:");
        TextField nrDomuTextField = new TextField();

        Label kodLabel = new Label("Kod pocztowy:");
        TextField kodTextField = new TextField();

        Button addButton = new Button("Dodaj");
        Button editButton = new Button("Edytuj");
        Button deleteButton = new Button("Usuń");

        // Tworzenie siatki i dodawanie komponentów
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(5);
        grid.setHgap(10);

        grid.add(nameLabel, 0, 0);
        grid.add(nameTextField, 1, 0);

        grid.add(surLabel, 0, 1);
        grid.add(surnameTextField, 1, 1);

        grid.add(telLabel, 0, 2);
        grid.add(telTextField, 1, 2);

        grid.add(cityLabel, 2, 0);
        grid.add(cityTextField, 3, 0);

        grid.add(ulicaLabel, 2, 1);
        grid.add(ulicaTextField, 3, 1);

        grid.add(nrDomuLabel, 2, 2);
        grid.add(nrDomuTextField, 3, 2);

        grid.add(kodLabel, 2, 3);
        grid.add(kodTextField, 3, 3);

        HBox buttonBox = new HBox();

        grid.add(buttonBox, 0, 7, 2, 1);

        buttonBox.getChildren().add(addButton);
        buttonBox.getChildren().add(editButton);
        buttonBox.getChildren().add(deleteButton);

        List<Klient> receivedList = (List<Klient>) con.requestObject("klienciList");
        klienci.addAll(receivedList);

        int lastIndex = receivedList.size() - 1;
        if (lastIndex > -1)
            lastIdFromDb = receivedList.get(lastIndex).getId();

        TableView<Klient> tableView = new TableView<>();

        addButton.setOnAction(e -> {
            String name = nameTextField.getText();
            String surname = surnameTextField.getText();
            String nrTel = telTextField.getText();
            String city = cityTextField.getText();
            String ulica = ulicaTextField.getText();

            String nrDomu = nrDomuTextField.getText();
            String kod = kodTextField.getText();

            Klient klient = new Klient(lastIdFromDb, name, surname, nrTel, city, ulica, nrDomu, kod);
            klienci.add(klient);
            con.sendObject("klienciAdd", klient);

            // Czyść pola tekstowe po dodaniu
            nameTextField.clear();
            surnameTextField.clear();
            telTextField.clear();
            cityTextField.clear();
            ulicaTextField.clear();
            nrDomuTextField.clear();
            kodTextField.clear();
        });

        editButton.setOnAction(e -> {
            String name = nameTextField.getText();
            String surname = surnameTextField.getText();
            String nrTel = telTextField.getText();
            String city = cityTextField.getText();
            String ulica = ulicaTextField.getText();

            String nrDomu = nrDomuTextField.getText();
            String kod = kodTextField.getText();

            Klient selectedKlient = tableView.getSelectionModel().selectedItemProperty().get();
            long selectedId = selectedKlient.getId();
            Klient klient = new Klient(selectedId, name, surname, nrTel, city, ulica, nrDomu, kod);
            con.sendObject("klienciEdit", klient);

            selectedKlient.setImie(name);
            selectedKlient.setNazwisko(surname);
            selectedKlient.setNr_tel(nrTel);
            selectedKlient.setMiasto(city);

            selectedKlient.setUlica(ulica);
            selectedKlient.setNr_domu(nrDomu);
            selectedKlient.setKod(kod);

            tableView.refresh();

            // Czyść pola tekstowe po dodaniu
            nameTextField.clear();
            surnameTextField.clear();
            telTextField.clear();
            cityTextField.clear();
            ulicaTextField.clear();
            nrDomuTextField.clear();
            kodTextField.clear();
        });

        deleteButton.setOnAction(e -> {
            Klient selectedKlient = tableView.getSelectionModel().selectedItemProperty().get();
            long selectedId = selectedKlient.getId();
            Klient klient = new Klient(selectedId, "", "", ""
                    , "", "", "", "");

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Zapytanie");
            alert.setHeaderText("Czy na pewno chcesz usunąć klienta?");

            ButtonType buttonTypeTak = new ButtonType("Tak");
            ButtonType buttonTypeNie = new ButtonType("Nie");

            alert.getButtonTypes().setAll(buttonTypeTak, buttonTypeNie);

            alert.showAndWait().ifPresent(response -> {
                if (response == buttonTypeTak) {
                    System.out.println("Wybrano Tak");
                    con.sendObject("klienciDel", klient);

                    klienci.remove(selectedKlient);
                    tableView.refresh();
                    alert.close();
                } else if (response == buttonTypeNie) {
                    System.out.println("Wybrano Nie");
                    alert.close();
                }
            });

            // Czyść pola tekstowe po dodaniu
            nameTextField.clear();
            surnameTextField.clear();
            telTextField.clear();
            cityTextField.clear();
            ulicaTextField.clear();
            nrDomuTextField.clear();
            kodTextField.clear();
        });

        // Tworzenie tabeli
        TableColumn<Klient, String> nameColumn = new TableColumn<>("Imię");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("imie"));

        TableColumn<Klient, String> surnameColumn = new TableColumn<>("Nazwisko");
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("nazwisko"));

        TableColumn<Klient, Integer> telColumn = new TableColumn<>("Nr tel.");
        telColumn.setCellValueFactory(new PropertyValueFactory<>("nr_tel"));

        TableColumn<Klient, Integer> cityColumn = new TableColumn<>("Miasto");
        cityColumn.setCellValueFactory(new PropertyValueFactory<>("miasto"));

        TableColumn<Klient, Integer> ulicaColumn = new TableColumn<>("Ulica");
        ulicaColumn.setCellValueFactory(new PropertyValueFactory<>("ulica"));

        TableColumn<Klient, Integer> nrDomuColumn = new TableColumn<>("Nr domu");
        nrDomuColumn.setCellValueFactory(new PropertyValueFactory<>("nr_domu"));

        TableColumn<Klient, Integer> kodColumn = new TableColumn<>("Kod pocztowy");
        kodColumn.setCellValueFactory(new PropertyValueFactory<>("kod"));

        tableView.getColumns().addAll(nameColumn, surnameColumn, telColumn,
                cityColumn, ulicaColumn, nrDomuColumn, kodColumn);
        tableView.setItems(klienci);

        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                nameTextField.setText(newValue.getImie());
                surnameTextField.setText(newValue.getNazwisko());
                telTextField.setText(String.valueOf(newValue.getNr_tel()));
                cityTextField.setText(String.valueOf(newValue.getMiasto()));

                ulicaTextField.setText(String.valueOf(newValue.getUlica()));
                nrDomuTextField.setText(String.valueOf(newValue.getNr_domu()));
                kodTextField.setText(String.valueOf(newValue.getKod()));
            }
        });

        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(10, 10, 10, 10));

        vbox.getChildren().addAll(grid, tableView);
        return vbox;
    }
}
