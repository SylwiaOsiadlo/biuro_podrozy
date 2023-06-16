package com.Kaczor.Kaluzinski.Kalarus.Serwer.Client;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.jetbrains.annotations.NotNull;

/**
 * Kolejna klasa s³u¿¹ca do obs³ugi po³¹czenia klienta z serwerem. Posiada listê, do której dodawani
 * s¹ wszyscy zaakceptowani i po³¹czeni w obecnej chwili u¿ytkownicy. Zawiera metody zarz¹dzaj¹ce tymi 
 * u¿ytkownikami.
 */
public class ClientOperator extends Thread{
	private static List<ClientOperator> op = new ArrayList<ClientOperator>();
	private Client client;

	/**
	 * Konstruktor s³u¿¹cy do dodania zaakceptowanego klienta aplikacji do listy po³¹czonych u¿ytkowników.
	 * Tworzy dla niego osobny obiekt tej klasy, a co za tym idzie osobny w¹tek do jego obs³ugi. Przyjmuje jako
	 * parametr obiekt klasy Client, w którym znajduj¹ siê informacje o po³¹czonym u¿ytkowniku.  
	 * @param client obiekt klasy Client, który zawiera dane na temat pod³¹czaj¹cego siê do serwera pracownika
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
	 * Metoda odpowiedzialna za zamkniêcie wszystkich bie¿¹cych po³¹czeñ z serwerem.
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
	 * Metoda sprawdzaj¹ca czy mo¿na zamkn¹æ po³¹czenie pracownika o podanym loginie. Jeœli dany pracownik znajduje
	 * siê na liœcie po³¹czonych obecnie z serwerem u¿ytkowników, to nie mo¿na go usun¹æ.
	 * @param nick login pracownika, którego chcemy sprawdziæ
	 * @return true, jeœli nie ma przeszkód do roz³¹czenia pracownika lub false, jeœli nie mo¿na tego zrobiæ
	 */
	public synchronized static boolean isCanDelete(@NotNull String nick)
	{
		for(ClientOperator cl : op)
			if(cl.client.isNickCorrect(nick))
				return false;
		return true;
	}

	/**
	 * Metoda wykorzystuj¹ca metodê isCanDelete w celu sprawdzenia czy dany pracownik jest obecnie po³¹czony
	 * z serwerem.
	 * @param nick login pracownika, któego po³¹czenie chcemy sprawdziæ
	 * @return true, jeœli jest po³¹czony lub false jeœli nie
	 */
	public synchronized static boolean isExist(@NotNull String nick)
	{
		return !isCanDelete(nick);
	}

	/**
	 * Metoda s³u¿¹ca do usuniêcia pracownika o podanym loginie z listy pod³¹czonych u¿ytkowników.
	 * @param nick login pracownika, którego po³¹czenie chcemy zamkn¹æ
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
