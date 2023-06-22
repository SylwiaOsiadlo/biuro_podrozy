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
public class Klient implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imie;
    private String nazwisko;
    private String nr_tel;
    private String miasto;
    private String ulica;
    private String nr_domu;
    private String kod; //kod pocztowy

    public Klient(String imie, String nazwisko, String nr_tel, String miasto, String ulica, String nr_domu, String kod){
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.nr_tel = nr_tel;
        this.miasto = miasto;
        this.ulica = ulica;
        this.nr_domu = nr_domu;
        this.kod = kod;
    }

}
