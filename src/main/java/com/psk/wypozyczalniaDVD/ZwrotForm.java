package com.psk.wypozyczalniaDVD;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javafx.geometry.Insets;

import java.time.LocalDateTime;
import java.util.List;

public class ZwrotForm {

    private final ObservableList<WypozyczenieView> wypozyczenia = FXCollections.observableArrayList();
    private final ObservableList<ZwrotView> zwroty = FXCollections.observableArrayList();

    private long lastIdFromDb = 0;

    public VBox getContent(ClientConnection con) {
        // Tworzenie etykiet i pól tekstowych
        Label wypozyczeniaLabel = new Label("Lista wypożyczeń:");
        TableView<WypozyczenieView> tableWypozyczeniaView = new TableView<>();
        Label zwroconeLabel = new Label("Lista zwróconych:");
        TableView<ZwrotView> tableZwrotyView = new TableView<>();

        Button addButton = new Button("Zwróć");
        HBox buttonBox = new HBox();

        buttonBox.getChildren().add(addButton);

        //List<Zwrot> receivedList = (List<Zwrot>) con.requestObject("zwrotList");
        List<ZwrotView> receivedZwrotyList = (List<ZwrotView>) con.requestObject("zwrotViewList");
        //List<ZwrotView> receivedZwrotyList = new ArrayList<>();
        //List<WypozyczenieView> receivedWypozyczeniaList = new ArrayList<>();
        List<WypozyczenieView> receivedWypozyczeniaList = (List<WypozyczenieView>) con.requestObject("wypozyczenieViewList");
        zwroty.addAll(receivedZwrotyList);
        wypozyczenia.addAll(receivedWypozyczeniaList);

        int lastIndex = zwroty.size() - 1;
        if (lastIndex > -1)
            lastIdFromDb = zwroty.get(lastIndex).getId();

        addButton.setOnAction(e -> {
            WypozyczenieView selectedWypozyczenie = tableWypozyczeniaView.getSelectionModel().selectedItemProperty().get();

            if (selectedWypozyczenie != null) {
                long selectedId = selectedWypozyczenie.getId();

                //zwroty.add(zwrot);
                con.sendObject("zwrot", selectedId);

                List<ZwrotView> receivedZwrotyListNew = (List<ZwrotView>) con.requestObject("zwrotViewList");
                //List<ZwrotView> receivedZwrotyList = new ArrayList<>();
                //List<WypozyczenieView> receivedWypozyczeniaList = new ArrayList<>();
                List<WypozyczenieView> receivedWypozyczeniaListNew = (List<WypozyczenieView>) con.requestObject("wypozyczenieViewList");
                zwroty.clear();
                zwroty.addAll(receivedZwrotyListNew);
                wypozyczenia.clear();
                wypozyczenia.addAll(receivedWypozyczeniaListNew);
            }
            else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Operacja niedozwolona");
                alert.setHeaderText("Wybierz wypożyczoną płytę do zwrotu z listy.");
                alert.show();
            }
        });

        // Tworzenie tabeli wypożyczonych
        TableColumn<WypozyczenieView, String> nameDvdColumn = new TableColumn<>("Nazwa płyty");
        nameDvdColumn.setCellValueFactory(new PropertyValueFactory<>("nazwaDVD"));

        TableColumn<WypozyczenieView, String> gatunekDVDColumn = new TableColumn<>("Gatunek");
        gatunekDVDColumn.setCellValueFactory(new PropertyValueFactory<>("gatunekDVD"));

        TableColumn<WypozyczenieView, Integer> iloscDVDColumn = new TableColumn<>("Ilość sztuk ogółem");
        iloscDVDColumn.setCellValueFactory(new PropertyValueFactory<>("iloscSztuk"));

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

        tableWypozyczeniaView.getColumns().addAll(nameDvdColumn, gatunekDVDColumn, iloscDVDColumn, imieColumn, nazwiskoColumn, telColumn, dataColumn, zwrotColumn);
        tableWypozyczeniaView.setItems(wypozyczenia);

        // Tworzenie tabeli zwróconych
        TableColumn<ZwrotView, String> nameDvdColumnZwroty = new TableColumn<>("Nazwa płyty");
        nameDvdColumnZwroty.setCellValueFactory(new PropertyValueFactory<>("nazwaDVD"));

        TableColumn<ZwrotView, String> gatunekDVDColumnZwroty = new TableColumn<>("Gatunek");
        gatunekDVDColumnZwroty.setCellValueFactory(new PropertyValueFactory<>("gatunekDVD"));

        TableColumn<ZwrotView, Float> cenaDVDColumnZwroty = new TableColumn<>("Cena [PLN]");
        cenaDVDColumnZwroty.setCellValueFactory(new PropertyValueFactory<>("cena"));

        TableColumn<ZwrotView, Integer> iloscDVDColumnZwroty = new TableColumn<>("Ilość sztuk ogółem");
        iloscDVDColumnZwroty.setCellValueFactory(new PropertyValueFactory<>("iloscSztuk"));

        TableColumn<ZwrotView, String> imieColumnZwroty = new TableColumn<>("Imię");
        imieColumnZwroty.setCellValueFactory(new PropertyValueFactory<>("imieKlienta"));

        TableColumn<ZwrotView, String> nazwiskoColumnZwroty = new TableColumn<>("Nazwisko");
        nazwiskoColumnZwroty.setCellValueFactory(new PropertyValueFactory<>("nazwiskoKlienta"));

        TableColumn<ZwrotView, String> telColumnZwroty = new TableColumn<>("Nr tel.");
        telColumnZwroty.setCellValueFactory(new PropertyValueFactory<>("nrTelKlienta"));

        TableColumn<ZwrotView, LocalDateTime> dataColumnZwroty = new TableColumn<>("Data wypożyczenia");
        dataColumnZwroty.setCellValueFactory(new PropertyValueFactory<>("data_w"));

        TableColumn<ZwrotView, LocalDateTime> zwrotPlanowanyColumnZwroty = new TableColumn<>("Planowana data zwrotu");
        zwrotPlanowanyColumnZwroty.setCellValueFactory(new PropertyValueFactory<>("data_z_planowana"));

        TableColumn<ZwrotView, LocalDateTime> zwrotColumnZwroty = new TableColumn<>("Data zwrotu");
        zwrotColumnZwroty.setCellValueFactory(new PropertyValueFactory<>("data_z"));

        tableZwrotyView.getColumns().addAll(nameDvdColumnZwroty, gatunekDVDColumnZwroty, cenaDVDColumnZwroty, iloscDVDColumnZwroty, imieColumnZwroty, nazwiskoColumnZwroty, telColumnZwroty, dataColumnZwroty, zwrotPlanowanyColumnZwroty, zwrotColumnZwroty);
        tableZwrotyView.setItems(zwroty);

        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(10, 10, 10, 10));

        vbox.getChildren().addAll(wypozyczeniaLabel, tableWypozyczeniaView, buttonBox, zwroconeLabel, tableZwrotyView);
        return vbox;
    }
}
