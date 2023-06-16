package com.Kaczor.Kaluzinski.Kalarus.Communication;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Klasa komunikacyjna s³u¿¹ca do zwracania odpowiedzi na prawie ka¿d¹ operacjê ywkonywan¹ przez pracownika (klienta aplikacji).
 * Korzysta z interfejsu komunikacyjnego.
 */
public class WorkerLoginReturn implements Communication {

	
	private static final long serialVersionUID = 2638900549119802556L;
	private final String message;
	private final boolean iserror;
	private WhatToDo what = WhatToDo.ADD;
	
	/**
	 * Podstawowy konstruktor ustawiaj¹cy w nowym obiekcie wiadomoœæ ewentualnego b³êdu i wartoœci true/false w przypadku pojawienia siê b³êdu.
	 * @param message wiadomoœæ ewentualnego b³êdu
	 * @param iserror true, jeœli wyst¹pi³ b³¹d
	 */
	public WorkerLoginReturn(@Nullable String message,boolean iserror)
	{
		this.message = message;
		this.iserror = iserror;
	}

	/**
	 * Rozszerzony konstruktor zawieraj¹cy dodatkowo parametr okreœlaj¹cy rodzaj operacji do wykonania przez serwer.
	 * @param message wiadomoœæ ewentualnego b³êdu
	 * @param iserror true, jeœli wyst¹pi³ b³¹d
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
