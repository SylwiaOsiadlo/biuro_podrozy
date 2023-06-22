package com.psk.wypozyczalniaDVD;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Album implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String genre;
    private int quantity;
    private int wypozyczenia;

    private float cena;

    public Album(String name, String genre, int quantity, float cena) {
        this.name = name;
        this.genre = genre;
        this.quantity = quantity;
        this.cena = cena;
    }

    public Album(long id, String name, String genre, int quantity, float cena) {
        this.id = id;
        this.name = name;
        this.genre = genre;
        this.quantity = quantity;
        this.cena = cena;
    }
}