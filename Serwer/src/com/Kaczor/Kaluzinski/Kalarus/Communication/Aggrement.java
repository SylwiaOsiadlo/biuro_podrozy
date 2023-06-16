package com.Kaczor.Kaluzinski.Kalarus.Communication;

import java.sql.Date;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Klasa komunikacyjna rozszerzaj¹ca klasê Trip. Zarz¹dza umowami p³atniczymi klientów biura.
 * Korzysta z interfejsu komunikacyjnego. 
 * @see Trip
 */
public class Aggrement extends Trip {
	private static final long serialVersionUID = 4369543172961302359L;
	private int idUmowy, idClient, guestcount,idaggrement;
	private java.sql.Date aggrementdate;
	private String payment_type, payment_status;
	
	/**
	 * Konstruktor zawieraj¹cy poza wszystkimi polami klasy dodatkowy parametr okreœlaj¹cy
	 * jaka operacja ma zostaæ wykonana na obiekcie przez serwer.
	 * @param idAggrement id umowy
	 * @param idTrip id wycieczki
	 * @param maxguest maksymalna liczba uczestników
	 * @param standard standard wycieczki
	 * @param idUmowy id umowy
	 * @param guestcount liczba goœci
	 * @param idClient id klienta banku
	 * @param date_out data wyjazdu
	 * @param date_back data powrotu
	 * @param aggrementdate data zawarcia umowy
	 * @param name nazwa wycieczki
	 * @param phonenumber numer telefonu
	 * @param price cena wycieczki
	 * @param payment_type rodzaj p³atnoœci
	 * @param payment_status status p³atnoœci
	 * @param what operacja jak¹ serwer ma wykonaæ na obiekcie
	 */
	public Aggrement(int idAggrement, int idTrip, int maxguest, int standard, int idUmowy, int guestcount, int idClient,@Nullable Date date_out,@Nullable Date date_back,@Nullable Date aggrementdate,@Nullable String name,
			@Nullable String phonenumber, float price,@Nullable String payment_type,@Nullable String payment_status,@NotNull WhatToDo what) {
		super(idTrip, maxguest, standard, date_out, date_back, name, phonenumber, price, what);
		this.aggrementdate = aggrementdate;
		this.idUmowy = idUmowy;
		this.idClient = idClient;
		this.guestcount = guestcount;
		this.payment_status = payment_status;
		this.payment_type = payment_type;
		this.idaggrement = idAggrement;
	}

	/**
	 * Podstawowy konstruktor ustawiaj¹cy wszystkie informacje w nowym obiekcie o dodawanej umowie p³atniczej klienta biura 
	 * w nowo tworzonym obiekcie.
	 * @param idAggrement id umowy
	 * @param idTrip id wycieczki
	 * @param maxguest maksymalna liczba uczestników
	 * @param standard standard wycieczki
	 * @param idUmowy id umowy
	 * @param guestcount liczba goœci
	 * @param idClient id klienta banku
	 * @param date_out data wyjazdu
	 * @param date_back data powrotu
	 * @param aggrementdate data zawarcia umowy
	 * @param name nazwa wycieczki
	 * @param phonenumber numer telefonu
	 * @param price cena wycieczki
	 * @param payment_type rodzaj p³atnoœci
	 * @param payment_status status p³atnoœci
	 */
	public Aggrement(int idAggrement,int idTrip, int maxguest, int standard, int idUmowy, int guestcount, int idClient,@Nullable Date date_out,@Nullable Date date_back,@Nullable Date aggrementdate,@Nullable String name,
			@Nullable String phonenumber, float price,@Nullable String payment_type,@Nullable String payment_status) {
		super(idTrip, maxguest, standard, date_out, date_back, name, phonenumber, price);
		this.aggrementdate = aggrementdate;
		this.idUmowy = idUmowy;
		this.idClient = idClient;
		this.guestcount = guestcount;
		this.payment_status = payment_status;
		this.payment_type = payment_type;
		this.idaggrement = idAggrement;
	}

	/**
	 * Konstruktor zwrotny, który jest u¿ywany w przypadku wyst¹pienia b³êdu.
	 * @param isError parametr okreœla czy wyst¹pi³ b³¹d
	 * @param message wiadomoœæ ewentualnego b³êdu
	 */
	public Aggrement(boolean isError,@Nullable String message)
	{
		super(isError, message);
	}

	public int getIdaggrement() {
		return idaggrement;
	}

	public int getIdUmowy() {
		return idUmowy;
	}

	public int getIdClient() {
		return idClient;
	}

	public int getGuestcount() {
		return guestcount;
	}

	public @Nullable java.sql.Date getAggrementdate() {
		return aggrementdate;
	}

	public @Nullable String getPayment_type() {
		return payment_type;
	}

	public @Nullable String getPayment_status() {
		return payment_status;
	}
	
}
