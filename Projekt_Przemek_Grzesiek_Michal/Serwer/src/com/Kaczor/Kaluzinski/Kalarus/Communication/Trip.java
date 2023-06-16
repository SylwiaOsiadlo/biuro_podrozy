package com.Kaczor.Kaluzinski.Kalarus.Communication;

import java.sql.Date;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Klasa komunikacyjna zarz¹dzaj¹ca wycieczkami oferowanymi przez biuro podró¿y.
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
	 * Podstawowy konstruktor ustawiaj¹cy wszystkie informacje w nowym obiekcie o dodawanej wycieczce w nowo tworzonym obiekcie
	 * @param idTrip id wycieczki
	 * @param maxguest maksymalna iloœæ goœci
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
	 * Konstruktor zawieraj¹cy poza wszystkimi polami klasy dodatkowy parametr okreœlaj¹cy
	 * jaka operacja ma zostaæ wykonana na obiekcie przez serwer.
	 * @param idTrip id wycieczki
	 * @param maxguest maksymalna iloœæ goœci
	 * @param standard standard wycieczki
	 * @param date_out data wyjazdu
	 * @param date_back data powrotu
	 * @param name nazwa wycieczki
	 * @param phonenumber numer telefonu
	 * @param price cena wycieczki
	 * @param what okreœla jaka operacja ma zostaæ wykonana na obiekcie
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
	 * Konstruktor zwrotny, który jest u¿ywany w przypadku wyst¹pienia b³êdu.
	 * @param isError parametr okreœla czy wyst¹pi³ b³¹d
	 * @param message wiadomoœæ ewentualnego b³êdu
	 */
	public Trip(boolean isError,@Nullable String message)
	{
		this.isError = isError;
		this.message = message;
	}

	/**
	 * Getter zwracaj¹cy id wycieczki
	 * @return id wycieczki
	 */
	public int getIdTrip() {
		return idTrip;
	}

	/**
	 * Getter zwracaj¹cy maksymaln¹ iloœæ goœci wycieczki
	 * @return maksymalna liczba goœci
	 */
	public int getMaxguest() {
		return maxguest;
	}

	/**
	 * Getter zwracaj¹cy standard wycieczki
	 * @return standard
	 */
	public int getStandard() {
		return standard;
	}

	/**
	 * Getter zwracaj¹cy datê wyjazdu
	 * @return data wyjazdu
	 */
	public @Nullable java.sql.Date getDate_out() {
		return date_out;
	}

	/**
	 * Getter zwracaj¹cy datê przyjazdu
	 * @return data powrotu
	 */
	public @Nullable java.sql.Date getDate_back() {
		return date_back;
	}

	/**
	 * Getter zwracaj¹cy nazwê wycieczki
	 * @return nazwa wycieczki
	 */
	public @Nullable String getName() {
		return name;
	}

	/**
	 * Getter zwracaj¹cy numer telefonu
	 * @return numer telefonu
	 */
	public @Nullable String getPhonenumber() {
		return phonenumber;
	}

	/**
	 * Getter zwracaj¹cy cenê wycieczki
	 * @return cena wycieczki
	 */
	public float getPrice() {
		return price;
	}

	/**
	 * Nadpisana metoda z interfejsu zwracaj¹ca wiadomoœæ b³êdu
	 * @return wiadomoœæ b³êdu
	 */
	@Override
	public String getMessage() {
		return this.message;
	}

	/**
	 * Nadpisana metoda z interfejsu sprawdzaj¹ca czy wyst¹pi³ b³¹d.
	 * @return true jeœli wyst¹pi³ b³¹d
	 */
	@Override
	public boolean isError() {
		return this.isError;
	}

	/**
	 * Nadpisana metoda z interfejsu zwracaj¹ca rodzaj operacji jaka ma zostaæ wykonana na obiekcie.
	 * @return true jeœli wyst¹pi³ b³¹d
	 */
	@Override
	public WhatToDo getWhatToDo() {
		return this.what;
	}
	
	/**
	 * Metoda ustawiaj¹ca rodzaj operacji do wykonania na obiekcie
	 * @param what rodzaj operacji do wykonania
	 */
	public void setWhatToDo(@NotNull WhatToDo what)
	{
		this.what = what;
	}

}
