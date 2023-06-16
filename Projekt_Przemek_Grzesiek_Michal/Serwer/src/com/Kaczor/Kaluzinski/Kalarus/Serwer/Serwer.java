package com.Kaczor.Kaluzinski.Kalarus.Serwer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

import com.Kaczor.Kaluzinski.Kalarus.Serwer.Client.Client;
import com.Kaczor.Kaluzinski.Kalarus.Serwer.Client.ClientOperator;

/**
 * Klasa serwera s³u¿¹ca do przyjmowania nowych po³¹czeñ od pracowników (klientów aplikacji)
 *  oraz tworz¹ca nowe w¹tki do ich obs³ugi.
 */
public class Serwer {
	private ServerSocket sock;
	private AtomicBoolean isworking = new AtomicBoolean(true);
	private AtomicBoolean issleeping = new AtomicBoolean(false);

	/**
	 * Metoda s³u¿¹ca do usypiania pêtli akceptuj¹cej nowe po³¹czenia.
	 * @param issleeping wartoœæ true usypia pêtlê  
	 */
	public void setSleep(boolean issleeping) {
		this.issleeping.set(issleeping);		
	}

	/**
	 * Konstruktor ustawiaj¹cy nowe gniazdo dla podanego w paramterze portu przy tworzeniu obiektu.
	 * @param port port
	 * @throws IOException
	 */
	public Serwer(int port) throws IOException
	{
		sock = new ServerSocket(port);
	}

	/**
	 * Metoda s³u¿¹ca do przyjmowania nowych po³¹czeñ i akceptowania ich.
	 * @throws IOException
	 */
	public void activeWaiting() throws IOException 
	{
		//0.0.0.0 jest spowodowane tym, ¿e server binduje do wszystkich adresów IP na komputerze
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
	 * Metoda wy³¹czaj¹ca aktywne oczekiwanie na nadchodz¹ce po³¹czenia od pracowników (klientów aplikacji).
	 */
	public void stopWaiting()
	{
		this.isworking.set(false);
	}

	/**
	 * Metoda wywo³ywana w celu przejœcia programu w stan oczekiwania do zamkniêcia
	 */
	public void waitToStop()
	{	
		this.isworking.set(false);
	}

	/**
	 * Metoda zamyka wszystkie aktywne po³¹czenia klientów aplikacji z serwerem.
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
