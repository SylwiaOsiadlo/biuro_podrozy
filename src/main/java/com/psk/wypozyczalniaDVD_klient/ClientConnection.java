package com.psk.wypozyczalniaDVD_klient;

import java.io.*;
import java.net.Socket;

public class ClientConnection {

    private static Socket socket;
    private static BufferedReader reader;
    private static OutputStream outputStream;

    public void connect(String hostname, int port) {
        // Nawiązanie połączenia z serwerem przy uruchomieniu aplikacji
        try {
            socket = new Socket(hostname, port);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outputStream = socket.getOutputStream();

            System.out.println("Połączono z serwerem");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Object requestObject(String objectName) {
        try {
            PrintWriter writer = new PrintWriter(outputStream, true);
            writer.println(objectName);
            outputStream.flush();

            // Odczytywanie odpowiedzi serwera
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            Object obj = objectInputStream.readObject();

            objectInputStream.close();
            return obj;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    void closeConnection() throws IOException {
        // Zamknięcie strumieni i gniazda
        outputStream.close();

        reader.close();
        socket.close();
        System.out.println("Zamknięto połączenie z serwerem");
    }
}
