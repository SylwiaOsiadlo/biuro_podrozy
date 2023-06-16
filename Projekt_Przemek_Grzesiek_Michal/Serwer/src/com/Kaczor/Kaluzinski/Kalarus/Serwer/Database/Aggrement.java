package com.Kaczor.Kaluzinski.Kalarus.Serwer.Database;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.jetbrains.annotations.NotNull;

/**
 * Klasa u¿ywana przez framework Hibernate. S³u¿y do tworzenia tabeli przechowywuj¹cej
 * umowy p³atnicze klientów biura. Tworzy takie encje jak: id umowy, id klienta, id wycieczki,
 * data umowy, liczba uczestników. Zawiera sekwencjê automatycznie generuj¹c¹ id umowy.
 */
@Entity(name = "UMOWY")
@Table(name = "Umowy")
public class Aggrement  {


	@Id
	@GeneratedValue(generator = "sequence-generator-Umowy")
	@GenericGenerator(
				name = "sequence-generator-Umowy",
				strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
		    	parameters = {
		    			@Parameter(name = "sequence_name", value = "Umowyseq"),
		    	        @Parameter(name = "initial_value", value = "1"),
		    	        @Parameter(name = "increment_size", value = "1")
				}
			)
	@Column(name = "idUmowy", unique = true)
	private int idAggrement;
	@Column(name = "idKlienci", nullable = false)
	private int idClient;
	@Column(name = "idWycieczki", nullable =  false)
	private int idTrip;
	@Column(name = "DataUmowy", nullable = false)
	private java.sql.Date aggrement_date;
	@Column(name = "LiczbaOsob", nullable = false)
	private int guestcount;
	
	/**
	 * Podstawowy konstruktor zawieraj¹cy wszystkie pola, s³u¿¹cy do tworzenia obiektu klasy.
	 * @param idclient id klienta
	 * @param idTrip id wycieczki
	 * @param guestcount liczba uczestników
	 * @param aggrement_date data zawarcia umowy
	 */
	public Aggrement(int idclient,int idTrip,int guestcount,@NotNull Date aggrement_date) {
		this.idClient = idclient;
		this.idTrip = idTrip;
		this.guestcount = guestcount;
		this.aggrement_date = aggrement_date;
	}
	
	@SuppressWarnings("unused")
	private Aggrement() {};
	protected int getIdClient() {
		return idClient;
	}

	public int getIdAggrement() {
		return idAggrement;
	}

	protected void setIdAggrement(int idAggrement) {
		this.idAggrement = idAggrement;
	}

	public int getGuestcount() {
		return guestcount;
	}

	public @NotNull java.sql.Date getAggrement_date() {
		return aggrement_date;
	}

	public void setAggrement_date(@NotNull java.sql.Date aggrement_date) {
		this.aggrement_date = aggrement_date;
	}

	public void setGuestcount(int guestcount) {
		this.guestcount = guestcount;
	}

	protected void setIdClient(int idClient) {
		this.idClient = idClient;
	}

	public void setIdClient(@NotNull Client client)
	{
		this.idClient = client.getIdClient();
	}

	public int getIdTrip() {
		return idTrip;
	}

	protected void setIdTrip(int idTrip) {
		this.idTrip = idTrip;
	}

	public void setIdTrip(@NotNull Trip of)
	{
		this.idTrip = of.getIdTrip();
	}
}
