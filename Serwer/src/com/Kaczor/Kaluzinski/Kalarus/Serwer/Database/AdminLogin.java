package com.Kaczor.Kaluzinski.Kalarus.Serwer.Database;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.jetbrains.annotations.NotNull;

/**
 * Klasa u¿ywana przez framework Hibernate. S³u¿y do tworzenia tabeli przechowywuj¹cej
 * dane logowania admina. Tworzy encje loginu oraz has³a.
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
	 * Podstawowy konstruktor zawieraj¹cy wszystkie pola, s³u¿¹cy do tworzenia obiektu klasy.
	 * @param nick login admina
	 * @param password has³o admina
	 */
	public AdminLogin(@NotNull String nick,@NotNull String password) {
		super();
		this.nick = nick;
		this.password = password;
	}
	
	@SuppressWarnings("unused")
	private AdminLogin() {}
	
	/**
	 * Getter zwracaj¹cy login admina.
	 * @return login admina
	 */
	public @NotNull String getNick() {
		return nick;
	}

	/**
	 * Getter zwracaj¹cy has³o admina
	 * @return has³o admina
	 */
	public @NotNull String getPassword() {
		return password;
	}

	/**
	 * Setter ustawiaj¹cy has³o admina
	 * @param password has³o admina
	 */
	public void setPassword(@NotNull String password) {
		this.password = password;
	}
	
}
