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
 * Klasa u�ywana przez framework Hibernate. S�u�y do tworzenia tabeli przechowywuj�cej
 * dane na temat p�atno�ci klient�w biura. Tworzy takie encje jak: id p�atno�ci, id umowy,
 * typ p�atno�ci, stan p�atno�ci, ca�kowity koszt. Zawiera tak�e sekwencj� automatycznie
 * inkrementuj�c� id p�atno�ci.
 */
@Entity(name = "PLATNOSCI")
@Table(name = "Platnosci")
public class Payment {

	@Id
	@Column(name = "idPlatnosci",unique = true)
	@GeneratedValue(generator = "sequence-generator-Platnosci")
	@GenericGenerator(
				name = "sequence-generator-Platnosci",
				strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
		    	parameters = {
		    			@Parameter(name = "sequence_name", value = "Platnosciseq"),
		    	        @Parameter(name = "initial_value", value = "1"),
		    	        @Parameter(name = "increment_size", value = "1")
				}
			)
	private int idPayment;
	
	@Column(name = "idUmowy",nullable = false)
	private int idaggrement;
	
	@Column(name = "TypPlatnosci", nullable = false)
	private String type;
	
	@Column(name = "StanPlatnosci", nullable = false)
	private String state;
	
	@Column(name = "CalkowityKoszt", nullable = false)
	private float total_cost;

	/**
	* Podstawowy konstruktor s�u��cy do ustawiania warto�ci dla p�l klasy.
	 * @param idaggrement id umowy
	 * @param type typ p�atno�ci
	 * @param state stan p�atno�ci
	 * @param total_cost ca�kowity koszt
	 */
	public Payment(int idaggrement,@NotNull String type,@NotNull String state, float total_cost) {
		this.idaggrement = idaggrement;
		this.type = type;
		this.state = state;
		this.total_cost = total_cost;
	}
	
	@SuppressWarnings("unused")
	private Payment() {};

	public int getIdPayment() {
		return idPayment;
	}

	public int getIdaggrement() {
		return idaggrement;
	}

	public @NotNull String getType() {
		return type;
	}

	public @NotNull String getState() {
		return state;
	}

	public float getTotal_cost() {
		return total_cost;
	}
	
	public void setIdPayment(int idPayment) {
		this.idPayment = idPayment;
	}
	
	public void setIdaggrement(int idaggrement) {
		this.idaggrement = idaggrement;
	}
	
	public void setState(@NotNull String state) {
		this.state = state;
	}

	public void setTotal_cost(float total_cost) {
		this.total_cost = total_cost;
	}
}
