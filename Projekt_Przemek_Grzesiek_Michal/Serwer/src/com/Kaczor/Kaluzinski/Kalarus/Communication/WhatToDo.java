package com.Kaczor.Kaluzinski.Kalarus.Communication;

/**
 * Typ wyliczeniowy enum, kt�ry zawiera wszystkie mo�liwe do wykonania
 * na obiekcie operacje. S� to min. dodanie nowego elementu, zwr�cenie danych,
 * aktualizacja obiektu lub jego usuni�ie.
 */
public enum WhatToDo {
	GET,
	ADD,
	UPDATE,
	REMOVE, 
	GET_TRIPS, 
	TRIPS_ERROR
}
