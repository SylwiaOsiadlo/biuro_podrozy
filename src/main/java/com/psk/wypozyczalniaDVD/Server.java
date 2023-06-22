package com.psk.wypozyczalniaDVD;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Server {

    private static final Logger logger = LogManager.getLogger(Server.class);

    private static final int THREAD_POOL_SIZE = 10; // Rozmiar puli wątków
    private static boolean isRunning = true; // Flaga informująca o działaniu serwera

    public static void main(String[] args) {
        int port = 8080; // Port, na którym serwer będzie nasłuchiwał

        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Serwer uruchomiony na porcie " + port);

            // Utworzenie puli wątków
            ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

            while (isRunning) {
                Socket clientSocket = serverSocket.accept();
                logger.info("Połączenie przychodzące z adresu: " + clientSocket.getInetAddress());

                // Przekazanie połączenia do wątku w puli
                executorService.execute(new RequestHandler(clientSocket));
            }

            // Zamknięcie serwera po zakończeniu działania
            serverSocket.close();
            executorService.shutdown();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    static class RequestHandler implements Runnable {
        private final Socket clientSocket;

        public RequestHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        private DatabaseConnection dbCon;

        private boolean isClosed = false;

        @Override
        public void run() {

            try {
                dbCon = new DatabaseConnection();
                dbCon.connect();

                long bytesToSkip = clientSocket.getInputStream().available();

                // Pomiń bajty w strumieniu
                long bytesSkipped = clientSocket.getInputStream().skip(bytesToSkip);

                while (!isClosed) {
                    handleRequest(clientSocket);
                }

                dbCon.closeDbConnect();
                logger.info("Rozlaczono klienta: " + clientSocket.getInetAddress());
            } catch (IOException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
            } catch (ClassNotFoundException e) {
                logger.error(e.getMessage());
                throw new RuntimeException(e);
            }
        }

        private void handleRequest(Socket clientSocket) throws IOException, ClassNotFoundException {
            DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
            String request = dis.readUTF();

            System.out.println("Odebrano żądanie: " + request);

            if (request.contains("plytyList")) {
                // Tworzenie listy do wysłania klientowi
                List<Album> list = new ArrayList<>();

                try {
                    Statement statement = dbCon.getConnection().createStatement();
                    ResultSet resultSet = statement.executeQuery("SELECT * FROM album;");

                    while (resultSet.next()) {
                        long id = resultSet.getLong("id");
                        String name = resultSet.getString("name");
                        String genre = resultSet.getString("genre");
                        int quantity = resultSet.getInt("quantity");
                        int cena = resultSet.getInt("cena");

                        Album album = new Album(id, name, genre, quantity, 0, cena);
                        list.add(album);
                    }
                }catch (SQLException e) {
                    logger.error("Błąd zapytania SQL dla: plytyList.");
                    throw new RuntimeException(e);
                }

                ObjectOutputStream objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                objectOutputStream.writeObject(list);
                objectOutputStream.flush();
            }
            else if (request.contains("plytyAdd")) {

                try {
                    ObjectInputStream objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
                    Album newAlbum = (Album) objectInputStream.readObject();
                    String insertQuery = "INSERT INTO album (name, genre, quantity, cena) VALUES (?, ?, ?, ?)";
                    PreparedStatement statement = dbCon.getConnection().prepareStatement(insertQuery);
                    statement.setString(1, newAlbum.getName());
                    statement.setString(2, newAlbum.getGenre());
                    statement.setInt(3, newAlbum.getQuantity());
                    statement.setFloat(4, newAlbum.getCena());

                    int rowsInserted = statement.executeUpdate();
                    if (rowsInserted > 0) {
                        System.out.println("Album został pomyślnie dodany do bazy danych.");
                    }
                    else {
                        logger.warn("Nie udało się dodać Albumu do bazy danych.");
                    }
                }catch (SQLException | ClassNotFoundException e) {
                    logger.error(e.getMessage());
                }
            }
            else if (request.contains("plytyEdit")) {

                try {
                    ObjectInputStream objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
                    Album editAlbum = (Album) objectInputStream.readObject();
                    String updateQuery = "UPDATE album SET name = ?, genre = ?, quantity = ?, cena = ? WHERE id = ?";
                    PreparedStatement statement = dbCon.getConnection().prepareStatement(updateQuery);
                    statement.setString(1, editAlbum.getName());
                    statement.setString(2, editAlbum.getGenre());
                    statement.setInt(3, editAlbum.getQuantity());
                    statement.setFloat(4, editAlbum.getCena());
                    statement.setLong(5, editAlbum.getId());

                    int rowsUpdated = statement.executeUpdate();
                    if (rowsUpdated > 0) {
                        System.out.println("Album został pomyślnie zaktualizowany w bazie danych.");
                    } else {
                        logger.warn("Nie znaleziono albumu o podanym identyfikatorze.");
                    }
                }catch (SQLException | ClassNotFoundException e) {
                    logger.error(e.getMessage());
                }
            }
            else if (request.contains("plytyDel")) {
                try {
                    ObjectInputStream objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
                    Album editAlbum = (Album) objectInputStream.readObject();
                    String updateQuery = "DELETE FROM album WHERE id = ?";
                    PreparedStatement statement = dbCon.getConnection().prepareStatement(updateQuery);
                    statement.setLong(1, editAlbum.getId());

                    int rowsUpdated = statement.executeUpdate();
                    if (rowsUpdated > 0) {
                        System.out.println("Album został pomyślnie uzunięty z bazy danych.");
                    } else {
                        logger.warn("Nie znaleziono albumu o podanym identyfikatorze.");
                    }
                }catch (SQLException | ClassNotFoundException e) {
                    logger.error(e.getMessage());
                }
            }
            else if (request.contains("klienciList")) {
                // Tworzenie listy
                List<Klient> list = new ArrayList<>();

                try {
                    Statement statement = dbCon.getConnection().createStatement();
                    ResultSet resultSet = statement.executeQuery("SELECT * FROM osoba;");

                    while (resultSet.next()) {
                        long id = resultSet.getLong("id");
                        String name = resultSet.getString("imie");
                        String surname = resultSet.getString("nazwisko");
                        String nrTel = resultSet.getString("nr_tel");
                        String city = resultSet.getString("miasto");
                        String ulica = resultSet.getString("ulica");
                        String nrDomu = resultSet.getString("nr_domu");
                        String kod = resultSet.getString("kod_pocztowy");

                        Klient klient = new Klient(id, name, surname, nrTel, city, ulica, nrDomu, kod);
                        list.add(klient);
                    }
                }catch (SQLException e) {
                    logger.error(e.getMessage());
                    throw new RuntimeException(e);
                }

                ObjectOutputStream objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                objectOutputStream.writeObject(list);
                objectOutputStream.flush();
            }
            else if (request.contains("klienciAdd")) {

                try {
                    ObjectInputStream objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
                    Klient newKlient = (Klient) objectInputStream.readObject();
                    String insertQuery = "INSERT INTO osoba (imie, nazwisko, nr_tel, miasto, ulica, nr_domu, kod_pocztowy) VALUES (?, ?, ?, ?, ?, ?, ?)";
                    PreparedStatement statement = dbCon.getConnection().prepareStatement(insertQuery);
                    statement.setString(1, newKlient.getImie());
                    statement.setString(2, newKlient.getNazwisko());
                    statement.setString(3, newKlient.getNr_tel());
                    statement.setString(4, newKlient.getMiasto());
                    statement.setString(5, newKlient.getUlica());
                    statement.setString(6, newKlient.getNr_domu());
                    statement.setString(7, newKlient.getKod());

                    int rowsInserted = statement.executeUpdate();
                    if (rowsInserted > 0) {
                        System.out.println("Klient został pomyślnie dodany do bazy danych.");
                    }
                    else {
                        System.out.println(); // response
                    }
                }catch (SQLException | ClassNotFoundException e) {
                    logger.error(e.getMessage());
                }
            }
            else if (request.contains("klienciEdit")) {

                try {
                    ObjectInputStream objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
                    Klient editKlient = (Klient) objectInputStream.readObject();
                    String updateQuery = "UPDATE osoba SET imie = ?, nazwisko = ?, nr_tel = ?, miasto = ?, ulica = ?, nr_domu = ?, kod_pocztowy = ? WHERE id = ?";
                    PreparedStatement statement = dbCon.getConnection().prepareStatement(updateQuery);
                    statement.setString(1, editKlient.getImie());
                    statement.setString(2, editKlient.getNazwisko());
                    statement.setString(3, editKlient.getNr_tel());
                    statement.setString(4, editKlient.getMiasto());
                    statement.setString(5, editKlient.getUlica());
                    statement.setString(6, editKlient.getNr_domu());
                    statement.setString(7, editKlient.getKod());

                    statement.setLong(8, editKlient.getId());

                    int rowsUpdated = statement.executeUpdate();
                    if (rowsUpdated > 0) {
                        System.out.println("Klient został pomyślnie zaktualizowany w bazie danych.");
                    } else {
                        logger.warn("Nie znaleziono klienta o podanym identyfikatorze.");
                    }
                }catch (SQLException | ClassNotFoundException e) {
                    logger.error(e.getMessage());
                }
            }
            else if (request.contains("klienciDel")) {
                try {
                    ObjectInputStream objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
                    Klient editKlient = (Klient) objectInputStream.readObject();
                    String updateQuery = "DELETE FROM osoba WHERE id = ?";
                    PreparedStatement statement = dbCon.getConnection().prepareStatement(updateQuery);
                    statement.setLong(1, editKlient.getId());

                    int rowsUpdated = statement.executeUpdate();
                    if (rowsUpdated > 0) {
                        System.out.println("Klient został pomyślnie uzunięty z bazy danych.");
                    } else {
                        logger.warn("Nie znaleziono klienta o podanym identyfikatorze.");
                    }
                }catch (SQLException | ClassNotFoundException e) {
                    logger.error(e.getMessage());
                }
            }
            /// =========== Wypozyczenie

            else if (request.contains("wypozyczenieList")) {
                // Tworzenie listy
                List<Wypozyczenie> list = new ArrayList<>();

                try {
                    Statement statement = dbCon.getConnection().createStatement();
                    ResultSet resultSet = statement.executeQuery("SELECT * FROM wypozyczenie;");

                    while (resultSet.next()) {
                        long id = resultSet.getLong("id");
                        long id_plyta = resultSet.getLong("id_plyta");
                        long id_klient = resultSet.getLong("id_klient");
                        LocalDate dataW = resultSet.getDate("data_w").toLocalDate();
                        LocalDate dataZ = resultSet.getDate("data_z").toLocalDate();

                        Wypozyczenie wypozyczenie = new Wypozyczenie(id, id_plyta, id_klient, dataW, dataZ);
                        list.add(wypozyczenie);
                    }
                }catch (SQLException e) {
                    logger.error(e.getMessage());
                    throw new RuntimeException(e);
                }

                ObjectOutputStream objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                objectOutputStream.writeObject(list);
                objectOutputStream.flush();
            }
            else if (request.contains("wypozyczenieViewList")) {
                // Tworzenie listy
                List<WypozyczenieView> list = new ArrayList<>();

                try {
                    Statement statement = dbCon.getConnection().createStatement();
                    ResultSet resultSet = statement.executeQuery("SELECT wypozyczenie.id, osoba.imie, osoba.nazwisko, osoba.nr_tel, album.id, album.name, album.genre, album.quantity, wypozyczenie.data_w, wypozyczenie.data_z\n" +
                            "FROM osoba\n" +
                            "         JOIN wypozyczenie ON osoba.id = wypozyczenie.id_klient\n" +
                            "         JOIN album ON wypozyczenie.id_plyta = album.id;");

                    while (resultSet.next()) {
                        long id = resultSet.getLong("wypozyczenie.id");
                        String name = resultSet.getString("imie");
                        String surname = resultSet.getString("nazwisko");
                        String nrTel = resultSet.getString("nr_tel");
                        
                        long idDVD = resultSet.getLong("album.id");
                        String nazwaDVD = resultSet.getString("name");
                        String gatunekDVD = resultSet.getString("genre");
                        int iloscDVD = resultSet.getInt("quantity");
                        LocalDate dataW = resultSet.getDate("data_w").toLocalDate();
                        LocalDate dataZ = resultSet.getDate("data_z").toLocalDate();

                        WypozyczenieView wypozyczenieView = new WypozyczenieView(id, name, surname, nrTel, idDVD, nazwaDVD, gatunekDVD, iloscDVD, dataW, dataZ);
                        list.add(wypozyczenieView);
                    }
                }catch (SQLException e) {
                    logger.error(e.getMessage());
                    throw new RuntimeException(e);
                }

                ObjectOutputStream objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                objectOutputStream.writeObject(list);
                objectOutputStream.flush();
            }
            else if (request.contains("wypozyczenieAdd")) {

                try {
                    ObjectInputStream objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
                    Wypozyczenie newWypozyczenie = (Wypozyczenie) objectInputStream.readObject();
                    String insertQuery = "INSERT INTO wypozyczenie (id_klient, id_plyta, data_w, data_z) VALUES (?, ?, ?, ?)";
                    PreparedStatement statement = dbCon.getConnection().prepareStatement(insertQuery);
                    statement.setInt(1, (int) newWypozyczenie.getId_klient());
                    statement.setInt(2, (int) newWypozyczenie.getId_plyta());
                    statement.setDate(3, Date.valueOf(newWypozyczenie.getData_w()));
                    statement.setDate(4, Date.valueOf(newWypozyczenie.getData_z()));

                    int rowsInserted = statement.executeUpdate();
                    if (rowsInserted > 0) {
                        System.out.println("Wypożyczenie zostało pomyślnie dodany do bazy danych.");
                    }
                    else {
                        logger.warn("Nie udało się dodać wypożyczenia do bazy danych.");
                    }
                }catch (SQLException | ClassNotFoundException e) {
                    logger.error(e.getMessage());
                }
            }
            
            /// =========== Zwrot

            else if (request.contains("zwrotViewList")) {
                // Tworzenie listy
                List<ZwrotView> list = new ArrayList<>();

                try {
                    Statement statement = dbCon.getConnection().createStatement();
                    ResultSet resultSet = statement.executeQuery("SELECT * FROM zwrot;");

                    while (resultSet.next()) {
                        long id = resultSet.getLong("id");
                        String name = resultSet.getString("imieKlienta");
                        String surname = resultSet.getString("nazwiskoKlienta");
                        String nrTel = resultSet.getString("nrTelKlienta");

                        long idDVD = resultSet.getLong("idDVD");
                        String nazwaDVD = resultSet.getString("nazwaDVD");
                        String gatunekDVD = resultSet.getString("gatunekDVD");
                        float cenaDVD = resultSet.getFloat("cena");
                        int iloscDVD = resultSet.getInt("iloscSztuk");
                        LocalDate dataW = resultSet.getDate("data_w").toLocalDate();
                        LocalDate dataZ_planowana = resultSet.getDate("data_z_planowana").toLocalDate();
                        LocalDate dataZ = resultSet.getDate("data_z").toLocalDate();

                        ZwrotView zwrotView = new ZwrotView(id, name, surname, nrTel, idDVD, nazwaDVD, gatunekDVD, iloscDVD, cenaDVD, dataW, dataZ_planowana, dataZ);
                        list.add(zwrotView);
                    }
                }catch (SQLException e) {
                    logger.error(e.getMessage());
                    throw new RuntimeException(e);
                }

                ObjectOutputStream objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                objectOutputStream.writeObject(list);
                objectOutputStream.flush();
            }
            else if (request.contains("zwrot")) {
                try {
                    ObjectInputStream objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
                    long selectedId = (long) objectInputStream.readObject();
                    String query = "SELECT wypozyczenie.id, osoba.imie, osoba.nazwisko, osoba.nr_tel, album.id, album.name, album.genre, album.cena, album.quantity, wypozyczenie.data_w, wypozyczenie.data_z\n" +
                            "FROM osoba\n" +
                            "         JOIN wypozyczenie ON osoba.id = wypozyczenie.id_klient\n" +
                            "         JOIN album ON wypozyczenie.id_plyta = album.id WHERE wypozyczenie.id = ?;";
                    PreparedStatement statement = dbCon.getConnection().prepareStatement(query);

                    statement.setLong(1, selectedId);
                    ResultSet resultSet = statement.executeQuery();

                    if (resultSet.next()) {
                        long id = resultSet.getLong("wypozyczenie.id");
                        String name = resultSet.getString("imie");
                        String surname = resultSet.getString("nazwisko");
                        String nrTel = resultSet.getString("nr_tel");

                        long idDVD = resultSet.getLong("album.id");
                        String nazwaDVD = resultSet.getString("name");
                        String gatunekDVD = resultSet.getString("genre");
                        float cenaDVD = resultSet.getFloat("cena");
                        int iloscDVD = resultSet.getInt("quantity");
                        LocalDate dataW = resultSet.getDate("data_w").toLocalDate();
                        LocalDate dataZ_planowana = resultSet.getDate("data_z").toLocalDate();
                        LocalDate dataZ = LocalDate.now();

                        String insertQuery = "INSERT INTO zwrot (imieKlienta, nazwiskoKlienta, nrTelKlienta, idDVD, nazwaDVD, gatunekDVD, iloscSztuk, cena, data_w, data_z_planowana, data_z) " +
                                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                        PreparedStatement statementInsert = dbCon.getConnection().prepareStatement(insertQuery);
                        statementInsert.setString(1, name);
                        statementInsert.setString(2, surname);
                        statementInsert.setString(3, nrTel);
                        statementInsert.setLong(4, 1L);
                        statementInsert.setString(5, nazwaDVD);
                        statementInsert.setString(6, gatunekDVD);
                        statementInsert.setInt(7, iloscDVD);
                        statementInsert.setFloat(8, cenaDVD);
                        statementInsert.setDate(9, Date.valueOf(dataW));
                        statementInsert.setDate(10, Date.valueOf(dataZ_planowana));
                        statementInsert.setDate(11, Date.valueOf(dataZ));

                        int rowsInserted = statementInsert.executeUpdate();
                        if (rowsInserted > 0) {
                            System.out.println("Zwrot DVD został pomyślnie dodany do bazy danych.");

                            String updateQuery = "DELETE FROM wypozyczenie WHERE id = ?";
                            PreparedStatement statementDel = dbCon.getConnection().prepareStatement(updateQuery);
                            statementDel.setLong(1, selectedId);

                            int rowsUpdated = statementDel.executeUpdate();
                            if (rowsUpdated > 0) {
                                System.out.println("Wypożyczenie zostało pomyślnie uzunięty z bazy danych.");
                            } else {
                                logger.warn("Nie znaleziono wypożyczenia o podanym identyfikatorze.");
                            }
                        }
                        else {
                            logger.warn("Nie udało się dodać DVD do bazy danych.");
                        }

                    }
                    else {
                        logger.warn("Nie znaleziono wypożyczenia o podanym identyfikatorze.");
                    }

                }catch (SQLException | ClassNotFoundException e) {
                    logger.error(e.getMessage());
                }
            }
            
            else if (request.startsWith("bye")) {
                // połączenie zamyka klient
                isClosed = true;
            }
        }
    }
}
