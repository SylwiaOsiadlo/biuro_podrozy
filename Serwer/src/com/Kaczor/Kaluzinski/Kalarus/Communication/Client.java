package com.Kaczor.Kaluzinski.Kalarus.Communication;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Client implements Communication {
	/**
	 * Klasa komunikacyjna s³u¿¹ca do przesy³ania danych zwi¹zanych z klientem biura (nie klientem aplikacji/pracownikiem)
	 */
	private static final long serialVersionUID = 272484732295243662L;
	private String name,surname, phoneNumber, pesel,
	country, city, street, homenumber, postcode,message;
	private WhatToDo what;
	private int idClient;
	private boolean isError = false;

	/**
	 * Podstawowy konstruktor ustawiaj¹cy wszystkie informacje w nowym obiekcie o dodawanym kliencie biura w nowo tworzonym obiekcie
	 * @param idClient id klienta
	 * @param name imie klienta
	 * @param surname nazwisko klienta
	 * @param phoneNumber telefon klienta
	 * @param pesel pesel klienta
	 * @param country kraj klienta
	 * @param city miasto klienta
	 * @param street ulica klienta
	 * @param homenumber numer domu klienta
	 * @param postcode kod pocztowy klienta
	 */
	public Client(int idClient,@Nullable String name,@Nullable String surname,@Nullable String phoneNumber,@Nullable String pesel,@Nullable String country,@Nullable String city,
			@Nullable String street,@Nullable String homenumber,@Nullable String postcode) {
		this.name = name;
		this.surname = surname;
		this.phoneNumber = phoneNumber;
		this.pesel = pesel;
		this.country = country;
		this.city = city;
		this.street = street;
		this.homenumber = homenumber;
		this.postcode = postcode;
		this.idClient = idClient;
	}

	/**
	 * Konstruktor zawieraj¹cy poza wszystkimi polami klasy dodatkowy parametr okreœlaj¹cy
	 * jaka operacja ma zostaæ wykonana na obiekcie przez serwer.
	 * @param idClient id klienta
	 * @param name imie klienta
	 * @param surname nazwisko klienta
	 * @param phoneNumber telefon klienta
	 * @param pesel pesel klienta
	 * @param country kraj klienta
	 * @param city miasto klienta
	 * @param street ulica klienta
	 * @param homenumber numer domu klienta
	 * @param postcode kod pocztowy klienta
	 * @param what okreœla jaka operacja ma zostaæ wykonana na obiekcie
	 */
	public Client(int idClient,@Nullable String name,@Nullable String surname,@Nullable String phoneNumber,@NotNull String pesel,@Nullable String country,@Nullable String city,
			@Nullable String street,@Nullable String homenumber,@Nullable String postcode,@NotNull WhatToDo what) {
		this.name = name;
		this.surname = surname;
		this.phoneNumber = phoneNumber;
		this.pesel = pesel;
		this.country = country;
		this.city = city;
		this.street = street;
		this.homenumber = homenumber;
		this.postcode = postcode;
		this.what = what;
		this.idClient = idClient;
	}

	/**
	 * Konstruktor zwrotny, który jest u¿ywany w przypadku wyst¹pienia b³êdu.
	 * @param isError parametr okreœla czy wyst¹pi³ b³¹d
	 * @param message wiadomoœæ ewentualnego b³êdu
	 */
	public Client(boolean isError,@Nullable String message) {
		this.message = message;
		this.isError = isError;
	}

	
	public @Nullable String getName() {
		return name;
	}

	public @Nullable String getSurname() {
		return surname;
	}

	public @Nullable String getPhoneNumber() {
		return phoneNumber;
	}

	public @Nullable String getPesel() {
		return pesel;
	}

	public @Nullable String getCountry() {
		return country;
	}

	public @Nullable String getCity() {
		return city;
	}

	public @Nullable String getStreet() {
		return street;
	}

	public @Nullable String getHomenumber() {
		return homenumber;
	}

	public @Nullable String getPostcode() {
		return postcode;
	}
	
	@Override
	public @Nullable String getMessage() {
		return this.message;
	}

	public int getIdClient() {
		return idClient;
	}

	/**
	 * Ustawia wiadomoœæ b³êdu
	 * @param message Wiadomoœæ b³êdu
	 */
	public void setMessage(@Nullable String message) {
		this.message = message;
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
	 * Nadpisana metoda z interfejsu zwracaj¹ca rodzaj operacji jaki ma zostaæ wykonany na obiekcie.
	 * @return rodzaj operacji do wykonania na obiekcie
	 */
	@Override
	public @NotNull WhatToDo getWhatToDo() {
		// TODO Auto-generated method stub
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
