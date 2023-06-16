package com.Kaczor.Kaluzinski.Kalarus.Communication;

import java.io.Serializable;

import org.jetbrains.annotations.NotNull;

/**
 * Interfejs z kt�ego korzystaj� wszystkie klasy komunikacyjne.
 * Posiada deklaracje trzech metod, z kt�rych b�d� korzysta� inne klasy.
 * S�u�� one do zwracania wiadomo�ci b��du, sprawdzania czy wyst�pi� b��d oraz
 * zwracania rodzaju operacji jaki ma zosta� wykonany na obiekcie.
 */
public interface Communication extends Serializable{

	public String getMessage();

	public boolean isError();

	public @NotNull WhatToDo getWhatToDo();
}
