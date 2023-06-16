package com.Kaczor.Kaluzinski.Kalarus.Communication;

/**
 * Typ wyliczeniowy enum, który zawiera wszystkie mo¿liwe do wykonania
 * na obiekcie operacje. S¹ to min. dodanie nowego elementu, zwrócenie danych,
 * aktualizacja obiektu lub jego usuniêie.
 */
public enum WhatToDo {
	GET,
	ADD,
	UPDATE,
	REMOVE, 
	GET_TRIPS, 
	TRIPS_ERROR
}
