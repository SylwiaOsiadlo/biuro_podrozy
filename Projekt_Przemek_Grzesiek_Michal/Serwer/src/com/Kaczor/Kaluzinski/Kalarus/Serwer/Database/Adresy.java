package com.Kaczor.Kaluzinski.Kalarus.Serwer.Database;

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
 * dane adresowe klientów aplikacji (pracowników) oraz klientów biura. Tworzy encje id adresu,
 * kraj, miejscowoœæ, numer domu, kod pocztowy oraz ulica. Zawiera sekwencjê automatycznie generuj¹c¹
 * id adresów.
 */
@Entity(name = "ADRESY")
@Table(name = "ADRESY")
public class Adresy {
	@Id
	@GeneratedValue(generator = "sequence-generator-Adresy")
	@GenericGenerator(
				name = "sequence-generator-Adresy",
				strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
		    	parameters = {
		    			@Parameter(name = "sequence_name", value = "AdresySeq"),
		    	        @Parameter(name = "initial_value", value = "1"),
		    	        @Parameter(name = "increment_size", value = "1")
				}
			)
	@Column(name = "idAdresy")
	private int idAdresy;
	
	@Column(name = "Kraj", nullable = false)
	private String country;
	
	@Column(name = "Miejscowosc", nullable = false)
	private String city;
	
	@Column(name = "NumerDomu", nullable = false)
	private String homenumber;
	
	@Column(name = "KodPocztowy", nullable = false)
	private String postdode;
	
	@Column(name = "Ulica", nullable = false)
	private String street;

	/**
	 * Podstawowy konstruktor zawieraj¹cy wszystkie pola, s³u¿¹cy do tworzenia obiektu klasy.
	 * @param country kraj adresu
	 * @param city miasto adresu
	 * @param homenumber numer domu adresu
	 * @param postdode kod pocztowy adresu
	 * @param street ulica adresu
	 */
	public Adresy(@NotNull String country,@NotNull String city,@NotNull String homenumber,@NotNull String postdode,@NotNull String street) {
		this.country = country;
		this.city = city;
		this.homenumber = homenumber;
		this.postdode = postdode;
		this.street = street;
	}
	
	@SuppressWarnings("unused")
	private Adresy(){}
	
	public int getIdAdresy() {
		return idAdresy;
	}
	
	public @NotNull String getCountry() {
		return country;
	}
	
	public @NotNull String getCity() {
		return city;
	}
	
	public @NotNull String getHomenumber() {
		return homenumber;
	}
	
	public @NotNull String getPostdode() {
		return postdode;
	}

	public @NotNull String getStreet() {
		return street;
	}

	public void setIdAdresy(int idAdresy) {
		this.idAdresy = idAdresy;
	}

	public void setCountry(@NotNull String country) {
		this.country = country;
	}

	public void setHomenumber(@NotNull String homenumber) {
		this.homenumber = homenumber;
	}

	public void setPostdode(@NotNull String postdode) {
		this.postdode = postdode;
	}
	
	public void setStreet(@NotNull String street) {
		this.street = street;
	}
	
	public void setCity(@NotNull String city) {
		this.city = city;
	}
}
