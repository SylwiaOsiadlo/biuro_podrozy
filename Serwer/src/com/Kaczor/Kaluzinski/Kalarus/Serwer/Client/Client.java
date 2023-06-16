package com.Kaczor.Kaluzinski.Kalarus.Serwer.Client;

import java.io.IOException;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;

import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.Kaczor.Kaluzinski.Kalarus.Communication.Aggrement;
import com.Kaczor.Kaluzinski.Kalarus.Communication.Communication;
import com.Kaczor.Kaluzinski.Kalarus.Communication.Trip;
import com.Kaczor.Kaluzinski.Kalarus.Communication.WhatToDo;
import com.Kaczor.Kaluzinski.Kalarus.Communication.WorkerLogin;
import com.Kaczor.Kaluzinski.Kalarus.Communication.WorkerLoginReturn;
import com.Kaczor.Kaluzinski.Kalarus.Serwer.Database.Adresy;
import com.Kaczor.Kaluzinski.Kalarus.Serwer.Database.DataBaseManager;
import com.Kaczor.Kaluzinski.Kalarus.Serwer.Database.Payment;
import com.Kaczor.Kaluzinski.Kalarus.Serwer.Database.Worker;

/**
 * Klasa obrazuje pojedyncze po³¹czenie klienta aplikacji z serwerem.
 * Zawiera metody odpowiedzialne za zarz¹dzanie operacjami, o wykonanie których prosi
 * klient aplikacji. W skrócie zawiera wszystkie implementacje reakcji na ¿¹dania wykorzystywanych w aplikacji.
 */
public class Client{
	private Socket sock;
	private boolean isworking;
	private boolean stoper;
	private Worker wo = null;
	private ObjectOutputStream stream;
	private ObjectInputStream streamin;
	private static final Logger logger = Logger.getLogger(Client.class);

	/**
	 * Konstruktor, który s³u¿y do stworzenia nowych strumieni wejœciowych i wyjœciowych dla
	 * nowo po³¹czonego klienta. Jako parametr przyjmuje gniazdo tego klienta.
	 * @param sock gniazdo nowo pod³¹czonego klienta
	 */
	public Client(@NotNull Socket sock)
	{
		this.sock = sock;
		try {
			stream = new ObjectOutputStream(sock.getOutputStream());
			streamin = new ObjectInputStream(sock.getInputStream());
		} catch (IOException e) {
			logger.error("Error!", e);
		}
	}

	/**
	 * Metoda zawieraj¹ca nieskoñczon¹ pêtlê s³u¿¹c¹ do aktywnego oczekiwania na ¿¹dania od klienta aplikacji.
	 * Podejmuje odpowiednie dzia³ania w zale¿noœci od rodzaju otrzymanego ¿¹dania.
	 * Sprawdza, czy obiekt o który prosi klient jest obiektem jednej z klas komunikacyjnych.
	 * W przypadku b³êdu przerywa pêtlê.
	 */
	public void activeWaiting()
	{
		isworking = true;
		stoper = true;
		main_loop:while(stoper)
		{
			try {
				try {
					Object obj = streamin.readObject();
					if(!(obj instanceof Communication))
						continue;
					Communication com = (Communication)obj;
					if(obj instanceof WorkerLogin)
					{
						if(wo==null)
						{
							String buff = ((WorkerLogin)obj).getNick();
							if(buff.length()<4)
								stream.writeObject(new WorkerLoginReturn("Nick musi mieæ conajmniej 3 znaki!!!", true));
							if(ClientOperator.isExist(buff))
								stream.writeObject(new WorkerLoginReturn("U¿ytkownik o takim nicku jest ju¿ zalogowany do systemu!", true));
							else
								workerLogin((WorkerLogin)obj);
						}
						else
						{
							stream.writeObject(new WorkerLoginReturn("Wylogowano pomyœlnie", false));
							wo = null;
						}
					}
					else if(obj instanceof com.Kaczor.Kaluzinski.Kalarus.Communication.Client)
					{
						com.Kaczor.Kaluzinski.Kalarus.Communication.Client cl = (com.Kaczor.Kaluzinski.Kalarus.Communication.Client)obj;
						if(com.getWhatToDo()==null)
						{
							stream.writeObject(new WorkerLoginReturn("WhatToDo NULL", true));
							return;
						}
						switch(com.getWhatToDo())
						{
						case ADD:
							ClientOperatorAdd(cl);
							break;
						case GET:
							ClientOperatorGet(cl);
							break;
						case UPDATE:
							ClientOperatorUpdate(cl);
							break;
						case REMOVE:
							ClientOperatorRemove(cl);
							break;
						default:
							continue main_loop;
						
						}
					}
					else if(obj instanceof com.Kaczor.Kaluzinski.Kalarus.Communication.Trip && !(obj  instanceof com.Kaczor.Kaluzinski.Kalarus.Communication.Aggrement))
					{
						switch (com.getWhatToDo()) {
						case ADD:
							TripOperatorAdd((Trip)obj);
							break;
						case GET_TRIPS:
							TripOperatorGetTrips((Trip)obj);
						default:
							break;
						}
					}
					else if(obj instanceof com.Kaczor.Kaluzinski.Kalarus.Communication.Aggrement)
					{
						switch (com.getWhatToDo()) {
						case ADD:
							AggrementOperatoAdd((Aggrement)obj);
							break;
						case GET_TRIPS:
							AggrementOperatorGet((Aggrement)obj);
							break;
						case UPDATE:
							AggrementOperatorUpdate((Aggrement)obj);
						default:
							break;
						}
						
					}
				} catch (ClassNotFoundException e) {
					logger.error("Error class not found!", e);
				}
			} catch (IOException e) {
				logger.error("Error: " + wo==null?"null":wo.getName(), e);
				stoper = false;
			}
		}
	    
	    isworking = false;    
	}
	
	/**
	 * Metoda sprawdza czy podany przez pracownika nick jest identyczny z nickiem
	 * pracownika znajduj¹cym siê w obecnie przetwarzanym obiekcie klasy Worker.
	 * @param nick wpisany przez u¿ytkownika nick
	 * @return true, jeœli nick jest poprawny lub false w przeciwnym wypadku
	 */
	boolean isNickCorrect(@NotNull String nick)
	{
		return  wo!=null?wo.getNick().equals(nick):false;
	}

	/**
	 * Metoda s³u¿¹ca do obs³u¿enia ¿¹dania klienta aplikacji dotycz¹cego aktualizacji danych
	 * w obiekcie zawieraj¹cym umowê p³atnicz¹ klienta biura. Aktualizuje dane w bazie danych. 
	 * Wykorzystuje do tego funkcje z klasy DataBaseManager.
	 * @param obj obiekt klasy Agreement, który chcemy zaktualizowaæ
	 * @throws IOException wyj¹tek
	 */
	private void AggrementOperatorUpdate(@NotNull Aggrement obj) throws IOException
	{
		
		if(!obj.getPayment_type().equalsIgnoreCase("Przelew"))
		{
			stream.writeObject(new WorkerLoginReturn("Mo¿na zmienic tylko stan p³atnoœci w przelewu", true));
			return;
		}
		com.Kaczor.Kaluzinski.Kalarus.Serwer.Database.Payment pay = DataBaseManager.PaymentManager.getPaymentByAggrement(obj.getIdaggrement());
		
		if(pay == null || !(obj.getPayment_status().equals("Zap³acono") || obj.getPayment_status().equals("Niezap³acono")))
		{
			stream.writeObject(new WorkerLoginReturn("Wyst¹pi³ b³¹d!", true));
			return;
		}
		
		pay.setState(obj.getPayment_status());
		if(!DataBaseManager.PaymentManager.updatePayment(pay))
		{
			stream.writeObject(new WorkerLoginReturn("Wyst¹pi³ b³¹d!", true));
			return;
		}
		
		stream.writeObject(new WorkerLoginReturn("Zmieniono stan p³atnoœci!", false));
		
	}

	/**
	 * Metoda ustawia wartoœæ zmiennej odpowiedzialnej za nieskoñczcone dzia³anie pêtli
	 * w metodzie ActiveWaiting na true lub false. S³u¿y do zatrzymywania (false) aktywnego oczekiwania
	 * na ¿¹dania klienta aplikacji lub jego wznawianie (true)
	 * @param value wartoœæ true lub false decyduj¹ca czy chcemy zatrzymaæ czy wznowiæ aktywne oczekiwanie
	 */
	public void setStop(boolean value)
	{
		stoper = value;
	}

	/**
	 * Metoda s³u¿¹ca do przejœcia w tryb oczekiwania na zatrzymanie.
	 * wykonuje nieskoñczon¹ pêtlê nie robi¹c¹ kompletnie nic poza dzia³aniem.
	 */
	public void waitToStop()
	{
		while(isworking);
	}
	
	/**
	 * Metoda wymusza na serwerze zamkniêcie po³¹czenia z klientem aplikacji.
	 * Zamyka gniazdo socket oraz strumienie wejœciowe i wyjœciowe klienta aplikacji.
	 */
	public void forceclose()
	{
		try {
			sock.getInputStream().close();
			sock.getOutputStream().close();
			sock.close();
		} catch (IOException e) {
		}
	}

	/**
	 * Metoda obs³uguj¹ca ¿¹danie klienta aplikacji dotycz¹ce zwrócenia danych na temat
	 * umowy p³atniczej klienta biura. Zapisuje ¿¹dane dane do strumieni. Pobiera dane z bazy danych przy pomocy metod
	 * z klasy DatabaseManager.
	 * @param ag obiekt klasy komunikacyjnej Agreement
	 * @throws IOException wyj¹tek
	 */
	public void AggrementOperatorGet(@NotNull com.Kaczor.Kaluzinski.Kalarus.Communication.Aggrement ag) throws IOException
	{
		com.Kaczor.Kaluzinski.Kalarus.Serwer.Database.Aggrement[] aggrements = DataBaseManager.AgrrementManager.getAggrements(wo.getIdOffice(), ag);
		
		if(aggrements == null)
		{
			Aggrement buff1 = new Aggrement(true, "Error!!");
			buff1.setWhatToDo(WhatToDo.TRIPS_ERROR);
			stream.writeObject(buff1);
			return;
		}
		for(com.Kaczor.Kaluzinski.Kalarus.Serwer.Database.Aggrement buff : aggrements)
		{
			Payment pay = DataBaseManager.PaymentManager.getPaymentByAggrement(buff.getIdAggrement());
			com.Kaczor.Kaluzinski.Kalarus.Serwer.Database.Trip tr = DataBaseManager.TripManager.getTrip(buff.getIdTrip());
			if(pay == null || tr == null)
			{
				Aggrement buff1 = new Aggrement(true, "Error!!");
				buff1.setWhatToDo(WhatToDo.TRIPS_ERROR);
				stream.writeObject(buff1);
				return;
			}
			
			Aggrement buffer = new Aggrement(buff.getIdAggrement(),buff.getIdTrip(), 0, 0, 0, buff.getGuestcount(), 0, null, null, buff.getAggrement_date(), tr.getName(), null, pay.getTotal_cost(), pay.getType(), pay.getState());
			stream.writeObject(buffer);
		}
		
		stream.writeObject(new Aggrement(true, null));
	}

	/**
	 * Metoda s³u¿¹ca do obs³u¿enia ¿¹dania klienta aplikacji dotycz¹cego dodania nowej umowy p³atniczzej.
	 * Tworzy nowe obiekty oraz dodaje ich zawartoœæ do bazy danych. Wykorzystuje metody z klasy DatabaseManager.
	 * @param ag obiekt klasy komunikacyjnej Agreement z danymi do dodania
	 * @throws IOException wyj¹tek
	 */
	public void AggrementOperatoAdd(@NotNull com.Kaczor.Kaluzinski.Kalarus.Communication.Aggrement ag) throws IOException
	{
		if(ag.getPrice()<= 0)
		{
			stream.writeObject(new WorkerLoginReturn("Koszt nie mo¿e byæ mniejszy lub równy zeru!", true));
			return;
		}
		if(ag.getGuestcount()<=0)
		{
			stream.writeObject(new WorkerLoginReturn("Liczba goœci nie mo¿e byæ mniejsza b¹dz równa zeru!", true));
			return;
		}
		com.Kaczor.Kaluzinski.Kalarus.Serwer.Database.Aggrement aggrement = new com.Kaczor.Kaluzinski.Kalarus.Serwer.Database.Aggrement(ag.getIdClient(), ag.getIdTrip(), ag.getGuestcount(), ag.getAggrementdate());
		com.Kaczor.Kaluzinski.Kalarus.Serwer.Database.Trip tr = DataBaseManager.TripManager.getTrip(aggrement.getIdTrip());

		if(tr == null)
		{
			stream.writeObject(new WorkerLoginReturn("Wyst¹pi³ b³¹d!!!", true));
			return;
		}
		if(tr.getMaxguests()<aggrement.getGuestcount())
		{
			stream.writeObject(new WorkerLoginReturn("Brak miejsc!!!", true));
			return;
		}
		int max = tr.getMaxguests();
		tr.setMaxguests(max - aggrement.getGuestcount());
		if(!DataBaseManager.TripManager.updateTrip(tr))
		{
			stream.writeObject(new WorkerLoginReturn("Wyst¹pi³ b³¹d1", true));
			return;
		}
		if(!DataBaseManager.AgrrementManager.addAggrement(aggrement))
		{
			tr.setMaxguests(max);
			DataBaseManager.TripManager.updateTrip(tr);
			stream.writeObject(new WorkerLoginReturn("Wyst¹pi³ b³¹d2", true));
			return;
		}
		if(!DataBaseManager.PaymentManager.addPayment(new Payment(aggrement.getIdAggrement(), ag.getPayment_type(), ag.getPayment_status(), ag.getPrice())))
		{
			tr.setMaxguests(max);
			DataBaseManager.TripManager.updateTrip(tr);
			DataBaseManager.AgrrementManager.removeAggrement(aggrement.getIdAggrement());
			stream.writeObject(new WorkerLoginReturn("Wyst¹pi³ b³¹d3", true));
			return;
		}
		
		stream.writeObject(new WorkerLoginReturn("Dodano umowê", false));	
	}

	/**
	 * Metoda obs³uguj¹ca logowanie pracownika. Przyjmuje obiekt klasy komunikacyjnej,
	 * w którym s¹ zawarte wpisane przez u¿ytkownika dane logowania. Nastêpnie szuka pracownika
	 * o takim loginie i porownuje podane has³o z przypisanym mu w bazie danych.
	 * Jako odpowiedz tworzy obiekt klasy komunikacyjnej WorkerLoginRetrun przyjmuj¹cy wiadomoœæ
	 * opisuj¹c¹ przebieg operacji i wartoœæ true lub false okreœlaj¹cy czy wyst¹pi³ b³¹d.
	 * @param l obiekt klasy komunikacyjnej posiadaj¹cy zestaw loginu i has³a wpisane przez pracownika
	 * @throws IOException wyj¹tek
	 */
	public void workerLogin(@NotNull WorkerLogin l) throws IOException
	{
		wo = DataBaseManager.WorkerManager.getWorker(l.getNick());
		if(wo == null)
		{
			stream.writeObject(new WorkerLoginReturn("Nick nie istnieje w bazie danych!",true));
			return;
		}
		
		if(l.getPassword().equals(wo.getPassword()))
			stream.writeObject(new WorkerLoginReturn("Zalogowano pomyœlnie!", false));
		else
			stream.writeObject(new WorkerLoginReturn("Nie poprawne has³o", true));
	}

	/**
	 * Metoda obs³uguje ¿¹danie dodania nowego klienta biura do bazy. Przyjmuje obiekt klasy komunikacyjnej Client
	 * który przechowuje zestaw danych osoby, któr¹ u¿ytkownik chce dodaæ.
	 * Tworzy nowe obiekty oraz dodaje ich zawartoœæ do bazy danych. Wykorzystuje metody z klasy DatabaseManager.
	 * W przypadku powowdzenia lub b³êdu tworzy obiekt zwracaj¹cy informacje o statusie operacji.
	 * @param cl obiekt klasy komunikacyjnej z zestawem danych klienta biura, którego chcemy dodaæ
	 * @throws IOException wyj¹tek
	 */
	public void ClientOperatorAdd(@NotNull com.Kaczor.Kaluzinski.Kalarus.Communication.Client cl) throws IOException
	{
		String buff = checkClientBeforeAdd(cl);
		if(buff!=null)
		{
			stream.writeObject(new WorkerLoginReturn(buff, true));
			return;
		}

		if(DataBaseManager.ClientManager.isClientExist(wo.getIdOffice(), cl.getPesel()))
		{
			stream.writeObject(new WorkerLoginReturn("Klient o podanym peselu istnieje w bazie danych ", true));
			return;
		}
		Adresy ad = new Adresy(cl.getCountry(), cl.getCity(), cl.getHomenumber(), cl.getPostcode(), cl.getStreet());
		
		com.Kaczor.Kaluzinski.Kalarus.Serwer.Database.Client client = new com.Kaczor.Kaluzinski.Kalarus.Serwer.Database.Client(cl.getName(), cl.getSurname(), cl.getPesel(), cl.getPhoneNumber());
		
		client.setIdBiura(wo.getIdOffice());
		
		if(DataBaseManager.AdresyManager.addAdresy(ad))
		{
			client.setIdAdresy(ad.getIdAdresy());
			if(DataBaseManager.ClientManager.addClient(client))
			{
				stream.writeObject(new WorkerLoginReturn("Klient zostal dodany do bazy danych", false));
				return;
			}
		}
		stream.writeObject(new WorkerLoginReturn("Klient o podanym peselu istnieje w bazie danych ", true));	
	}

	/**
	 * Metoda obs³uguj¹ca ¿¹danie pracownika o zasób przechowywuj¹cy dane o kliencie biura. Przymuje obiekt klasy komunikacyjnej Client
	 * do którego zapisuje znalezione w bazie danych dane. Korzysta z metod klasy DatabaseManager. Tworzy obiekt zwracaj¹cy komunikat o wyniku operacji. 
	 * @param cl obiekt klasy komunikacyjnej, do którego metoda zapisuje czêœæ danych, na których potem operuje
	 * @throws IOException wyj¹tek
	 */
	public void ClientOperatorGet(@NotNull com.Kaczor.Kaluzinski.Kalarus.Communication.Client cl) throws IOException
	{
		com.Kaczor.Kaluzinski.Kalarus.Serwer.Database.Client client = DataBaseManager.ClientManager.getClient(wo.getIdOffice(), cl.getPesel());
		if(client == null)
		{
			stream.writeObject(new WorkerLoginReturn("Klient o podanym peselu nie istnieje!", true));
			return;
		}
		Adresy ad = DataBaseManager.AdresyManager.getAdresy(client.getIdAdresy());
		if(ad == null)
		{
			stream.writeObject(new WorkerLoginReturn("B³¹d odczytu danych!", true));
			return;
		}
		stream.writeObject(new com.Kaczor.Kaluzinski.Kalarus.Communication.Client(client.getIdClient(),client.getName(), client.getSurname(), client.getPhonenumber(), client.getPesel(),
				ad.getCountry(), ad.getCity(), ad.getStreet(), ad.getHomenumber(), ad.getPostdode()));
	}

	/**
	 * Metoda obs³uguj¹ca ¿¹danie aktualizacji danych w obiekcie konkretnego klienta biura. Przyjmuje
	 * obiekt klasy komunikacyjnej Client, w którym zawarty jest pe³en zestaw nowych danych, którymi zostaj¹
	 * nadpisane dane tego klienta biura w bazie danych. Korzysta z metod klasy DatabaseManager.
	 * @param cl obiekt zawieraj¹cy zestaw nowych danych, które mamy przypisaæ do istniej¹cego ju¿ klienta biura
	 * @throws IOException wyj¹tek
	 */
	public void ClientOperatorUpdate(@NotNull com.Kaczor.Kaluzinski.Kalarus.Communication.Client cl) throws IOException
	{
		String buff = checkClientBeforeAdd(cl);
		if(buff!=null)
		{
			stream.writeObject(new WorkerLoginReturn(buff, true));
			return;
		}
		
		com.Kaczor.Kaluzinski.Kalarus.Serwer.Database.Client client = DataBaseManager.ClientManager.getClient(wo.getIdOffice(), cl.getPesel());
		if(client == null)
		{
			stream.writeObject(new WorkerLoginReturn("Client o podanym peselu nie istnieje w bazie danych", true));
			return;
		}
		Adresy ad = DataBaseManager.AdresyManager.getAdresy(client.getIdAdresy());
		if(ad == null)
		{
			stream.writeObject(new WorkerLoginReturn("Wyst¹pi³ b³¹d!", false));
			return;
		}
		ad.setCity(cl.getCity());
		ad.setCountry(cl.getCountry());
		ad.setHomenumber(cl.getHomenumber());
		ad.setPostdode(cl.getPostcode());
		ad.setStreet(cl.getStreet());
		
		client.setName(cl.getName());
		client.setSurname(cl.getSurname());
		client.setPhonenumber(cl.getPhoneNumber());
		if(DataBaseManager.AdresyManager.updateAdresy(ad) && DataBaseManager.ClientManager.updateClient(client))
		{
			stream.writeObject(new WorkerLoginReturn("Zmodyfikowano pomyœlnie!", false));
			return;
		}
		stream.writeObject(new WorkerLoginReturn("Nie uda³o siê zmodyfikowaæ klienta", true));
	}

	/**
	 * Metoda s³u¿¹ca do sprawdzenia wprowadzanych przez u¿ytkownika danych klienta biura. Nak³ada wymóg spe³niania przez te dane
	 * odpowiednich ograniczeñ. Zwraca komunikaty okreœlaj¹ce b³¹d, jeœli wyst¹pi¹ jakieœ nieprawid³owoœci. Wykorzystywana przed ka¿d¹
	 * aktualizacj¹ lub dodaniem klienta biura. Przyjmuje obiekt z danymi klienta biura, które pracownik chce umieœciæ w bazie danych.
	 * @param cl obiekt z danymi klienta biura, które zostan¹ poddane sprawdzeniu
	 * @return komunikat dotycz¹cy nieprawid³owoœci we wprowadzonych danych lub null jeœli jest OK
	 */
	private @Nullable String checkClientBeforeAdd(@NotNull com.Kaczor.Kaluzinski.Kalarus.Communication.Client cl)
	{
		if(cl.getName() == null)
			return "Pole imie nie mo¿e byæ puste!!!";
		if(!(cl.getName().length()>1 && cl.getName().length() < 31))
			return "D³ugoœæ imienia od 2 do 30";
		
		if(cl.getCountry() == null)
			return "Pole kraj nie mo¿e byæ puste";
		if(!(cl.getCountry().length()>1 && cl.getCountry().length()<31))
			return "Pole kraj ma mieæ d³ugoœæ od 2 do 30 znaków";
		
		if(cl.getCity() == null)
			return "Pole miasto nie mo¿e byæ puste";
		if(!(cl.getCountry().length()>1 && cl.getCountry().length()<31))
			return "Pole miasto ma mieæ d³ugoœæ od 2 do 30 znaków";
		
		
		if(cl.getHomenumber() == null)
			return "Pole numer domu nie mo¿e byæ puste";
		if(!(cl.getHomenumber().length()>0 && cl.getHomenumber().length()<20))
			return "Pole numer domu powinno mieæ d³ugoœæ od 1 do 19 znaków";
		
		if(cl.getPesel() == null)
			return "Pole pesel nie mo¿e byæ puste";
		if(!(cl.getPesel().length()>5 && cl.getPesel().length()<20))
			return "Pole pesel powinno mieæ d³ugoœæ od 5 do 19 znaków";
		 
		if(cl.getPhoneNumber() == null)
			return "Pole numer telefonu nie mo¿e byæ puste";
		if(!(cl.getPhoneNumber().length()>5 && cl.getPhoneNumber().length()<20))
			return "Pole numer telefonu powinno mieæ d³ugoœæ od 5 do 19 znaków";
		 
		if(cl.getPostcode() == null)
			return "Pole kod pocztowy nie mo¿e byæ puste";
		if(!(cl.getPostcode().length()>3 && cl.getPostcode().length()<7))
			return "Pole kod pocztowy powinno mieæ d³ugoœæ od 4 do 6 znaków";
		
		if(cl.getStreet() == null)
			return "Pole ulica nie mo¿e byæ puste";
		if(!(cl.getStreet().length()>1 && cl.getStreet().length()<21))
			return "Pole ulica powinno mieæ d³ugoœæ od 2 do 20 znaków";
		
		if(cl.getSurname() == null)
			return "Pole nazwisko nie mo¿e byæ puste";
		if(!(cl.getSurname().length()>3 && cl.getSurname().length()<7))
			return "Pole kod nazwisko powinno mieæ d³ugoœæ od 4 do 6 znaków";
		return null;
	}

	/**
	 * Metoda s³u¿¹ca do sprawdzenia wprowadzanych przez u¿ytkownika danych o wycieczce. Nak³ada wymóg spe³niania przez te dane
	 * odpowiednich ograniczeñ. Zwraca komunikaty okreœlaj¹ce b³¹d, jeœli wyst¹pi¹ jakieœ nieprawid³owoœci. Wykorzystywana przed ka¿d¹
	 * aktualizacj¹ lub dodaniem wycieczki. Przyjmuje obiekt z danymi opisuj¹cymi wycieczkê, któr¹ pracownik chce umieœciæ w bazie danych.
	 * @param tr obiekt z danymi opisuj¹cymi wycieczkê, które zostan¹ poddane sprawdzeniu
	 * @return komunikat dotycz¹cy nieprawid³owoœci we wprowadzonych danych lub null jeœli jest OK
	 */
	private @Nullable String checkTripBeforeAdd(@NotNull Trip tr)
	{
		if(tr.getDate_back() == null || tr.getDate_out() == null || tr.getDate_out().getTime() < new Date().getTime() || tr.getDate_back().getTime() < tr.getDate_out().getTime())
			return "Nie poprawna data";
		if(tr.getMaxguest()<=0)
			return "Nie poprawna iloœæ maksymalna goœci";
		
		if(tr.getName()==null)
			return "Pole nazwa nie mo¿e byæ puste";
		if(!(tr.getName().length()>3 && tr.getName().length()<21))
			return "Pole kod pocztowy powinno mieæ d³ugoœæ od 4 do 20 znaków";
		
		if(tr.getPhonenumber()==null)
			return "Pole numertelefonu nie mo¿e byæ puste";
		if(!(tr.getPhonenumber().length()>3 && tr.getPhonenumber().length()<21))
			return "Numer telefonu powinien mieæ mieæ d³ugoœæ od 4 do 20 znaków";
		if(tr.getPrice()<=0)
			return "Pole cena nie mo¿e mieæ wartoœci mniejszej b¹dz równej zeru";
		if(tr.getStandard()<=0 || tr.getStandard()>=6)
			return "Nie prawid³owy standard";
		return null;
	}

	/**
	 * Metoda s³u¿¹ca do obs³ugi ¿¹dania dodania nowej wycieczki do bazy danych. Przyjmuje obiekt klasy komunikacyjnej Trip z nowym zestawem danych,
	 * który ma zostaæ dodany. Korzysta z metod klasy DatabaseManager w celu umieszczenia danych w bazie danych. Towrzy nowy obiekt klasy komunikacyjnej
	 * opisuj¹cy odpowiednim komunikatem wynik operacji.
	 * @param tr obiekt zawieraj¹cy dane o wycieczce, które chcemy dodaæ do bazy danych
	 * @throws IOException wyj¹tek
	 */
	public void TripOperatorAdd(@NotNull Trip tr) throws IOException
	{
		String buff = checkTripBeforeAdd(tr);
		if(buff!=null)
			stream.writeObject(new WorkerLoginReturn(buff, true));
		if(DataBaseManager.TripManager.addTrip(new com.Kaczor.Kaluzinski.Kalarus.Serwer.Database.Trip(wo.getIdOffice(), tr.getPrice(),
				tr.getDate_out(), tr.getDate_back(), tr.getName(), tr.getPhonenumber(), tr.getMaxguest(), tr.getStandard())))
			stream.writeObject(new WorkerLoginReturn("Dodano  wycieczke!", false));
		else
			stream.writeObject(new WorkerLoginReturn("Nie uda³o siê dodaæ wycieczki!", true));		
		
	}

	/**
	 * Metoda obs³uguje ¿¹danie klienta aplikacji dotycz¹ce zwrócenia zasobu opisuj¹cego wszystkie wycieczki.
	 * Przy pomocy metod z kalsy DatabaseManager pobiera dane z bazy danych i umieszcza je w tablicy obiektów.
	 * @param tr obiekt klasy komunikacyjnej Trip
	 * @throws IOException wyj¹tek
	 */
	public void TripOperatorGetTrips(@NotNull Trip tr) throws IOException
	{
		com.Kaczor.Kaluzinski.Kalarus.Serwer.Database.Trip[] trip = DataBaseManager.TripManager.getAllTrips(wo.getIdOffice(), tr);
		if(trip == null)
		{
			Trip trop = new Trip(true, "Error!");
			trop.setWhatToDo(WhatToDo.TRIPS_ERROR);
			stream.writeObject(trop);
			return;
		}
		for(com.Kaczor.Kaluzinski.Kalarus.Serwer.Database.Trip buff : trip)
			stream.writeObject(new Trip(buff.getIdTrip(), buff.getMaxguests(), buff.getStandard(), buff.getDateout(), buff.getDatein(), buff.getName(), buff.getPhonenumber(), buff.getPrice()));
		
		stream.writeObject(new Trip(true, null));
	}
	
	/**
	 * Metoda obs³uguj¹ca ¿¹danie usuniêcia klienta biura z bazy danych. Przyjmuje obiekt klienta biura, którego pracownik
	 * zamierza usun¹æ. Nastêpnie szuka klienta o takim peselu w bazie danych i usuwa go w przypadku znalezienia. Korzysta z metod klasy
	 * DatabaseManager. Tworzy nowy obiekt klasy komunikacyjnej opisuj¹cy komunikatem wynik przeprowadzonej operacji.
	 * @param cl obiekt klienta biura, którego pracownik chce usun¹æ
	 * @throws IOException wyj¹tek
	 */
	public void ClientOperatorRemove(@NotNull com.Kaczor.Kaluzinski.Kalarus.Communication.Client cl) throws IOException
	{
		if(DataBaseManager.ClientManager.removeClient(wo.getIdOffice(), cl.getPesel()))
			stream.writeObject(new WorkerLoginReturn("Usuniêto klienta!", false));
		else
			stream.writeObject(new WorkerLoginReturn("Nie uda³o siê usun¹æ klienta!", true));
	}
	
	
	
}
