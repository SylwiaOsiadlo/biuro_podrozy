package com.psk.wypozyczalniaDVD_klient;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
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
        private Socket clientSocket;

        public RequestHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {

            try {
                while (true) {
                    handleRequest(clientSocket);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        private static void handleRequest(Socket clientSocket) throws IOException {
            //clientSocket.setSoTimeout(1000);
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String request = reader.readLine();
            System.out.println("Odebrano żądanie: " + request);

            if (request.startsWith("plytyList")) {
                OutputStream outputStream = clientSocket.getOutputStream();
                // Tworzenie przykładowej listy
                List<Album> list = new ArrayList<>();
                list.add(new Album("Skyfall", "film akcji", 12));
                list.add(new Album("Shrek 2", "bajka", 42));
                list.add(new Album("Shrek 3", "bajka", 33));

                ObjectOutputStream objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                objectOutputStream.writeObject(list);
                objectOutputStream.flush();

                outputStream.close();
            }
        }
    }
}
