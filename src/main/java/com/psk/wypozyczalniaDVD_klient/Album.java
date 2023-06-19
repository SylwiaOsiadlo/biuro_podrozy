package com.psk.wypozyczalniaDVD_klient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Album {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String genre;
    private int quantity;
    private int wypozyczenia;

    public Album(String name, String genre, int quantity) {
        this.name = name;
        this.genre = genre;
        this.quantity = quantity;
    }
}