package com.psk.wypozyczalniaDVD;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class DatabaseTests {

    public static ClientConnection clientConnection;

    @Test
    public void testServerConnection() {
        clientConnection = new ClientConnection();
        clientConnection.connect("localhost", 8080);

        Assert.assertTrue(clientConnection.isConnected());
    }

    @Test
    public void testAddAndDeleteCustomer() {
        Klient klient = new Klient("Testowe", "Testowy", "111222333", "Test",
                "Test", "10", "11-111");
        clientConnection.sendObject("klienciAdd", klient);

        List<Klient> receivedList = (List<Klient>) clientConnection.requestObject("klienciList");

        Klient foundClient = null;

        for (Klient klient1 : receivedList) {
            if (klient1.getImie().equals("Testowe") && klient1.getNazwisko().equals("Testowy") &&
                    klient1.getNr_tel().equals("111222333")) {
                foundClient = klient1;
                break;
            }
        }

        Assert.assertNotNull(foundClient);

        clientConnection.sendObject("klienciDel", foundClient);

        receivedList = (List<Klient>) clientConnection.requestObject("klienciList");

        foundClient = null;

        for (Klient klient1 : receivedList) {
            if (klient1.getImie().equals("Testowe") && klient1.getNazwisko().equals("Testowy") &&
                    klient1.getNr_tel().equals("111222333")) {
                foundClient = klient1;
                break;
            }
        }

        Assert.assertNull(foundClient);
    }
}
