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
 * dane dotycz¹ce pracowników biur podró¿y. Tworzy takie encje jak: id pracownika, id biura,
 * id adresu, imie, nazwisko, pesel, login, has³o. Zawiera tak¿e sekwencjê do automatycznego
 * inkrementowania id pracownika.
 */
@Entity(name = "WORKER")
@Table(name = "Pracownicy")
public class Worker {
	
	@Id
	@GeneratedValue(generator = "sequence-generator-Worker")
	@GenericGenerator(
				name = "sequence-generator-Worker",
				strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
		    	parameters = {
		    			@Parameter(name = "sequence_name", value = "Pracownikseq"),
		    	        @Parameter(name = "initial_value", value = "1"),
		    	        @Parameter(name = "increment_size", value = "1")
				}
			)
	@Column(name = "idPracownicy", unique = true)
	private int idWorker;
	
	@Column(name = "idBiura", nullable = false)
	private int idOffice;
	
	@Column(name = "idAdresy", nullable = false)
	private int idAdres;
	
	@Column(name = "Imie", nullable = false)
	private String name;
	
	@Column(name = "Nazwisko", nullable = false)
	private String surname;
	
	@Column(name = "Pesel", nullable = false)
	private String pesel;
	
	@Column(name = "Nick", nullable = false)
	private String nick;
	
	@Column(name = "Haslo", nullable = false)
	private String password;

	/**
	 * Podstawowy konstruktor zawieraj¹cy wszystkie pola, s³u¿¹cy do tworzenia obiektu klasy.
	 * @param idOffice id biura podró¿y
	 * @param idAdres id adresu pracownika
	 * @param name imiê pracownika
	 * @param surname nazwisko pracownika
	 * @param pesel pesel pracownika
	 * @param nick login pracownika
	 * @param password has³o pracownika
	 */
	public Worker(int idOffice, int idAdres,@NotNull String name,@NotNull String surname,@NotNull String pesel,@NotNull String nick,@NotNull String password) {
		this.idOffice = idOffice;
		this.idAdres = idAdres;
		this.name = name;
		this.surname = surname;
		this.pesel = pesel;
		this.nick = nick;
		this.password = password;
	}
	@SuppressWarnings("unused")
	private Worker() {};
	
	public @NotNull String getPassword() {
		return password;
	}
	
	public void setPassword(@NotNull String password) {
		this.password = password;
	}
	
	public int getIdWorker() {
		return idWorker;
	}
	
	public int getIdOffice() {
		return idOffice;
	}
	
	public int getIdAdres() {
		return idAdres;
	}
	
	public @NotNull String getName() {
		return name;
	}
	
	public @NotNull String getSurname() {
		return surname;
	}
	
	public @NotNull String getPesel() {
		return pesel;
	}
	
	public @NotNull String getNick() {
		return nick;
		
	}
	
	public void setIdWorker(int idWorker) {
		this.idWorker = idWorker;
	}
	
	public void setIdOffice(int idOffice) {
		this.idOffice = idOffice;
	}
	
	public void setIdAdres(int idAdres) {
		this.idAdres = idAdres;
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
	
	public void setNick(@NotNull String nick) {
		this.nick = nick;
	}
	
}
