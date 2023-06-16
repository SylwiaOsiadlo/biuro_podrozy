package com.Kaczor.Kaluzinski.Kalarus.Serwer.Client;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.jetbrains.annotations.NotNull;

/**
 * Kolejna klasa s�u��ca do obs�ugi po��czenia klienta z serwerem. Posiada list�, do kt�rej dodawani
 * s� wszyscy zaakceptowani i po��czeni w obecnej chwili u�ytkownicy. Zawiera metody zarz�dzaj�ce tymi 
 * u�ytkownikami.
 */
public class ClientOperator extends Thread{
	private static List<ClientOperator> op = new ArrayList<ClientOperator>();
	private Client client;

	/**
	 * Konstruktor s�u��cy do dodania zaakceptowanego klienta aplikacji do listy po��czonych u�ytkownik�w.
	 * Tworzy dla niego osobny obiekt tej klasy, a co za tym idzie osobny w�tek do jego obs�ugi. Przyjmuje jako
	 * parametr obiekt klasy Client, w kt�rym znajduj� si� informacje o po��czonym u�ytkowniku.  
	 * @param client obiekt klasy Client, kt�ry zawiera dane na temat pod��czaj�cego si� do serwera pracownika
	 */
	public synchronized static void addOperator(@NotNull Client client)
	{
		ClientOperator cop = new ClientOperator();
		cop.client = client;
		op.add(cop);
		cop.start();
	}
	
	@Override
	public void run()
	{
		client.activeWaiting();
		op.remove(this);
	}

	/**
	 * Metoda odpowiedzialna za zamkni�cie wszystkich bie��cych po��cze� z serwerem.
	 */
	public synchronized static void closeAll()
	{
		ListIterator<ClientOperator> begin = op.listIterator();
		while(begin.hasNext()) {
			ClientOperator buff = begin.next();
			buff.client.setStop(true);
			buff.client.forceclose();
			begin.remove();
		}
	}

	/**
	 * Metoda sprawdzaj�ca czy mo�na zamkn�� po��czenie pracownika o podanym loginie. Je�li dany pracownik znajduje
	 * si� na li�cie po��czonych obecnie z serwerem u�ytkownik�w, to nie mo�na go usun��.
	 * @param nick login pracownika, kt�rego chcemy sprawdzi�
	 * @return true, je�li nie ma przeszk�d do roz��czenia pracownika lub false, je�li nie mo�na tego zrobi�
	 */
	public synchronized static boolean isCanDelete(@NotNull String nick)
	{
		for(ClientOperator cl : op)
			if(cl.client.isNickCorrect(nick))
				return false;
		return true;
	}

	/**
	 * Metoda wykorzystuj�ca metod� isCanDelete w celu sprawdzenia czy dany pracownik jest obecnie po��czony
	 * z serwerem.
	 * @param nick login pracownika, kt�ego po��czenie chcemy sprawdzi�
	 * @return true, je�li jest po��czony lub false je�li nie
	 */
	public synchronized static boolean isExist(@NotNull String nick)
	{
		return !isCanDelete(nick);
	}

	/**
	 * Metoda s�u��ca do usuni�cia pracownika o podanym loginie z listy pod��czonych u�ytkownik�w.
	 * @param nick login pracownika, kt�rego po��czenie chcemy zamkn��
	 */
	public synchronized static void removeWorker(@NotNull String nick)
	{
		for(ClientOperator cl : op)
		   if(cl.client.isNickCorrect(nick))
		   {
			   cl.client.setStop(true);
			   cl.client.forceclose();
			   op.remove(cl);
			   return;
		   }
			   
	}
	
}
