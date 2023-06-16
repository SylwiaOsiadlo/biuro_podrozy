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
 * Klasa obrazuje pojedyncze po��czenie klienta aplikacji z serwerem.
 * Zawiera metody odpowiedzialne za zarz�dzanie operacjami, o wykonanie kt�rych prosi
 * klient aplikacji. W skr�cie zawiera wszystkie implementacje reakcji na ��dania wykorzystywanych w aplikacji.
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
	 * Konstruktor, kt�ry s�u�y do stworzenia nowych strumieni wej�ciowych i wyj�ciowych dla
	 * nowo po��czonego klienta. Jako parametr przyjmuje gniazdo tego klienta.
	 * @param sock gniazdo nowo pod��czonego klienta
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
	 * Metoda zawieraj�ca niesko�czon� p�tl� s�u��c� do aktywnego oczekiwania na ��dania od klienta aplikacji.
	 * Podejmuje odpowiednie dzia�ania w zale�no�ci od rodzaju otrzymanego ��dania.
	 * Sprawdza, czy obiekt o kt�ry prosi klient jest obiektem jednej z klas komunikacyjnych.
	 * W przypadku b��du przerywa p�tl�.
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
								stream.writeObject(new WorkerLoginReturn("Nick musi mie� conajmniej 3 znaki!!!", true));
							if(ClientOperator.isExist(buff))
								stream.writeObject(new WorkerLoginReturn("U�ytkownik o takim nicku jest ju� zalogowany do systemu!", true));
							else
								workerLogin((WorkerLogin)obj);
						}
						else
						{
							stream.writeObject(new WorkerLoginReturn("Wylogowano pomy�lnie", false));
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
	 * pracownika znajduj�cym si� w obecnie przetwarzanym obiekcie klasy Worker.
	 * @param nick wpisany przez u�ytkownika nick
	 * @return true, je�li nick jest poprawny lub false w przeciwnym wypadku
	 */
	boolean isNickCorrect(@NotNull String nick)
	{
		return  wo!=null?wo.getNick().equals(nick):false;
	}

	/**
	 * Metoda s�u��ca do obs�u�enia ��dania klienta aplikacji dotycz�cego aktualizacji danych
	 * w obiekcie zawieraj�cym umow� p�atnicz� klienta biura. Aktualizuje dane w bazie danych. 
	 * Wykorzystuje do tego funkcje z klasy DataBaseManager.
	 * @param obj obiekt klasy Agreement, kt�ry chcemy zaktualizowa�
	 * @throws IOException wyj�tek
	 */
	private void AggrementOperatorUpdate(@NotNull Aggrement obj) throws IOException
	{
		
		if(!obj.getPayment_type().equalsIgnoreCase("Przelew"))
		{
			stream.writeObject(new WorkerLoginReturn("Mo�na zmienic tylko stan p�atno�ci w przelewu", true));
			return;
		}
		com.Kaczor.Kaluzinski.Kalarus.Serwer.Database.Payment pay = DataBaseManager.PaymentManager.getPaymentByAggrement(obj.getIdaggrement());
		
		if(pay == null || !(obj.getPayment_status().equals("Zap�acono") || obj.getPayment_status().equals("Niezap�acono")))
		{
			stream.writeObject(new WorkerLoginReturn("Wyst�pi� b��d!", true));
			return;
		}
		
		pay.setState(obj.getPayment_status());
		if(!DataBaseManager.PaymentManager.updatePayment(pay))
		{
			stream.writeObject(new WorkerLoginReturn("Wyst�pi� b��d!", true));
			return;
		}
		
		stream.writeObject(new WorkerLoginReturn("Zmieniono stan p�atno�ci!", false));
		
	}

	/**
	 * Metoda ustawia warto�� zmiennej odpowiedzialnej za niesko�czcone dzia�anie p�tli
	 * w metodzie ActiveWaiting na true lub false. S�u�y do zatrzymywania (false) aktywnego oczekiwania
	 * na ��dania klienta aplikacji lub jego wznawianie (true)
	 * @param value warto�� true lub false decyduj�ca czy chcemy zatrzyma� czy wznowi� aktywne oczekiwanie
	 */
	public void setStop(boolean value)
	{
		stoper = value;
	}

	/**
	 * Metoda s�u��ca do przej�cia w tryb oczekiwania na zatrzymanie.
	 * wykonuje niesko�czon� p�tl� nie robi�c� kompletnie nic poza dzia�aniem.
	 */
	public void waitToStop()
	{
		while(isworking);
	}
	
	/**
	 * Metoda wymusza na serwerze zamkni�cie po��czenia z klientem aplikacji.
	 * Zamyka gniazdo socket oraz strumienie wej�ciowe i wyj�ciowe klienta aplikacji.
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
	 * Metoda obs�uguj�ca ��danie klienta aplikacji dotycz�ce zwr�cenia danych na temat
	 * umowy p�atniczej klienta biura. Zapisuje ��dane dane do strumieni. Pobiera dane z bazy danych przy pomocy metod
	 * z klasy DatabaseManager.
	 * @param ag obiekt klasy komunikacyjnej Agreement
	 * @throws IOException wyj�tek
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
	 * Metoda s�u��ca do obs�u�enia ��dania klienta aplikacji dotycz�cego dodania nowej umowy p�atniczzej.
	 * Tworzy nowe obiekty oraz dodaje ich zawarto�� do bazy danych. Wykorzystuje metody z klasy DatabaseManager.
	 * @param ag obiekt klasy komunikacyjnej Agreement z danymi do dodania
	 * @throws IOException wyj�tek
	 */
	public void AggrementOperatoAdd(@NotNull com.Kaczor.Kaluzinski.Kalarus.Communication.Aggrement ag) throws IOException
	{
		if(ag.getPrice()<= 0)
		{
			stream.writeObject(new WorkerLoginReturn("Koszt nie mo�e by� mniejszy lub r�wny zeru!", true));
			return;
		}
		if(ag.getGuestcount()<=0)
		{
			stream.writeObject(new WorkerLoginReturn("Liczba go�ci nie mo�e by� mniejsza b�dz r�wna zeru!", true));
			return;
		}
		com.Kaczor.Kaluzinski.Kalarus.Serwer.Database.Aggrement aggrement = new com.Kaczor.Kaluzinski.Kalarus.Serwer.Database.Aggrement(ag.getIdClient(), ag.getIdTrip(), ag.getGuestcount(), ag.getAggrementdate());
		com.Kaczor.Kaluzinski.Kalarus.Serwer.Database.Trip tr = DataBaseManager.TripManager.getTrip(aggrement.getIdTrip());

		if(tr == null)
		{
			stream.writeObject(new WorkerLoginReturn("Wyst�pi� b��d!!!", true));
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
			stream.writeObject(new WorkerLoginReturn("Wyst�pi� b��d1", true));
			return;
		}
		if(!DataBaseManager.AgrrementManager.addAggrement(aggrement))
		{
			tr.setMaxguests(max);
			DataBaseManager.TripManager.updateTrip(tr);
			stream.writeObject(new WorkerLoginReturn("Wyst�pi� b��d2", true));
			return;
		}
		if(!DataBaseManager.PaymentManager.addPayment(new Payment(aggrement.getIdAggrement(), ag.getPayment_type(), ag.getPayment_status(), ag.getPrice())))
		{
			tr.setMaxguests(max);
			DataBaseManager.TripManager.updateTrip(tr);
			DataBaseManager.AgrrementManager.removeAggrement(aggrement.getIdAggrement());
			stream.writeObject(new WorkerLoginReturn("Wyst�pi� b��d3", true));
			return;
		}
		
		stream.writeObject(new WorkerLoginReturn("Dodano umow�", false));	
	}

	/**
	 * Metoda obs�uguj�ca logowanie pracownika. Przyjmuje obiekt klasy komunikacyjnej,
	 * w kt�rym s� zawarte wpisane przez u�ytkownika dane logowania. Nast�pnie szuka pracownika
	 * o takim loginie i porownuje podane has�o z przypisanym mu w bazie danych.
	 * Jako odpowiedz tworzy obiekt klasy komunikacyjnej WorkerLoginRetrun przyjmuj�cy wiadomo��
	 * opisuj�c� przebieg operacji i warto�� true lub false okre�laj�cy czy wyst�pi� b��d.
	 * @param l obiekt klasy komunikacyjnej posiadaj�cy zestaw loginu i has�a wpisane przez pracownika
	 * @throws IOException wyj�tek
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
			stream.writeObject(new WorkerLoginReturn("Zalogowano pomy�lnie!", false));
		else
			stream.writeObject(new WorkerLoginReturn("Nie poprawne has�o", true));
	}

	/**
	 * Metoda obs�uguje ��danie dodania nowego klienta biura do bazy. Przyjmuje obiekt klasy komunikacyjnej Client
	 * kt�ry przechowuje zestaw danych osoby, kt�r� u�ytkownik chce doda�.
	 * Tworzy nowe obiekty oraz dodaje ich zawarto�� do bazy danych. Wykorzystuje metody z klasy DatabaseManager.
	 * W przypadku powowdzenia lub b��du tworzy obiekt zwracaj�cy informacje o statusie operacji.
	 * @param cl obiekt klasy komunikacyjnej z zestawem danych klienta biura, kt�rego chcemy doda�
	 * @throws IOException wyj�tek
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
	 * Metoda obs�uguj�ca ��danie pracownika o zas�b przechowywuj�cy dane o kliencie biura. Przymuje obiekt klasy komunikacyjnej Client
	 * do kt�rego zapisuje znalezione w bazie danych dane. Korzysta z metod klasy DatabaseManager. Tworzy obiekt zwracaj�cy komunikat o wyniku operacji. 
	 * @param cl obiekt klasy komunikacyjnej, do kt�rego metoda zapisuje cz�� danych, na kt�rych potem operuje
	 * @throws IOException wyj�tek
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
			stream.writeObject(new WorkerLoginReturn("B��d odczytu danych!", true));
			return;
		}
		stream.writeObject(new com.Kaczor.Kaluzinski.Kalarus.Communication.Client(client.getIdClient(),client.getName(), client.getSurname(), client.getPhonenumber(), client.getPesel(),
				ad.getCountry(), ad.getCity(), ad.getStreet(), ad.getHomenumber(), ad.getPostdode()));
	}

	/**
	 * Metoda obs�uguj�ca ��danie aktualizacji danych w obiekcie konkretnego klienta biura. Przyjmuje
	 * obiekt klasy komunikacyjnej Client, w kt�rym zawarty jest pe�en zestaw nowych danych, kt�rymi zostaj�
	 * nadpisane dane tego klienta biura w bazie danych. Korzysta z metod klasy DatabaseManager.
	 * @param cl obiekt zawieraj�cy zestaw nowych danych, kt�re mamy przypisa� do istniej�cego ju� klienta biura
	 * @throws IOException wyj�tek
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
			stream.writeObject(new WorkerLoginReturn("Wyst�pi� b��d!", false));
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
			stream.writeObject(new WorkerLoginReturn("Zmodyfikowano pomy�lnie!", false));
			return;
		}
		stream.writeObject(new WorkerLoginReturn("Nie uda�o si� zmodyfikowa� klienta", true));
	}

	/**
	 * Metoda s�u��ca do sprawdzenia wprowadzanych przez u�ytkownika danych klienta biura. Nak�ada wym�g spe�niania przez te dane
	 * odpowiednich ogranicze�. Zwraca komunikaty okre�laj�ce b��d, je�li wyst�pi� jakie� nieprawid�owo�ci. Wykorzystywana przed ka�d�
	 * aktualizacj� lub dodaniem klienta biura. Przyjmuje obiekt z danymi klienta biura, kt�re pracownik chce umie�ci� w bazie danych.
	 * @param cl obiekt z danymi klienta biura, kt�re zostan� poddane sprawdzeniu
	 * @return komunikat dotycz�cy nieprawid�owo�ci we wprowadzonych danych lub null je�li jest OK
	 */
	private @Nullable String checkClientBeforeAdd(@NotNull com.Kaczor.Kaluzinski.Kalarus.Communication.Client cl)
	{
		if(cl.getName() == null)
			return "Pole imie nie mo�e by� puste!!!";
		if(!(cl.getName().length()>1 && cl.getName().length() < 31))
			return "D�ugo�� imienia od 2 do 30";
		
		if(cl.getCountry() == null)
			return "Pole kraj nie mo�e by� puste";
		if(!(cl.getCountry().length()>1 && cl.getCountry().length()<31))
			return "Pole kraj ma mie� d�ugo�� od 2 do 30 znak�w";
		
		if(cl.getCity() == null)
			return "Pole miasto nie mo�e by� puste";
		if(!(cl.getCountry().length()>1 && cl.getCountry().length()<31))
			return "Pole miasto ma mie� d�ugo�� od 2 do 30 znak�w";
		
		
		if(cl.getHomenumber() == null)
			return "Pole numer domu nie mo�e by� puste";
		if(!(cl.getHomenumber().length()>0 && cl.getHomenumber().length()<20))
			return "Pole numer domu powinno mie� d�ugo�� od 1 do 19 znak�w";
		
		if(cl.getPesel() == null)
			return "Pole pesel nie mo�e by� puste";
		if(!(cl.getPesel().length()>5 && cl.getPesel().length()<20))
			return "Pole pesel powinno mie� d�ugo�� od 5 do 19 znak�w";
		 
		if(cl.getPhoneNumber() == null)
			return "Pole numer telefonu nie mo�e by� puste";
		if(!(cl.getPhoneNumber().length()>5 && cl.getPhoneNumber().length()<20))
			return "Pole numer telefonu powinno mie� d�ugo�� od 5 do 19 znak�w";
		 
		if(cl.getPostcode() == null)
			return "Pole kod pocztowy nie mo�e by� puste";
		if(!(cl.getPostcode().length()>3 && cl.getPostcode().length()<7))
			return "Pole kod pocztowy powinno mie� d�ugo�� od 4 do 6 znak�w";
		
		if(cl.getStreet() == null)
			return "Pole ulica nie mo�e by� puste";
		if(!(cl.getStreet().length()>1 && cl.getStreet().length()<21))
			return "Pole ulica powinno mie� d�ugo�� od 2 do 20 znak�w";
		
		if(cl.getSurname() == null)
			return "Pole nazwisko nie mo�e by� puste";
		if(!(cl.getSurname().length()>3 && cl.getSurname().length()<7))
			return "Pole kod nazwisko powinno mie� d�ugo�� od 4 do 6 znak�w";
		return null;
	}

	/**
	 * Metoda s�u��ca do sprawdzenia wprowadzanych przez u�ytkownika danych o wycieczce. Nak�ada wym�g spe�niania przez te dane
	 * odpowiednich ogranicze�. Zwraca komunikaty okre�laj�ce b��d, je�li wyst�pi� jakie� nieprawid�owo�ci. Wykorzystywana przed ka�d�
	 * aktualizacj� lub dodaniem wycieczki. Przyjmuje obiekt z danymi opisuj�cymi wycieczk�, kt�r� pracownik chce umie�ci� w bazie danych.
	 * @param tr obiekt z danymi opisuj�cymi wycieczk�, kt�re zostan� poddane sprawdzeniu
	 * @return komunikat dotycz�cy nieprawid�owo�ci we wprowadzonych danych lub null je�li jest OK
	 */
	private @Nullable String checkTripBeforeAdd(@NotNull Trip tr)
	{
		if(tr.getDate_back() == null || tr.getDate_out() == null || tr.getDate_out().getTime() < new Date().getTime() || tr.getDate_back().getTime() < tr.getDate_out().getTime())
			return "Nie poprawna data";
		if(tr.getMaxguest()<=0)
			return "Nie poprawna ilo�� maksymalna go�ci";
		
		if(tr.getName()==null)
			return "Pole nazwa nie mo�e by� puste";
		if(!(tr.getName().length()>3 && tr.getName().length()<21))
			return "Pole kod pocztowy powinno mie� d�ugo�� od 4 do 20 znak�w";
		
		if(tr.getPhonenumber()==null)
			return "Pole numertelefonu nie mo�e by� puste";
		if(!(tr.getPhonenumber().length()>3 && tr.getPhonenumber().length()<21))
			return "Numer telefonu powinien mie� mie� d�ugo�� od 4 do 20 znak�w";
		if(tr.getPrice()<=0)
			return "Pole cena nie mo�e mie� warto�ci mniejszej b�dz r�wnej zeru";
		if(tr.getStandard()<=0 || tr.getStandard()>=6)
			return "Nie prawid�owy standard";
		return null;
	}

	/**
	 * Metoda s�u��ca do obs�ugi ��dania dodania nowej wycieczki do bazy danych. Przyjmuje obiekt klasy komunikacyjnej Trip z nowym zestawem danych,
	 * kt�ry ma zosta� dodany. Korzysta z metod klasy DatabaseManager w celu umieszczenia danych w bazie danych. Towrzy nowy obiekt klasy komunikacyjnej
	 * opisuj�cy odpowiednim komunikatem wynik operacji.
	 * @param tr obiekt zawieraj�cy dane o wycieczce, kt�re chcemy doda� do bazy danych
	 * @throws IOException wyj�tek
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
			stream.writeObject(new WorkerLoginReturn("Nie uda�o si� doda� wycieczki!", true));		
		
	}

	/**
	 * Metoda obs�uguje ��danie klienta aplikacji dotycz�ce zwr�cenia zasobu opisuj�cego wszystkie wycieczki.
	 * Przy pomocy metod z kalsy DatabaseManager pobiera dane z bazy danych i umieszcza je w tablicy obiekt�w.
	 * @param tr obiekt klasy komunikacyjnej Trip
	 * @throws IOException wyj�tek
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
	 * Metoda obs�uguj�ca ��danie usuni�cia klienta biura z bazy danych. Przyjmuje obiekt klienta biura, kt�rego pracownik
	 * zamierza usun��. Nast�pnie szuka klienta o takim peselu w bazie danych i usuwa go w przypadku znalezienia. Korzysta z metod klasy
	 * DatabaseManager. Tworzy nowy obiekt klasy komunikacyjnej opisuj�cy komunikatem wynik przeprowadzonej operacji.
	 * @param cl obiekt klienta biura, kt�rego pracownik chce usun��
	 * @throws IOException wyj�tek
	 */
	public void ClientOperatorRemove(@NotNull com.Kaczor.Kaluzinski.Kalarus.Communication.Client cl) throws IOException
	{
		if(DataBaseManager.ClientManager.removeClient(wo.getIdOffice(), cl.getPesel()))
			stream.writeObject(new WorkerLoginReturn("Usuni�to klienta!", false));
		else
			stream.writeObject(new WorkerLoginReturn("Nie uda�o si� usun�� klienta!", true));
	}
	
	
	
}
