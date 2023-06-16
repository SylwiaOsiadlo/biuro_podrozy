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
 * dane na temat klientów biura. Tworzy takie encje jak: id klienta, imie, nazwisko
 * pesel, nr telefonu, id adresu, id biura. Zawiera sekwencjê automatycznie inkrementuj¹c¹
 * id klienta.
 */
@Entity(name = "KLIENCI")
@Table(name = "Klienci")
public class Client {
	
	@Id
	@GeneratedValue(generator = "sequence-generator-Klienci")
	@GenericGenerator(
				name = "sequence-generator-Klienci",
				strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
		    	parameters = {
		    			@Parameter(name = "sequence_name", value = "KlienciSeq"),
		    	        @Parameter(name = "initial_value", value = "1"),
		    	        @Parameter(name = "increment_size", value = "1")
				}
			)
	@Column(name = "idKlienci", unique = false)
	private int idClient;
	
	@Column(name = "Imie", nullable = false)
	private String name;
	
	@Column(name = "Nazwisko", nullable = false)
	private String surname;
	
	@Column(name = "Pesel", nullable = false)
	private String pesel;
	
	@Column(name = "Telefon", nullable = false)
	private String phonenumber;
	
	@Column(name = "idAdresy", nullable = false)
	private int idAdresy;
	
	@Column(name = "idBiura", nullable = false)
	private int idBiura;

	/**
	 * Podstawowy konstruktor, s³u¿¹cy do tworzenia obiektu klasy.
	 * @param name imiê klienta
	 * @param surname nazwisko klienta
	 * @param pesel pesel klienta
	 * @param phonenumber numer telefonu klienta
	 */
	public Client(@NotNull String name,@NotNull String surname,@NotNull String pesel,@NotNull String phonenumber) {
		this.name = name;
		this.surname = surname;
		this.pesel = pesel;
		this.phonenumber = phonenumber;
	}
	
	@SuppressWarnings("unused")
	private Client()
	{}

	public @NotNull String getName() {
		return name;
	}

	public @NotNull String getSurname() {
		return surname;
	}

	public @NotNull String getPesel() {
		return pesel;
	}

	public @NotNull String getPhonenumber() {
		return phonenumber;
	}

	public void setIdClient(int idClient) {
		this.idClient = idClient;
	}

	public void setName(@NotNull String name) {
		this.name = name;
	}

	public void setSurname(@NotNull String surname) {
		this.surname = surname;
	}

	public void setPesel(@NotNull String pesel) {
		this.pesel = pesel;
	}

	public void setPhonenumber(@NotNull String phonenumber) {
		this.phonenumber = phonenumber;
	}

	public int getIdAdresy() {
		return idAdresy;
	}

	public void setIdAdresy(int idAdresy) {
		this.idAdresy = idAdresy;
	}

	public int getIdBiura() {
		return idBiura;
	}

	public void setIdBiura(int idBiura) {
		this.idBiura = idBiura;
	}

	public int getIdClient() {
		return idClient;
	}


	
}
