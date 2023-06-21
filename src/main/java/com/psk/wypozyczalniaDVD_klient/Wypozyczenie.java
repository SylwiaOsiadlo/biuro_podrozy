package com.psk.wypozyczalniaDVD_klient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Wypozyczenie implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int id_plyta;
    private int id_klient;
    private LocalDateTime data_w; //data wypożyczenia
    private LocalDateTime data_z; //data zwrotu planowana


    public Wypozyczenie(Long id_plyta, Long id_klient, LocalDateTime data_w, LocalDateTime data_z){
        this.id_plyta = id_plyta;
        this.id_klient = id_klient;
        this.data_w = data_w;
        this.data_z = data_z;
    }

}
