package com.psk.wypozyczalniaDVD;

import java.time.LocalDate;

public class Validator {

    public static boolean validateName(String name, StringBuilder outInfo) {
        if (name.isEmpty()) {
            outInfo.append("Pole 'Imię' jest wymagane!");
            return false;
        }

        if (!name.matches("[A-Za-zĄĆĘŁŃÓŚŹŻąćęłńóśźż\\s]+")) {
            outInfo.append("Pole 'Imię' zawiera nieprawidłowe znaki!");
            return false;
        }

        return true;
    }

    public static boolean validateSurname(String surname, StringBuilder outInfo) {
        if (surname.isEmpty()) {
            outInfo.append("Pole 'Nazwisko' jest wymagane!");
            return false;
        }

        if (!surname.matches("[A-Za-zĄĆĘŁŃÓŚŹŻąćęłńóśźż\\s]+")) {
            outInfo.append("Pole 'Nazwisko' zawiera nieprawidłowe znaki!");
            return false;
        }

        return true;
    }

    public static boolean validateCity(String city, StringBuilder outInfo) {
        if (city.isEmpty()) {
            outInfo.append("Pole 'Miasto' jest wymagane!");
            return false;
        }

        if (!city.matches("[A-Za-zĄĆĘŁŃÓŚŹŻąćęłńóśźż\\s-]+")) {
            outInfo.append("Pole 'Miasto' zawiera nieprawidłowe znaki!");
            return false;
        }

        return true;
    }

    public static boolean validateStreet(String street, StringBuilder outInfo) {
        if (street.isEmpty()) {
            outInfo.append("Pole 'Ulica' jest wymagane!");
            return false;
        }

        if (!street.matches("[A-Za-zĄĆĘŁŃÓŚŹŻąćęłńóśźż0-9\\s-]+")) {
            outInfo.append("Pole 'Ulica' zawiera nieprawidłowe znaki!");
            return false;
        }

        return true;
    }

    public static boolean validateHouseNumber(String houseNumber, StringBuilder outInfo) {
        if (houseNumber.isEmpty()) {
            outInfo.append("Pole 'Nr domu' jest wymagane!");
            return false;
        }

        if (!houseNumber.matches("[0-9/\\-]+")) {
            outInfo.append("Pole 'Nr domu' zawiera nieprawidłowe znaki!");
            return false;
        }

        return true;
    }

    public static boolean validatePostalCode(String postalCode, StringBuilder outInfo) {
        if (postalCode.isEmpty()) {
            outInfo.append("Pole 'Kod pocztowy' jest wymagane!");
            return false;
        }

        if (!postalCode.matches("\\d{2}-\\d{3}")) {
            outInfo.append("Nieprawidłowy format pola 'Kod pocztowy'!");
            return false;
        }

        return true;
    }

    public static boolean validatePhoneNumber(String phoneNumber, StringBuilder outInfo) {
        if (phoneNumber.isEmpty()) {
            outInfo.append("Pole 'Nr tel.' jest wymagane!");
            return false;
        }

        if (!phoneNumber.matches("(\\+48)?\\d{9}")) {
            outInfo.append("Nieprawidłowy format numeru telefonu!");
            return false;
        }

        return true;
    }

    public static boolean validateAlbumName(String albumName, StringBuilder outInfo) {
        if (albumName.isEmpty()) {
            outInfo.append("Pole 'Nazwa płyty' jest wymagane!");
            return false;
        }

        if (albumName.length() > 255) {
            System.out.println("Pole 'Nazwa płyty' przekracza maksymalną długość (255 znaków)!");
            return false;
        }

        return true;
    }

    public static boolean validateAlbumGenre(String albumGenre, StringBuilder outInfo) {
        if (albumGenre.isEmpty()) {
            outInfo.append("Pole 'Gatunek płyty' jest wymagane!");
            return false;
        }

        if (!albumGenre.matches("^[A-Za-zĄĆĘŁŃÓŚŹŻąćęłńóśźż\\s-]*$")) {
            outInfo.append("Pole 'Gatunek płyty' zawiera nieprawidłowe znaki!");
            return false;
        }

        if (albumGenre.length() > 255) {
            System.out.println("Pole 'Gatunek płyty' przekracza maksymalną długość (255 znaków)!");
            return false;
        }

        return true;
    }

    public static boolean validatePrice(String price, StringBuilder outInfo) {
        if (price.isEmpty()) {
            outInfo.append("Pole 'cena' jest wymagane!");
            return false;
        }

        try {
            double priceValue = Double.parseDouble(price);
            if (priceValue < 1.00 || priceValue > 999.99) {
                outInfo.append("Pole 'Cena' powinno być w zakresie od 1.00 do 999.99!");
                return false;
            }
        } catch (NumberFormatException e) {
            outInfo.append("Pole 'Cena' zawiera nieprawidłową wartość liczbową!");
            return false;
        }

        return true;
    }

    public static boolean validateQuantity(String quantity, StringBuilder outInfo) {
        if (quantity.isEmpty()) {
            outInfo.append("Pole 'Ilość sztuk' jest wymagane!");
            return false;
        }

        try {
            int quantityValue = Integer.parseInt(quantity);
            if (quantityValue < 1 || quantityValue > 9999) {
                System.out.println("Pole 'Ilość sztuk' powinno być w zakresie od 1 do 9999!");
                return false;
            }
        } catch (NumberFormatException e) {
            System.out.println("Pole 'Ilość sztuk' zawiera nieprawidłową wartość liczbową!");
            return false;
        }

        return true;
    }

    public static boolean validateDate(String date, StringBuilder outInfo) {
        if (date.isEmpty()) {
            outInfo.append("Pole 'Data' jest wymagane!");
            return false;
        }

        try {
            LocalDate.parse(date);
        } catch (Exception e) {
            outInfo.append("Nieprawidłowy format pola 'Data'!");
            return false;
        }

        return true;
    }
}
