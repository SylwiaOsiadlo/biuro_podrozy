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
 * dane na temat biur podró¿y. Tworzy takie encje jak: id biura, id adresu, nazwa, nip.
 * Zawiera sekwencjê automatycznie inkrementuj¹c¹ id biur.
 */
@Entity(name = "BIURA")
@Table(name = "Biura")
public class Office {

	@Id
	@GeneratedValue(generator = "sequence-generator-Office")
	@GenericGenerator(
				name = "sequence-generator-Office",
				strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
		    	parameters = {
		    			@Parameter(name = "sequence_name", value = "BiuraSeq"),
		    	        @Parameter(name = "initial_value", value = "1"),
		    	        @Parameter(name = "increment_size", value = "1")
				}
			)
	@Column(name = "idBiura")
	private int idOffice;
	
	@Column(name = "idAdresy", nullable = false)
	private int idAddres;
	
	@Column(name = "Nazwa", nullable = false)
	private String name;
	
	@Column(name = "NIP", nullable = false)
	private int nip;

	/**
	 * Podstawowy konstruktor zawieraj¹cy wszystkie pola, s³u¿¹cy do tworzenia obiektu klasy.
	 * @param idAddres id adresu biura
	 * @param name nazwa biura
	 * @param nip nip biura
	 */
	public Office(int idAddres,@NotNull String name, int nip) {
		this.idAddres = idAddres;
		this.name = name;
		this.nip = nip;
	}
	@SuppressWarnings("unused")
	private Office() {};

	public int getIdOffice() {
		return idOffice;
	}

	public int getIdAddres() {
		return idAddres;
	}

	public @NotNull String getName() {
		return name;
	}

	public int getNIP() {
		return this.nip;
	}

	public void setIdOffice(int idOffice) {
		this.idOffice = idOffice;
	}

	public void setIdAddres(int idAddres) {
		this.idAddres = idAddres;
	}

	public void setName(@NotNull String name) {
		this.name = name;
	}

	public void setNIP(int nIP) {
		this.nip = nIP;
	}
}
