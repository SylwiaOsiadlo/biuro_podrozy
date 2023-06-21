package com.psk.wypozyczalniaDVD_klient;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.DatePicker;

import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class WypozyczenieForm {

    private final ObservableList<WypozyczenieView> wypozyczenia = FXCollections.observableArrayList();

    private long lastIdFromDb = 0;

    public VBox getContent(ClientConnection con) {
        // Tworzenie etykiet i pól tekstowych
        Label dvdLabel = new Label("Numer płyty:"); //id płyty
        TextField dvdTextField = new TextField();

        Label clientLabel = new Label("Numer klienta:"); // id klient
        TextField clientTextField = new TextField();

        Label dataLabel = new Label("Data wypożyczenia:");
        DatePicker datePicker = new DatePicker();
        datePicker.setValue(LocalDate.now());

        TextField dataTextField = new TextField();

        Label zwrotLabel = new Label("Data zwrotu planowana:");

        DatePicker zwrotDatePicker = new DatePicker();
        zwrotDatePicker.setValue(LocalDate.now());

        TextField zwrotTextField = new TextField();

        Button addButton = new Button("Wypożycz");
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
        grid.add(datePicker, 1, 2);

        grid.add(zwrotLabel, 0, 3);
        grid.add(zwrotDatePicker, 1, 3);

        HBox buttonBox = new HBox();

        grid.add(buttonBox, 0, 5, 2, 1);

        buttonBox.getChildren().add(addButton);
        buttonBox.getChildren().add(editButton);
        buttonBox.getChildren().add(deleteButton);

        List<WypozyczenieView> receivedList = (List<WypozyczenieView>) con.requestObject("wypozyczenieList");
        //List<Wypozyczenie> receivedList = new ArrayList<>();

        wypozyczenia.addAll(receivedList);

        int lastIndex = receivedList.size() - 1;
        if (lastIndex > -1)
            lastIdFromDb = receivedList.get(lastIndex).getId();

        TableView<WypozyczenieView> tableView = new TableView<>();

        addButton.setOnAction(e -> {
            int dvd = Integer.parseInt(dvdTextField.getText());
            int client = Integer.parseInt(clientTextField.getText());
            LocalDate dataWypozyczenia = datePicker.getValue();
            LocalDate dataZwrotuPlanowana = datePicker.getValue();

            Wypozyczenie wypozyczenie = new Wypozyczenie(lastIdFromDb, dvd, client, dataWypozyczenia, dataZwrotuPlanowana);
            //wypozyczenia.add(wypozyczenie);
            con.sendObject("wypozyczenieAdd", wypozyczenie);

            List<WypozyczenieView> receivedListAdd = (List<WypozyczenieView>) con.requestObject("wypozyczenieList");

            wypozyczenia.clear();
            wypozyczenia.addAll(receivedListAdd);

            // Czyść pola tekstowe po dodaniu
            dvdTextField.clear();
            clientTextField.clear();
            dataTextField.clear();
            zwrotTextField.clear();
        });

        editButton.setOnAction(e -> {
            int dvd = Integer.parseInt(dvdTextField.getText());
            int client = Integer.parseInt(clientTextField.getText());
            LocalDate dataWypozyczenia = LocalDate.parse(dataTextField.getText());
            LocalDate dataZwrotuPlanowana = LocalDate.parse(zwrotTextField.getText());

            //Wypozyczenie selectedWypozyczenie = tableView.getSelectionModel().getSelectedItem();
/*            if (selectedWypozyczenie != null) {
                long selectedId = selectedWypozyczenie.getId();
                Wypozyczenie wypozyczenie = new Wypozyczenie(selectedId, dvd, client, dataWypozyczenia, dataZwrotuPlanowana);

                selectedWypozyczenie.setId_plyta(dvd);
                selectedWypozyczenie.setId_klient(client);
                selectedWypozyczenie.setData_w(dataWypozyczenia);
                selectedWypozyczenie.setData_z(dataZwrotuPlanowana);
                tableView.refresh();

                con.sendObject("wypozyczenieEdit", wypozyczenie);
            }*/

            // Czyść pola tekstowe po edycji
            dvdTextField.clear();
            clientTextField.clear();
            dataTextField.clear();
            zwrotTextField.clear();
        });

        deleteButton.setOnAction(e -> {
            /*Wypozyczenie selectedWypozyczenie = tableView.getSelectionModel().getSelectedItem();
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
            }*/

            // Czyść pola tekstowe po usunięciu
            dvdTextField.clear();
            clientTextField.clear();
            dataTextField.clear();
            zwrotTextField.clear();
        });

        // Tworzenie tabeli
        TableColumn<WypozyczenieView, String> nameDvdColumn = new TableColumn<>("Nazwa płyty");
        nameDvdColumn.setCellValueFactory(new PropertyValueFactory<>("nazwaDVD"));

        TableColumn<WypozyczenieView, String> gatunekDVDColumn = new TableColumn<>("Gatunek");
        gatunekDVDColumn.setCellValueFactory(new PropertyValueFactory<>("gatunekDVD"));

        TableColumn<WypozyczenieView, String> imieColumn = new TableColumn<>("Imię");
        imieColumn.setCellValueFactory(new PropertyValueFactory<>("imieKlienta"));

        TableColumn<WypozyczenieView, String> nazwiskoColumn = new TableColumn<>("Nazwisko");
        nazwiskoColumn.setCellValueFactory(new PropertyValueFactory<>("nazwiskoKlienta"));

        TableColumn<WypozyczenieView, String> telColumn = new TableColumn<>("Nr tel.");
        telColumn.setCellValueFactory(new PropertyValueFactory<>("nrTelKlienta"));

        TableColumn<WypozyczenieView, LocalDateTime> dataColumn = new TableColumn<>("Data wypożyczenia");
        dataColumn.setCellValueFactory(new PropertyValueFactory<>("data_w"));

        TableColumn<WypozyczenieView, LocalDateTime> zwrotColumn = new TableColumn<>("Planowana data zwrotu");
        zwrotColumn.setCellValueFactory(new PropertyValueFactory<>("data_z"));

        tableView.getColumns().addAll(nameDvdColumn, gatunekDVDColumn, imieColumn, nazwiskoColumn, telColumn, dataColumn, zwrotColumn);
        tableView.setItems(wypozyczenia);

        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                //dvdTextField.setText(String.valueOf(newValue.getId_plyta()));
                //clientTextField.setText(String.valueOf(newValue.getId_klient()));
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
