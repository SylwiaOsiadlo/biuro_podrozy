package com.psk.wypozyczalniaDVD;

import javafx.scene.control.Alert;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class ClientConnection {

    private static final Logger logger = LogManager.getLogger(ClientConnection.class);

    private static Socket socket;
    private static BufferedReader reader;
    private static OutputStream outputStream;

    public void connect(String hostname, int port) {
        // Nawiązanie połączenia z serwerem przy uruchomieniu aplikacji
        try {
            socket = new Socket(hostname, port);
            socket.setSoTimeout(1000);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outputStream = socket.getOutputStream();

            logger.info("Połączono z serwerem.");
        } catch (ConnectException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd połączenia");
            alert.setHeaderText("Wystąpił błąd połączenia.");
            alert.setContentText(e.getMessage());

            alert.show();
            logger.error("Błąd połączenia: " + e.getMessage());
        }
        catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd połączenia");
            alert.setHeaderText("Wystąpił nieznany błąd połączenia.");
            alert.show();
        }
    }

    public Object requestObject(String objectName) {
        if (socket == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd połączenia");
            alert.setHeaderText("Nie połączono z serwerem.");
            alert.setContentText("Uruchom aplikację ponownie.");
            alert.show();

            return null;
        }

        try {
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            dos.writeUTF(objectName);
            dos.flush();

            // Odczytywanie odpowiedzi serwera
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            return objectInputStream.readObject();
        } catch (SocketTimeoutException e) {
            logger.error(e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd połączenia");
            alert.setHeaderText("Przekroczono limit czasu połączenia dla tego żądania.");
            alert.show();
            return null;
        } catch (IOException | ClassNotFoundException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public void sendCommand(String command) {
        if (socket == null)
            return;

        try {
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            dos.writeUTF(command);
            dos.flush();

        } catch (SocketTimeoutException e) {
            logger.error(e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd połączenia");
            alert.setHeaderText("Przekroczono limit czasu połączenia dla tego żądania.");
            alert.show();
        } catch (IOException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

    public void sendObject(String objectName, Object obj) {
        try {
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            dos.writeUTF(objectName);
            dos.flush();

            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(obj);
            oos.flush();
            outputStream.flush();
        } catch (SocketTimeoutException e) {
            logger.error(e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd połączenia");
            alert.setHeaderText("Przekroczono limit czasu połączenia dla tego żądania.");
            alert.show();
        }  catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
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
        logger.info("Zamknięto połączenie z serwerem.");
    }
}
