package com.psk.wypozyczalniaDVD_klient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WypozyczenieView implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imieKlienta;
    private String nazwiskoKlienta;
    private String nrTelKlienta;

    private long idDVD;
    private String nazwaDVD;
    private String gatunekDVD;
    private int iloscSztuk;

    private LocalDate data_w; //data wypożyczenia
    private LocalDate data_z; //data zwrotu planowana

}
