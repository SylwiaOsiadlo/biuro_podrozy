package com.Kaczor.Kaluzinski.Kalarus.Communication;

import java.io.Serializable;

import org.jetbrains.annotations.NotNull;

/**
 * Interfejs z któego korzystaj¹ wszystkie klasy komunikacyjne.
 * Posiada deklaracje trzech metod, z których bêd¹ korzystaæ inne klasy.
 * S³u¿¹ one do zwracania wiadomoœci b³êdu, sprawdzania czy wyst¹pi³ b³¹d oraz
 * zwracania rodzaju operacji jaki ma zostaæ wykonany na obiekcie.
 */
public interface Communication extends Serializable{

	public String getMessage();

	public boolean isError();

	public @NotNull WhatToDo getWhatToDo();
}
