package com.Kaczor.Kaluzinski.Kalarus.Communication;

import org.jetbrains.annotations.NotNull;

/**
 * Klasa komunikacyjna s逝蕨ca do zarz鉅zania logowaniem pracownika (klienta aplikacji).
 * Korzysta z interfejsu komunikacyjnego.
 */
public class WorkerLogin implements Communication{

	private static final long serialVersionUID = 107332579837710941L;
	private final String nick, password;

	/**
	 * Podstawowy konstruktor ustawiaj鉍y w nowym obiekcie login i has這 pracownika w obiekcie.
	 * @param nick login pracownika
	 * @param password has這 pracownika
	 */
	public WorkerLogin(@NotNull String nick,@NotNull String password)
	{
		this.nick = nick;
		this.password = password;
	}
	
	/**
	 * Getter zwracaj鉍y login pracownika.
	 * @return login pracownika
	 */
	public @NotNull String getNick() {
		return nick;
	}
	
	/**
	 * Getter zwracaj鉍y has這 pracownika.
	 * @return ha這 pracownika
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
