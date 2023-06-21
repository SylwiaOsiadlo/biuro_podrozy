package com.psk.wypozyczalniaDVD_klient;

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
public class Zwrot implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long id_plyta;
    private Long id_klient;
    private String active; //pole logiczne przyjmujace wartosci tak/nie


    public Zwrot(Long id_plyta, Long id_klient, String active){
        this.id_plyta = id_plyta;
        this.id_klient = id_klient;
        this.active = active;
    }
}