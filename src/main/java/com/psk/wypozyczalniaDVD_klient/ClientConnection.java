package com.psk.wypozyczalniaDVD_klient;

import javafx.scene.control.Alert;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class ClientConnection {

    private static Socket socket;
    private static BufferedReader reader;
    private static OutputStream outputStream;
    private static ObjectOutputStream objectOutputStream;

    public void connect(String hostname, int port) {
        // Nawiązanie połączenia z serwerem przy uruchomieniu aplikacji
        try {
            socket = new Socket(hostname, port);
            socket.setSoTimeout(1000);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outputStream = socket.getOutputStream();

            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

            System.out.println("Połączono z serwerem");
        } catch (ConnectException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd połączenia");
            alert.setHeaderText("Wystąpił błąd połączenia.");
            alert.setContentText(e.getMessage());

            alert.show();
        }
        catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd połączenia");
            alert.setHeaderText("Wystąpił nieznany błąd połączenia.");
            alert.show();
        }
    }

    public Object requestObject(String objectName) {
        try {
            outputStream.flush();
            PrintWriter writer = new PrintWriter(outputStream, true);
            writer.flush();
            writer.println(objectName);

            // Odczytywanie odpowiedzi serwera
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            return objectInputStream.readObject();
        } catch (SocketTimeoutException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd połączenia");
            alert.setHeaderText("Przekroczono limit czasu połączenia dla tego żądania.");
            alert.show();
            return null;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void sendCommand(String command) {
        try {
            outputStream.flush();
            PrintWriter writer = new PrintWriter(outputStream, true);
            writer.flush();
            writer.println(command);
        } catch (SocketTimeoutException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd połączenia");
            alert.setHeaderText("Przekroczono limit czasu połączenia dla tego żądania.");
            alert.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendObject(String objectName, Object obj) {
        try {
            PrintWriter writer = new PrintWriter(outputStream, true);
            writer.println(objectName);

            outputStream.flush();
            objectOutputStream.writeObject(obj);
            outputStream.flush();
        } catch (SocketTimeoutException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd połączenia");
            alert.setHeaderText("Przekroczono limit czasu połączenia dla tego żądania.");
            alert.show();
        }  catch (IOException e) {
            e.printStackTrace();
        }
    }

    void closeConnection() throws IOException {
        if (socket == null)
            return;

        if (socket.isClosed())
            return;

        // Zamknięcie strumieni i gniazda
        outputStream.close();

        reader.close();
        socket.close();
        System.out.println("Zamknięto połączenie z serwerem");
    }
}
