package com.psk.wypozyczalniaDVD;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.control.DatePicker;

import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

public class WypozyczenieForm {

    private final ObservableList<WypozyczenieView> wypozyczenia = FXCollections.observableArrayList();
    private final ObservableList<Album> albumy = FXCollections.observableArrayList();
    private final ObservableList<Klient> klienci = FXCollections.observableArrayList();

    private String validationInfo = "";
    private boolean validForm = true;

    private long lastIdFromDb = 0;

    public VBox getContent(ClientConnection con) {
        // Tworzenie etykiet i pól tekstowych
        Label dvdLabel = new Label("Wybierz płytę:"); //id płyty
        TextField dvdTextField = new TextField();

        Label clientLabel = new Label("Wybierz klienta:"); // id klient
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

        // Tworzenie siatki i dodawanie komponentów
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(5);
        grid.setHgap(5);

        TableView<Album> tableAlbumView = new TableView<>();
        TableView<Klient> tableKlientView = new TableView<>();

        HBox tabBox = new HBox();
        VBox tabFirstBox = new VBox();
        VBox tabSecondBox = new VBox();

        tabBox.setSpacing(10);
        HBox.setHgrow(tabBox, Priority.ALWAYS);
        tabFirstBox.getChildren().add(dvdLabel);
        tabFirstBox.getChildren().add(tableAlbumView);

        tabSecondBox.getChildren().add(clientLabel);
        tabSecondBox.getChildren().add(tableKlientView);

        tabBox.getChildren().addAll(tabFirstBox, tabSecondBox);

        grid.add(dataLabel, 0, 2);
        grid.add(datePicker, 1, 2);

        grid.add(zwrotLabel, 0, 3);
        grid.add(zwrotDatePicker, 1, 3);

        HBox buttonBox = new HBox();

        grid.add(buttonBox, 0, 5, 2, 1);

        buttonBox.getChildren().add(addButton);

        List<Album> receivedAlbumList = (List<Album>) con.requestObject("plytyList");
        List<Klient> receivedKlientList = (List<Klient>) con.requestObject("klienciList");

        List<WypozyczenieView> receivedViewList = (List<WypozyczenieView>) con.requestObject("wypozyczenieViewList");
        List<Wypozyczenie> receivedList = (List<Wypozyczenie>) con.requestObject("wypozyczenieList");
        //List<Wypozyczenie> receivedList = new ArrayList<>();

        albumy.addAll(receivedAlbumList);
        klienci.addAll(receivedKlientList);
        wypozyczenia.addAll(receivedViewList);

        int lastIndex = receivedViewList.size() - 1;
        if (lastIndex > -1)
            lastIdFromDb = receivedViewList.get(lastIndex).getId();

        TableView<WypozyczenieView> tableView = new TableView<>();

        addButton.setOnAction(e -> {
            validForm = true;
            validationInfo = "";

            Album selectedAlbum = tableAlbumView.getSelectionModel().selectedItemProperty().get();
            Klient selectedKlient = tableKlientView.getSelectionModel().selectedItemProperty().get();

            LocalDate dataWypozyczenia = datePicker.getValue();
            LocalDate dataZwrotuPlanowana = zwrotDatePicker.getValue();

            String dateInfo = "";
            String zwrotDataInfo = "";

            if (dataWypozyczenia == null) {
                dateInfo += "Podaj właściwy format daty wypożyczenia.\n";
                validForm = false;
            }

            if (dataZwrotuPlanowana == null) {
                validationInfo += "Podaj właściwy format daty planowanego zwrotu.\n";
                validForm = false;
            }

            if (selectedAlbum != null && selectedKlient != null && validForm) {
                long dvd = selectedAlbum.getId();
                long client = selectedKlient.getId();

                int iloscWypozyczen = 0;
                int iloscSztuk = 0;
                boolean correctIlosc = true;
                for (Wypozyczenie element : receivedList) {
                    if (element.getId_plyta() == dvd) {
                        iloscWypozyczen++;
                    }
                }
                for (WypozyczenieView element : receivedViewList) {
                    if (element.getIdDVD() == dvd) {
                        iloscSztuk = element.getIloscSztuk();
                        break;
                    }
                }

                boolean correctDate = true;
                if (dataZwrotuPlanowana.isBefore(dataWypozyczenia)) {
                    correctDate = false;
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Operacja niedozwolona");
                    alert.setHeaderText("Planowana data oddania nie może być wcześniejsza niż data wypożyczenia.");
                    alert.show();
                }

                if (iloscSztuk < iloscWypozyczen) {
                    correctIlosc = false;
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Operacja niedozwolona");
                    alert.setHeaderText("Wybrana płyta DVD nie jest dostępna do wypożyczenia.");
                    alert.show();
                }

                if (correctIlosc && correctDate) {
                    Wypozyczenie wypozyczenie = new Wypozyczenie(lastIdFromDb, dvd, client, dataWypozyczenia, dataZwrotuPlanowana);
                    //wypozyczenia.add(wypozyczenie);
                    con.sendObject("wypozyczenieAdd", wypozyczenie);

                    List<WypozyczenieView> receivedListAdd = (List<WypozyczenieView>) con.requestObject("wypozyczenieViewList");

                    wypozyczenia.clear();
                    wypozyczenia.addAll(receivedListAdd);

                    // Czyść pola tekstowe po dodaniu
                    dvdTextField.clear();
                    clientTextField.clear();
                    dataTextField.clear();
                    zwrotTextField.clear();
                }
            }
            else if ((selectedAlbum != null || selectedKlient != null) && validForm) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Operacja niedozwolona");
                alert.setHeaderText("Wybierz płytę DVD do wypożyczenia z listy oraz wybierz klienta wypożyczającego.");
                alert.show();
            }
            else if (!validForm) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Operacja niedozwolona");
                alert.setHeaderText("Proszę podać właściwy format daty.");
                alert.show();
            }
        });

        // Tworzenie tabeli albumy
        TableColumn<Album, String> albumNameColumn = new TableColumn<>("Nazwa płyty");
        albumNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Album, String> albumGenreColumn = new TableColumn<>("Gatunek");
        albumGenreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));

        TableColumn<Album, Integer> quantityColumn = new TableColumn<>("Ilość sztuk");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        TableColumn<Album, Integer> cenaColumn = new TableColumn<>("Cena");
        cenaColumn.setCellValueFactory(new PropertyValueFactory<>("cena"));

        tableAlbumView.getColumns().addAll(albumNameColumn, albumGenreColumn, quantityColumn, cenaColumn);
        tableAlbumView.setItems(albumy);

        // Tworzenie tabeli klient
        TableColumn<Klient, String> customerNameColumn = new TableColumn<>("Imię");
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("imie"));

        TableColumn<Klient, String> customerSurnameColumn = new TableColumn<>("Nazwisko");
        customerSurnameColumn.setCellValueFactory(new PropertyValueFactory<>("nazwisko"));

        TableColumn<Klient, Integer> customerTelColumn = new TableColumn<>("Nr tel.");
        customerTelColumn.setCellValueFactory(new PropertyValueFactory<>("nr_tel"));

        TableColumn<Klient, Integer> cityColumn = new TableColumn<>("Miasto");
        cityColumn.setCellValueFactory(new PropertyValueFactory<>("miasto"));

        TableColumn<Klient, Integer> ulicaColumn = new TableColumn<>("Ulica");
        ulicaColumn.setCellValueFactory(new PropertyValueFactory<>("ulica"));

        TableColumn<Klient, Integer> nrDomuColumn = new TableColumn<>("Nr domu");
        nrDomuColumn.setCellValueFactory(new PropertyValueFactory<>("nr_domu"));

        TableColumn<Klient, Integer> kodColumn = new TableColumn<>("Kod pocztowy");
        kodColumn.setCellValueFactory(new PropertyValueFactory<>("kod"));

        tableKlientView.getColumns().addAll(customerNameColumn, customerSurnameColumn, customerTelColumn,
                cityColumn, ulicaColumn, nrDomuColumn, kodColumn);
        tableKlientView.setItems(klienci);

        // Tworzenie tabeli wypożyczenie
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

        tableView.getColumns().addAll(nameDvdColumn, gatunekDVDColumn, iloscDVDColumn, imieColumn, nazwiskoColumn, telColumn, dataColumn, zwrotColumn);
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

        vbox.getChildren().addAll(tabBox, grid, tableView);
        return vbox;
    }
}
