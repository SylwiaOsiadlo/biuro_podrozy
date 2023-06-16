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
 * Klasa zawieraj¹cametody komunikacji klienta z serwerem
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
	 * Metoda wysy³a ¿¹danie logowania siê klienta. Zwraca true lub false.
	 * @param nick nazwa pracownika
	 * @param password has³o pracownika
	 * @return true, je¿eli uda³o siê zalogowaæ, false je¿eli nie uda³o siê zalogowaæ.
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
	 * Metoda wysy³a ¿¹danie do serwera dotycz¹ce aktualizacji umowy p³atnoœci klienta biura.
	 * @param ag Pe³ny obiekt z danymi, które maj¹ zostaæ zaktualizowane.
	 * @return true, je¿eli operacja zakoñczy³a siê powodzeniem
	 */
	public boolean updatePayment(@NotNull Aggrement ag)
	{
		ag.setWhatToDo(WhatToDo.UPDATE);
		try {
			stream.writeObject(ag);
			Object obj = streamin.readObject();
			if(!(obj instanceof Communication))
			{
				showErrorMessage("Nieprawid³owy obiekt!");
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
	 * Metoda wysy³a ¿¹danie do serwera o dodanie nowej wycieczki do bazy wycieczek.
	 * @param of Pe³ny obiekt z przygotowanymi danymi, które maj¹ zostaæ dodane do bazy.
	 * @return wartoœæ true, jeœli operacja siê powiod³a
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
	 * Metoda wysy³aj¹ca ¿¹danie o zwrócenie informacji o wszystkich wycieczkach. Zwraca tablicê wyciecczek lub null.
	 * @param tr Obiekt klasy zawieraj¹cej dane na temat wycieczek
	 * @return tablica wycieczek w przypadku powodzenia operacji lub null w przypadku b³êdu
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
	 * Metoda wysy³a ¿¹danie o dodanie klienta biura (nie serwera) do bazy danych.
	 * @param cl Pe³ny obiekt z przygotowanymi danymi, które maj¹ zostaæ dodane do bazy.
	 * @return wartoœæ true, jeœli operacja siê powiod³a
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
	 * Metoda wysy³aj¹ca ¿¹danie o aktualizacjê danych klienta biura.
	 * @param cl Pe³ny obiekt z danymi, które maj¹ zostaæ zaktualizowane.
	 * @return wartoœæ true, jeœli operacja powiedzie siê
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
	 * Metoda wysy³a ¿¹danie o usuniêcie klienta biura o podanym peselu. Pesele s¹ unikalne.
	 * @param pesel pesel klienta biura, którego chcemy usun¹æ
	 * @return true, jeœli usuniêcie klienta powiedzie siê
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
	 * Metoda wysy³a ¿¹danie o zwrócenie danych klienta biura o podanym peselu. Pesele s¹ unikalne.
	 * @param pesel pesel klienta biura, o którego dane prosimy
	 * @return obiekt klienta o podanym peselu, jeœli taki istnieje lub null w przypadku b³êdu
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
	 * Metoda s³u¿¹ca do wylogowania pracownika (klienta aplikacji) z programu.
	 * @return true, jeœli uda³o siê wylogowaæ
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
	 * Metoda wysy³a ¿¹danie o zwrócenie umów wycieczkowych klienta biura.
	 * @param ag Obiekt klasy zawieraj¹cy dane o wycieczkach
	 * @return tablica wycieczek klienta biura w przypadku powodzenia operacji lub null w przypadku b³êdu
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
	 * Metoda wysy³aj¹ca ¿¹danie o dodanie nowej umowy wycieczkowej do bazy danych.
	 * @param ag Pe³ny obiekt z przygotowanymi danymi, które maj¹ zostaæ dodane do bazy
	 * @return true, jeœli operacja zakoñczy³a siê sukcesem lub false w przypadku b³êdu
	 */
	public boolean addAggrement(@NotNull Aggrement ag)
	{

		try {
			ag.setWhatToDo(WhatToDo.ADD);
			stream.writeObject(ag);
			
			Object obj = streamin.readObject();
			if(!(obj instanceof Communication))
			{
				showErrorMessage("Nieznany b³¹d!");
				return false;
			}
			Communication com = (Communication)obj;
			if(com.isError())
				showErrorMessage(com.getMessage());
			else
				showInfoMessage("Dodano umowê klientowi!!!");
			
			
			
		}catch (Exception e) {
			logger.error("Error!", e);
			return false;
		}
		return true;
		
	}
	
	/**
	 * Metoda zamykaj¹ca po³¹czenie pracownika (klienta aplikacji) z serwerem
	 * @return true, jeœli operacja zakoñczcy³a siê sukcesem
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
	 * Metoda inicjalizuje po³¹czenie pracownika (klienta aplikacji) z serwerem.
	 * @return true, jeœli operacja zakoñczcy³a siê sukcesem
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
