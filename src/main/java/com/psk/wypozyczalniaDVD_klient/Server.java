package com.psk.wypozyczalniaDVD_klient;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private static final int THREAD_POOL_SIZE = 10; // Rozmiar puli wątków
    private static boolean isRunning = true; // Flaga informująca o działaniu serwera

    public static void main(String[] args) {
        int port = 8000; // Port, na którym serwer będzie nasłuchiwał

        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Serwer uruchomiony na porcie " + port);

            // Utworzenie puli wątków
            ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

            while (isRunning) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Połączenie przychodzące z adresu: " + clientSocket.getInetAddress());

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
                System.out.println("Rozlaczono klienta.");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

        }

        private void handleRequest(Socket clientSocket) throws IOException, ClassNotFoundException {
            //clientSocket.setSoTimeout(1000);
            DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
            String request = dis.readUTF();

            System.out.println("Odebrano żądanie: " + request);

            if (request.contains("plytyList")) {
                // Tworzenie przykładowej listy
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
                        System.out.println("ID: " + id);
                        System.out.println("Name: " + name);
                        System.out.println("Genre: " + genre);
                        System.out.println("Quantity: " + quantity);
                        System.out.println("Cena: " + cena);
                        System.out.println("--------------------");
                    }
                }catch (SQLException e) {
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
                        System.out.println(); // response
                    }
                }catch (SQLException | ClassNotFoundException e) {
                    System.out.println(e);
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
                        System.out.println("Nie znaleziono albumu o podanym identyfikatorze.");
                    }
                }catch (SQLException | ClassNotFoundException e) {
                    System.out.println(e);
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
                        System.out.println("Nie znaleziono albumu o podanym identyfikatorze.");
                    }
                }catch (SQLException | ClassNotFoundException e) {
                    System.out.println(e);
                }
            }
            else if (request.startsWith("bye")) {
                if (!clientSocket.isClosed()) {
                    clientSocket.getOutputStream().close();
                    clientSocket.getInputStream().close();
                    clientSocket.close();
                }
                isClosed = true;
            }
        }
    }
}
