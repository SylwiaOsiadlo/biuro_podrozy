package com.Kaczor.Kaluzinski.Kalarus.Serwer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

import com.Kaczor.Kaluzinski.Kalarus.Serwer.Client.Client;
import com.Kaczor.Kaluzinski.Kalarus.Serwer.Client.ClientOperator;

/**
 * Klasa serwera s�u��ca do przyjmowania nowych po��cze� od pracownik�w (klient�w aplikacji)
 *  oraz tworz�ca nowe w�tki do ich obs�ugi.
 */
public class Serwer {
	private ServerSocket sock;
	private AtomicBoolean isworking = new AtomicBoolean(true);
	private AtomicBoolean issleeping = new AtomicBoolean(false);

	/**
	 * Metoda s�u��ca do usypiania p�tli akceptuj�cej nowe po��czenia.
	 * @param issleeping warto�� true usypia p�tl�  
	 */
	public void setSleep(boolean issleeping) {
		this.issleeping.set(issleeping);		
	}

	/**
	 * Konstruktor ustawiaj�cy nowe gniazdo dla podanego w paramterze portu przy tworzeniu obiektu.
	 * @param port port
	 * @throws IOException
	 */
	public Serwer(int port) throws IOException
	{
		sock = new ServerSocket(port);
	}

	/**
	 * Metoda s�u��ca do przyjmowania nowych po��cze� i akceptowania ich.
	 * @throws IOException
	 */
	public void activeWaiting() throws IOException 
	{
		//0.0.0.0 jest spowodowane tym, �e server binduje do wszystkich adres�w IP na komputerze
		//0.0.0.0 jest adresem wildcard
		System.out.println("Aktywowano serwer, pod adresem " + sock.getInetAddress().getHostName());
		sock.setSoTimeout(50);
		
		while(this.isworking.get())
		{	

			try
			{
				Socket socket = sock.accept();
				
				System.out.println("Odebrano nowe polaczenie z adresu: " + socket.getLocalAddress().getHostName());
				ClientOperator.addOperator(new Client(socket));
			}
			catch(SocketTimeoutException e)
			{
				while(this.issleeping.get());
					
			}
		}
	}

	/**
	 * Metoda wy��czaj�ca aktywne oczekiwanie na nadchodz�ce po��czenia od pracownik�w (klient�w aplikacji).
	 */
	public void stopWaiting()
	{
		this.isworking.set(false);
	}

	/**
	 * Metoda wywo�ywana w celu przej�cia programu w stan oczekiwania do zamkni�cia
	 */
	public void waitToStop()
	{	
		this.isworking.set(false);
	}

	/**
	 * Metoda zamyka wszystkie aktywne po��czenia klient�w aplikacji z serwerem.
	 */
	public void Close()
	{
		try {
			ClientOperator.closeAll();
			sock.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
