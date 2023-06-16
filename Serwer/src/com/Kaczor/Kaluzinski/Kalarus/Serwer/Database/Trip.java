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
 * dane na temat wycieczek oferowanych przez biuro podró¿y. Tworzy encje takie jak: id wycieczki,
 * id biura, cena, data wyjazdu, data powrotu, nazwa wycieczki, telefon, maksymalna liczba uczestników,
 * standard. Zawiera tak¿e sekwencjê automatycznie inkrementuj¹c¹ id wycieczki.
 */
@Entity(name = "TRIP")
@Table(name = "Wycieczki")
public class Trip {

	@Id
	@GeneratedValue(generator = "sequence-generator-Trip")
	@GenericGenerator(
				name = "sequence-generator-Trip",
				strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
		    	parameters = {
		    			@Parameter(name = "sequence_name", value = "Wycieczkiseq"),
		    	        @Parameter(name = "initial_value", value = "1"),
		    	        @Parameter(name = "increment_size", value = "1")
				}
			)
	@Column(name = "idWycieczki", unique = true)
	private int idTrip;
	
	@Column(name = "idBiura", nullable = false)
	private int idOffice;
	
	@Column(name = "Cena", nullable = false)
	private float price;
	
	@Column(name = "DataWyjazdu", nullable = false)
	private java.sql.Date dateout;
	
	@Column(name = "DataPowrotu", nullable = false)
	private java.sql.Date datein;
	
	@Column(name = "Nazwa", nullable = false)
	private String name;
	
	@Column(name = "Telefon", nullable = false)
	private String phonenumber;
	
	@Column(name = "MaxOsob", nullable = false)
	private int maxguests;
	
	@Column(name = "Standard", nullable = false)
	private int standard;
	
	/**
	 * Podstawowy konstruktor zawieraj¹cy wszystkie pola, s³u¿¹cy do tworzenia obiektu klasy.
	 * @param idOffice id biura podró¿y
	 * @param price cena wycieczki
	 * @param dateout data wyjazdu
	 * @param datein data powrotu
	 * @param name nazwa wycieczki
	 * @param phonenumber numer telefonu
	 * @param maxguests maksymalna liczba uczestników
	 * @param standard standard wycieczki
	 */
	public Trip(int idOffice, float price,@NotNull Date dateout,@NotNull Date datein,@NotNull String name,@NotNull String phonenumber, int maxguests,
			int standard) {
		this.idOffice = idOffice;
		this.price = price;
		this.dateout = dateout;
		this.datein = datein;
		this.name = name;
		this.phonenumber = phonenumber;
		this.maxguests = maxguests;
		this.standard = standard;
	}
	@SuppressWarnings("unused")
	private Trip() {};

	public int getIdTrip() {
		return idTrip;
	}

	public int getIdOffice() {
		return idOffice;
	}

	public float getPrice() {
		return price;
	}
	
	public @NotNull java.sql.Date getDateout() {
		return dateout;
	}
	
	public @NotNull java.sql.Date getDatein() {
		return datein;
	}
	
	public @NotNull String getName() {
		return name;
	}
	
	public String getPhonenumber() {
		return phonenumber;
	}
	
	public int getMaxguests() {
		return maxguests;
	}
	
	public int getStandard() {
		return standard;
	}
	
	public void setIdTrip(int idTrip) {
		this.idTrip = idTrip;
	}
	
	public void setIdOffice(int idOffice) {
		this.idOffice = idOffice;
	}
	
	public void setPrice(float price) {
		this.price = price;
	}
	
	public void setDateout(@NotNull java.sql.Date dateout) {
		this.dateout = dateout;
	}
	
	public void setDatein(@NotNull java.sql.Date datein) {
		this.datein = datein;
	}
	
	public void setName(@NotNull String name) {
		this.name = name;
	}
    
	public void setPhonenumber(@NotNull String phonenumber) {
		this.phonenumber = phonenumber;
	}
	
	public void setMaxguests(int maxguests) {
		this.maxguests = maxguests;
	}
	
	public void setStandard(int standard) {
		this.standard = standard;
	}	
}
