package com.Kaczor.Kaluzinski.Kalarus.Serwer.Database;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.jetbrains.annotations.NotNull;

/**
 * Klasa u�ywana przez framework Hibernate. S�u�y do tworzenia tabeli przechowywuj�cej
 * dane logowania admina. Tworzy encje loginu oraz has�a.
 */
@Entity
@Table(name = "panel")
public class AdminLogin {
	@Id
	@Column(name = "nick")
	private String nick;
	@Column(name = "password")
	private String password;

	/**
	 * Podstawowy konstruktor zawieraj�cy wszystkie pola, s�u��cy do tworzenia obiektu klasy.
	 * @param nick login admina
	 * @param password has�o admina
	 */
	public AdminLogin(@NotNull String nick,@NotNull String password) {
		super();
		this.nick = nick;
		this.password = password;
	}
	
	@SuppressWarnings("unused")
	private AdminLogin() {}
	
	/**
	 * Getter zwracaj�cy login admina.
	 * @return login admina
	 */
	public @NotNull String getNick() {
		return nick;
	}

	/**
	 * Getter zwracaj�cy has�o admina
	 * @return has�o admina
	 */
	public @NotNull String getPassword() {
		return password;
	}

	/**
	 * Setter ustawiaj�cy has�o admina
	 * @param password has�o admina
	 */
	public void setPassword(@NotNull String password) {
		this.password = password;
	}
	
}
