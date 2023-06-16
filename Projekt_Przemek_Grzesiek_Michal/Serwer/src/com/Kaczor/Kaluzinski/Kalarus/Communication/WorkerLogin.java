package com.Kaczor.Kaluzinski.Kalarus.Communication;

import org.jetbrains.annotations.NotNull;

/**
 * Klasa komunikacyjna s�u��ca do zarz�dzania logowaniem pracownika (klienta aplikacji).
 * Korzysta z interfejsu komunikacyjnego.
 */
public class WorkerLogin implements Communication{

	private static final long serialVersionUID = 107332579837710941L;
	private final String nick, password;

	/**
	 * Podstawowy konstruktor ustawiaj�cy w nowym obiekcie login i has�o pracownika w obiekcie.
	 * @param nick login pracownika
	 * @param password has�o pracownika
	 */
	public WorkerLogin(@NotNull String nick,@NotNull String password)
	{
		this.nick = nick;
		this.password = password;
	}
	
	/**
	 * Getter zwracaj�cy login pracownika.
	 * @return login pracownika
	 */
	public @NotNull String getNick() {
		return nick;
	}
	
	/**
	 * Getter zwracaj�cy has�o pracownika.
	 * @return ha�o pracownika
	 */
	public @NotNull String getPassword() {
		return password;
	}
	
	@Override
	public String getMessage() {
		return "";
	}
	
	@Override
	public boolean isError() {
		return false;
	}
	
	@Override
	public WhatToDo getWhatToDo() {
		return WhatToDo.ADD;
	}
}
