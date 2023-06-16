package com.Kaczor.Kaluzinski.Kalarus.SerwerConnector;

import static com.Kaczor.Kaluzinski.Kalarus.Klient.Window.showErrorMessage;
import static com.Kaczor.Kaluzinski.Kalarus.Klient.Window.showInfoMessage;

import java.io.IOException;


import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.Kaczor.Kaluzinski.Kalarus.Communication.Aggrement;
import com.Kaczor.Kaluzinski.Kalarus.Communication.Client;
import com.Kaczor.Kaluzinski.Kalarus.Communication.Communication;
import com.Kaczor.Kaluzinski.Kalarus.Communication.Trip;
import com.Kaczor.Kaluzinski.Kalarus.Communication.WhatToDo;
import com.Kaczor.Kaluzinski.Kalarus.Communication.WorkerLogin;
import com.Kaczor.Kaluzinski.Kalarus.Communication.WorkerLoginReturn;

/**
 * Klasa zawieraj�cametody komunikacji klienta z serwerem
 */
public class SerwerConnector {
	private static final Logger logger = Logger.getLogger(SerwerConnector.class);
	private Socket sock;
	private ObjectOutputStream stream;
	private ObjectInputStream streamin;
	public SerwerConnector() throws UnknownHostException, IOException
	{
		sock = new Socket("127.0.0.1", 4021);	
		stream = new ObjectOutputStream(sock.getOutputStream());
		streamin = new ObjectInputStream(sock.getInputStream());
	}
	
	/**
	 * Metoda wysy�a ��danie logowania si� klienta. Zwraca true lub false.
	 * @param nick nazwa pracownika
	 * @param password has�o pracownika
	 * @return true, je�eli uda�o si� zalogowa�, false je�eli nie uda�o si� zalogowa�.
	 */
	public boolean login(@NotNull String nick,@NotNull String password)
	{
		try {
			
			stream.writeObject(new WorkerLogin(nick,password));
			Object obj = streamin.readObject();
			if(obj instanceof Communication)
			{
				if(((Communication)obj).isError())
				{
					showErrorMessage(((Communication)obj).getMessage());
					return false;
				}
				else
				{	
					showInfoMessage(((Communication)obj).getMessage());
					return true;
				}
			}
			else
				return false;
		} catch (IOException e) {
			logger.error("Error!", e);
			return false;
		} catch (ClassNotFoundException e) {
			logger.error("Error!", e);
			return false;
		} 
	}
	
	/**
	 * Metoda wysy�a ��danie do serwera dotycz�ce aktualizacji umowy p�atno�ci klienta biura.
	 * @param ag Pe�ny obiekt z danymi, kt�re maj� zosta� zaktualizowane.
	 * @return true, je�eli operacja zako�czy�a si� powodzeniem
	 */
	public boolean updatePayment(@NotNull Aggrement ag)
	{
		ag.setWhatToDo(WhatToDo.UPDATE);
		try {
			stream.writeObject(ag);
			Object obj = streamin.readObject();
			if(!(obj instanceof Communication))
			{
				showErrorMessage("Nieprawid�owy obiekt!");
				return false;
			}
			Communication com = (Communication)obj;
			if(com.isError())
			{
				showErrorMessage(com.getMessage());
				return false;
			}
			showInfoMessage(com.getMessage());
			return true;
		} catch (IOException | ClassNotFoundException e) {
			logger.error("Error!", e);
		}
		
		return false;
	}
	
	/**
	 * Metoda wysy�a ��danie do serwera o dodanie nowej wycieczki do bazy wycieczek.
	 * @param of Pe�ny obiekt z przygotowanymi danymi, kt�re maj� zosta� dodane do bazy.
	 * @return warto�� true, je�li operacja si� powiod�a
	 */
	public boolean addTrip(@NotNull Trip of)
	{
		
		try {
			of.setWhatToDo(WhatToDo.ADD);
			stream.writeObject(of);
			Object obj = streamin.readObject();
			if(obj instanceof Communication)
			{
				Communication com = (Communication)obj;
				if(com.isError())
				{
					showErrorMessage(com.getMessage());
					return false;
				}
				else 
				{
					showInfoMessage(com.getMessage());
					return true;
				}
				
			}
		} catch (IOException | ClassNotFoundException e) {
			logger.error("Error!", e);
		}
		
		return false;
	}
	
	/**
	 * Metoda wysy�aj�ca ��danie o zwr�cenie informacji o wszystkich wycieczkach. Zwraca tablic� wyciecczek lub null.
	 * @param tr Obiekt klasy zawieraj�cej dane na temat wycieczek
	 * @return tablica wycieczek w przypadku powodzenia operacji lub null w przypadku b��du
	 */
	public @Nullable Trip[] getTrips(@NotNull Trip tr)
	{
		tr.setWhatToDo(WhatToDo.GET_TRIPS);
		List<Trip> triplist = new ArrayList<Trip>();
		try {
			stream.writeObject(tr);
			do {
		
				Object obj = streamin.readObject();
				if(!(obj instanceof Trip))
					return null;
				Trip trip = (Trip)obj;
				if(trip.isError())
					break;
				triplist.add(trip);
				if(trip.getWhatToDo()==WhatToDo.TRIPS_ERROR)
					break;
				
			}while(true);
		} catch (IOException | ClassNotFoundException e) {
			logger.error("Error!", e);
		}
		return triplist.toArray(new Trip[0]);
	}
	
	/**
	 * Metoda wysy�a ��danie o dodanie klienta biura (nie serwera) do bazy danych.
	 * @param cl Pe�ny obiekt z przygotowanymi danymi, kt�re maj� zosta� dodane do bazy.
	 * @return warto�� true, je�li operacja si� powiod�a
	 */
	public boolean addClient(@NotNull Client cl)
	{
		try {
			cl.setWhatToDo(WhatToDo.ADD);
			stream.writeObject(cl);
			Object obj = streamin.readObject();
			if(obj instanceof Communication)
			{
				Communication com = (Communication) obj;
				if(com.isError())
				{
					showErrorMessage(com.getMessage());
					return false;
				}
				else {
					showInfoMessage(com.getMessage());
					return true;
				}
			}
			
		} catch (IOException | ClassNotFoundException e) {
			logger.error("Error!", e);
		}
		
		return false;
	}
	
	/**
	 * Metoda wysy�aj�ca ��danie o aktualizacj� danych klienta biura.
	 * @param cl Pe�ny obiekt z danymi, kt�re maj� zosta� zaktualizowane.
	 * @return warto�� true, je�li operacja powiedzie si�
	 */
	public boolean updateClient(@NotNull Client cl)
	{
		try {
			cl.setWhatToDo(WhatToDo.UPDATE);
			stream.writeObject(cl);
			Object obj = streamin.readObject();
			Communication com = null;
			if(obj instanceof Communication)
			{
				com = (Communication)obj;
				if(com.isError())
				{
					showErrorMessage(com.getMessage());
					return false;
				}
				showInfoMessage(com.getMessage());
				return true;
			}
		} catch (IOException | ClassNotFoundException e) {
			logger.error("Error!", e);
		}
		return false;
	}
	
	/**
	 * Metoda wysy�a ��danie o usuni�cie klienta biura o podanym peselu. Pesele s� unikalne.
	 * @param pesel pesel klienta biura, kt�rego chcemy usun��
	 * @return true, je�li usuni�cie klienta powiedzie si�
	 */
	public boolean removeClient(@NotNull String pesel)
	{
		Client cl = new Client(-1,null, null, null, pesel, null, null, null, null, null, WhatToDo.REMOVE);
		try {
			stream.writeObject(cl);
			Object obj = streamin.readObject();
			Communication com = null;
			if(obj instanceof Communication)
			{
				com = (Communication)obj;
				if(com.isError())
				{
					showErrorMessage(com.getMessage());
					return false;
				}
				showInfoMessage(com.getMessage());
				return true;
			}
		} catch (IOException | ClassNotFoundException e) {
			logger.error("Error!", e);
		}
		return false;
	}
	
	/**
	 * Metoda wysy�a ��danie o zwr�cenie danych klienta biura o podanym peselu. Pesele s� unikalne.
	 * @param pesel pesel klienta biura, o kt�rego dane prosimy
	 * @return obiekt klienta o podanym peselu, je�li taki istnieje lub null w przypadku b��du
	 */
	public @Nullable Client getClient(@NotNull String pesel)
	{
		try {
			stream.writeObject(new Client(-1,null, null, null, pesel, null, null, null, null, null, WhatToDo.GET));
			Object obj = streamin.readObject();
			if(obj instanceof Client &&  !((Communication)obj).isError())
				return (Client)obj;
			showErrorMessage(((Communication)obj).getMessage());
			return null;
		}catch (Exception e) {
			logger.error("Error!", e);
		}
		return null;
	}
	
	/**
	 * Metoda s�u��ca do wylogowania pracownika (klienta aplikacji) z programu.
	 * @return true, je�li uda�o si� wylogowa�
	 */
	public boolean logout()
	{
		try {
			stream.writeObject(new WorkerLogin(null, null));
			Object obj = streamin.readObject();
			if(!(obj instanceof WorkerLoginReturn))
				return false;
			showInfoMessage(((Communication)obj).getMessage());
			return true;
		}catch (Exception e) {
		}
		return false;
	}
	
	/**
	 * Metoda wysy�a ��danie o zwr�cenie um�w wycieczkowych klienta biura.
	 * @param ag Obiekt klasy zawieraj�cy dane o wycieczkach
	 * @return tablica wycieczek klienta biura w przypadku powodzenia operacji lub null w przypadku b��du
	 */
	public @Nullable Aggrement[] getAggrements(@NotNull Aggrement ag)
	{
		ArrayList<Aggrement> list = new ArrayList<Aggrement>();
		
		try {
			ag.setWhatToDo(WhatToDo.GET_TRIPS);
			stream.writeObject(ag);
			do {
				Object obj = streamin.readObject();
				if(!(obj instanceof Aggrement))
					return null;
				Communication com = (Communication) obj;
				if(com.isError() && com.getWhatToDo() == WhatToDo.TRIPS_ERROR)
				{
					list.add((Aggrement)obj);
					return list.toArray(new Aggrement[0]);
				}
				else if(com.isError())
					return list.toArray(new Aggrement[0]);
				list.add((Aggrement)obj);
				
			}while(true);
			
			
		}catch (Exception e) {
			logger.error("Error!", e);
		}
		return null;
	}
	
	/**
	 * Metoda wysy�aj�ca ��danie o dodanie nowej umowy wycieczkowej do bazy danych.
	 * @param ag Pe�ny obiekt z przygotowanymi danymi, kt�re maj� zosta� dodane do bazy
	 * @return true, je�li operacja zako�czy�a si� sukcesem lub false w przypadku b��du
	 */
	public boolean addAggrement(@NotNull Aggrement ag)
	{

		try {
			ag.setWhatToDo(WhatToDo.ADD);
			stream.writeObject(ag);
			
			Object obj = streamin.readObject();
			if(!(obj instanceof Communication))
			{
				showErrorMessage("Nieznany b��d!");
				return false;
			}
			Communication com = (Communication)obj;
			if(com.isError())
				showErrorMessage(com.getMessage());
			else
				showInfoMessage("Dodano umow� klientowi!!!");
			
			
			
		}catch (Exception e) {
			logger.error("Error!", e);
			return false;
		}
		return true;
		
	}
	
	/**
	 * Metoda zamykaj�ca po��czenie pracownika (klienta aplikacji) z serwerem
	 * @return true, je�li operacja zako�czcy�a si� sukcesem
	 */
	public boolean close()
	{
		try {
			this.sock.close();
		} catch (IOException e) {
			logger.error("Error!", e);
			return false;
		}
		return true;
	}
	
	/**
	 * Metoda inicjalizuje po��czenie pracownika (klienta aplikacji) z serwerem.
	 * @return true, je�li operacja zako�czcy�a si� sukcesem
	 */
	public boolean open()
	{
		try {
			this.sock = new Socket("127.0.0.1", 4021);
		} catch (IOException e) {
			logger.error("Error!", e); 
			return false;
		};
		return true;
	}
	
}
