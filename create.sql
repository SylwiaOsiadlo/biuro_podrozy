CREATE TABLE album (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    genre VARCHAR(80) NOT NULL,
    quantity INT NOT NULL,
    cena DECIMAL(10, 2) NOT NULL
);

CREATE TABLE osoba (
  id INT AUTO_INCREMENT PRIMARY KEY,
  imie VARCHAR(130),
  nazwisko VARCHAR(150),
  nr_tel VARCHAR(12),
  miasto VARCHAR(255),
  ulica VARCHAR(255),
  nr_domu VARCHAR(255),
  kod_pocztowy VARCHAR(6)
);
CREATE TABLE wypozyczenie (
  id INT AUTO_INCREMENT PRIMARY KEY,
  id_plyta INT,
  id_klient INT,
  data_w DATE,
  data_z DATE,
  FOREIGN KEY (id_plyta) REFERENCES album(id),
  FOREIGN KEY (id_klient) REFERENCES osoba(id)
);
CREATE TABLE zwrot(
  id INT AUTO_INCREMENT PRIMARY KEY,
  id_plyta INT,
  id_klient INT,
  active VARCHAR(10),
    FOREIGN KEY (id_plyta) REFERENCES album(id),
  FOREIGN KEY (id_klient) REFERENCES osoba(id)
);

CREATE TABLE zwrot (
   id INT PRIMARY KEY AUTO_INCREMENT,
   imieKlienta VARCHAR(130) NOT NULL ,
   nazwiskoKlienta VARCHAR(150) NOT NULL ,
   nrTelKlienta VARCHAR(12) NOT NULL ,
   idDVD BIGINT NOT NULL ,
   nazwaDVD VARCHAR(255) NOT NULL ,
   gatunekDVD VARCHAR(80) NOT NULL ,
   iloscSztuk INT NOT NULL,
   cena DECIMAL(10, 2) NOT NULL,
   data_w DATE NOT NULL,
   data_z_planowana DATE NOT NULL,
   data_z DATE NOT NULL
);


SELECT osoba.imie, osoba.nazwisko, osoba.nr_tel, album.name, album.genre, wypozyczenie.data_w, wypozyczenie.data_z
FROM osoba
         JOIN wypozyczenie ON osoba.id = wypozyczenie.id_klient
         JOIN album ON wypozyczenie.id_plyta = album.id;


