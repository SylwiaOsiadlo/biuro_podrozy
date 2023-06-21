CREATE TABLE album (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    genre VARCHAR(255) NOT NULL,
    quantity INT NOT NULL,
    cena DECIMAL(10, 2) NOT NULL
);

CREATE TABLE osoba (
  id INT AUTO_INCREMENT PRIMARY KEY,
  imie VARCHAR(255),
  nazwisko VARCHAR(255),
  nr_tel VARCHAR(255),
  miasto VARCHAR(255),
  ulica VARCHAR(255),
  nr_domu VARCHAR(255),
  kod_pocztowy VARCHAR(10)
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


