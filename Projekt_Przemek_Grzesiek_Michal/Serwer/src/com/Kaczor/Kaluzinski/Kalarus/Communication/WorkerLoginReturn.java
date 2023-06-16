package com.Kaczor.Kaluzinski.Kalarus.Communication;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Klasa komunikacyjna s�u��ca do zwracania odpowiedzi na prawie ka�d� operacj� ywkonywan� przez pracownika (klienta aplikacji).
 * Korzysta z interfejsu komunikacyjnego.
 */
public class WorkerLoginReturn implements Communication {

	
	private static final long serialVersionUID = 2638900549119802556L;
	private final String message;
	private final boolean iserror;
	private WhatToDo what = WhatToDo.ADD;
	
	/**
	 * Podstawowy konstruktor ustawiaj�cy w nowym obiekcie wiadomo�� ewentualnego b��du i warto�ci true/false w przypadku pojawienia si� b��du.
	 * @param message wiadomo�� ewentualnego b��du
	 * @param iserror true, je�li wyst�pi� b��d
	 */
	public WorkerLoginReturn(@Nullable String message,boolean iserror)
	{
		this.message = message;
		this.iserror = iserror;
	}

	/**
	 * Rozszerzony konstruktor zawieraj�cy dodatkowo parametr okre�laj�cy rodzaj operacji do wykonania przez serwer.
	 * @param message wiadomo�� ewentualnego b��du
	 * @param iserror true, je�li wyst�pi� b��d
	 * @param what operacja do wykonania
	 */
	public WorkerLoginReturn(@Nullable String message,boolean iserror,@NotNull WhatToDo what )
	{
		this.message = message;
		this.iserror = iserror;
		this.what = what;
	}
	
	@Override
	public String getMessage() {
		return this.message;
	}

	@Override
	public boolean isError() {
		return this.iserror;
	}
	@Override
	public WhatToDo getWhatToDo() {
		return what;
	}
	
}
