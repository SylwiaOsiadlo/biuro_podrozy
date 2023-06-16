package com.Kaczor.Kaluzinski.Kalarus.Communication;

import java.sql.Date;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Klasa komunikacyjna zarz�dzaj�ca wycieczkami oferowanymi przez biuro podr�y.
 * Korzysta z interfejsu komunikacyjnego. 
 */
public class Trip implements Communication {

	private static final long serialVersionUID = 3707264398926554289L;
	private int idTrip, maxguest, standard;
	private java.sql.Date date_out, date_back;
	private String name,phonenumber,message;
	private float price;
	private WhatToDo what;
	private boolean isError = false;

	/**
	 * Podstawowy konstruktor ustawiaj�cy wszystkie informacje w nowym obiekcie o dodawanej wycieczce w nowo tworzonym obiekcie
	 * @param idTrip id wycieczki
	 * @param maxguest maksymalna ilo�� go�ci
	 * @param standard standard wycieczki
	 * @param date_out data wyjazdu
	 * @param date_back data powrotu
	 * @param name nazwa wycieczki
	 * @param phonenumber numer telefonu
	 * @param price cena wycieczki
	 */
	public Trip(int idTrip, int maxguest, int standard,@Nullable Date date_out,@Nullable Date date_back,@Nullable String name,@Nullable String phonenumber,
			float price) {
		super();
		this.idTrip = idTrip;
		this.maxguest = maxguest;
		this.standard = standard;
		this.date_out = date_out;
		this.date_back = date_back;
		this.name = name;
		this.phonenumber = phonenumber;
		this.price = price;
	}

	/**
	 * Konstruktor zawieraj�cy poza wszystkimi polami klasy dodatkowy parametr okre�laj�cy
	 * jaka operacja ma zosta� wykonana na obiekcie przez serwer.
	 * @param idTrip id wycieczki
	 * @param maxguest maksymalna ilo�� go�ci
	 * @param standard standard wycieczki
	 * @param date_out data wyjazdu
	 * @param date_back data powrotu
	 * @param name nazwa wycieczki
	 * @param phonenumber numer telefonu
	 * @param price cena wycieczki
	 * @param what okre�la jaka operacja ma zosta� wykonana na obiekcie
	 */
	public Trip(int idTrip, int maxguest, int standard, Date date_out, Date date_back, String name, String phonenumber,
			float price, WhatToDo what) {
		super();
		this.idTrip = idTrip;
		this.maxguest = maxguest;
		this.standard = standard;
		this.date_out = date_out;
		this.date_back = date_back;
		this.name = name;
		this.phonenumber = phonenumber;
		this.price = price;
		this.what = what;
	}

	/**
	 * Konstruktor zwrotny, kt�ry jest u�ywany w przypadku wyst�pienia b��du.
	 * @param isError parametr okre�la czy wyst�pi� b��d
	 * @param message wiadomo�� ewentualnego b��du
	 */
	public Trip(boolean isError,@Nullable String message)
	{
		this.isError = isError;
		this.message = message;
	}

	/**
	 * Getter zwracaj�cy id wycieczki
	 * @return id wycieczki
	 */
	public int getIdTrip() {
		return idTrip;
	}

	/**
	 * Getter zwracaj�cy maksymaln� ilo�� go�ci wycieczki
	 * @return maksymalna liczba go�ci
	 */
	public int getMaxguest() {
		return maxguest;
	}

	/**
	 * Getter zwracaj�cy standard wycieczki
	 * @return standard
	 */
	public int getStandard() {
		return standard;
	}

	/**
	 * Getter zwracaj�cy dat� wyjazdu
	 * @return data wyjazdu
	 */
	public @Nullable java.sql.Date getDate_out() {
		return date_out;
	}

	/**
	 * Getter zwracaj�cy dat� przyjazdu
	 * @return data powrotu
	 */
	public @Nullable java.sql.Date getDate_back() {
		return date_back;
	}

	/**
	 * Getter zwracaj�cy nazw� wycieczki
	 * @return nazwa wycieczki
	 */
	public @Nullable String getName() {
		return name;
	}

	/**
	 * Getter zwracaj�cy numer telefonu
	 * @return numer telefonu
	 */
	public @Nullable String getPhonenumber() {
		return phonenumber;
	}

	/**
	 * Getter zwracaj�cy cen� wycieczki
	 * @return cena wycieczki
	 */
	public float getPrice() {
		return price;
	}

	/**
	 * Nadpisana metoda z interfejsu zwracaj�ca wiadomo�� b��du
	 * @return wiadomo�� b��du
	 */
	@Override
	public String getMessage() {
		return this.message;
	}

	/**
	 * Nadpisana metoda z interfejsu sprawdzaj�ca czy wyst�pi� b��d.
	 * @return true je�li wyst�pi� b��d
	 */
	@Override
	public boolean isError() {
		return this.isError;
	}

	/**
	 * Nadpisana metoda z interfejsu zwracaj�ca rodzaj operacji jaka ma zosta� wykonana na obiekcie.
	 * @return true je�li wyst�pi� b��d
	 */
	@Override
	public WhatToDo getWhatToDo() {
		return this.what;
	}
	
	/**
	 * Metoda ustawiaj�ca rodzaj operacji do wykonania na obiekcie
	 * @param what rodzaj operacji do wykonania
	 */
	public void setWhatToDo(@NotNull WhatToDo what)
	{
		this.what = what;
	}

}
