CREATE TABLE album (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    genre VARCHAR(80) NOT NULL,
    quantity INT NOT NULL,
    cena DECIMAL(10, 2) NOT NULL
);
INSERT INTO album (name, genre, quantity, cena)
VALUES ('Avengers: Endgame', 'Akcja', 10, 29.99);
INSERT INTO album (name, genre, quantity, cena)
VALUES ('Pulp Fiction', 'Kryminał', 5, 12.99);
INSERT INTO album (name, genre, quantity, cena)
VALUES ('Incepcja', 'Science Fiction', 3, 9.99);
INSERT INTO album (name, genre, quantity, cena)
VALUES ('The Shawshank Redemption', 'Dramat', 7, 14.99);
INSERT INTO album (name, genre, quantity, cena)
VALUES ('The Dark Knight', 'Akcja', 4, 11.99);

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
INSERT INTO osoba (imie, nazwisko, nr_tel, miasto, ulica, nr_domu, kod_pocztowy)
VALUES ('Jan', 'Kowalski', '123456789', 'Warszawa', 'Aleja Pokoju', '10', '00-001');
INSERT INTO osoba (imie, nazwisko, nr_tel, miasto, ulica, nr_domu, kod_pocztowy)
VALUES ('Anna', 'Nowak', '987654321', 'Kraków', 'ul. Główna', '5', '30-001');
INSERT INTO osoba (imie, nazwisko, nr_tel, miasto, ulica, nr_domu, kod_pocztowy)
VALUES ('Tomasz', 'Wójcik', '555444333', 'Gdańsk', 'ul. Morska', '15', '80-001');
INSERT INTO osoba (imie, nazwisko, nr_tel, miasto, ulica, nr_domu, kod_pocztowy)
VALUES ('Monika', 'Lis', '111222333', 'Poznań', 'ul. Ogrodowa', '20', '60-001');
INSERT INTO osoba (imie, nazwisko, nr_tel, miasto, ulica, nr_domu, kod_pocztowy)
VALUES ('Piotr', 'Szymański', '999888777', 'Wrocław', 'ul. Wiejska', '8', '50-001');


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

