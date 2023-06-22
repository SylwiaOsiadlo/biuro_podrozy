package com.psk.wypozyczalniaDVD;

import org.junit.Assert;
import org.junit.Test;

public class MainTests {

    @Test
    public void testValidateName() {
        StringBuilder outInfo = new StringBuilder();
        Assert.assertTrue(Validator.validateName("John", outInfo));
        Assert.assertEquals("", outInfo.toString());

        Assert.assertFalse(Validator.validateName("", outInfo));
        Assert.assertTrue(outInfo.toString().contains("Pole 'Imię' jest wymagane!"));

        Assert.assertFalse(Validator.validateName("John123", outInfo));
        Assert.assertTrue(outInfo.toString().contains("Pole 'Imię' zawiera nieprawidłowe znaki!"));
    }

    @Test
    public void testValidateSurname() {
        StringBuilder outInfo = new StringBuilder();
        Assert.assertTrue(Validator.validateSurname("Doe", outInfo));
        Assert.assertEquals("", outInfo.toString());

        Assert.assertFalse(Validator.validateSurname("", outInfo));
        Assert.assertTrue(outInfo.toString().contains("Pole 'Nazwisko' jest wymagane!"));

        Assert.assertFalse(Validator.validateSurname("Doe123", outInfo));
        Assert.assertTrue(outInfo.toString().contains("Pole 'Nazwisko' zawiera nieprawidłowe znaki!"));
    }

    @Test
    public void testValidateCity() {
        StringBuilder outInfo = new StringBuilder();
        Assert.assertTrue(Validator.validateCity("Warsaw", outInfo));
        Assert.assertEquals("", outInfo.toString());

        Assert.assertFalse(Validator.validateCity("", outInfo));
        Assert.assertTrue(outInfo.toString().contains("Pole 'Miasto' jest wymagane!"));

        Assert.assertFalse(Validator.validateCity("New York!", outInfo));
        Assert.assertTrue(outInfo.toString().contains("Pole 'Miasto' zawiera nieprawidłowe znaki!"));
    }

    @Test
    public void testValidateStreet() {
        StringBuilder outInfo = new StringBuilder();
        Assert.assertTrue(Validator.validateStreet("Main Street 123", outInfo));
        Assert.assertEquals("", outInfo.toString());

        Assert.assertFalse(Validator.validateStreet("", outInfo));
        Assert.assertTrue(outInfo.toString().contains("Pole 'Ulica' jest wymagane!"));

        Assert.assertFalse(Validator.validateStreet("Invalid@Street", outInfo));
        Assert.assertTrue(outInfo.toString().contains("Pole 'Ulica' zawiera nieprawidłowe znaki!"));
    }

    @Test
    public void testValidateHouseNumber() {
        StringBuilder outInfo = new StringBuilder();
        Assert.assertTrue(Validator.validateHouseNumber("10A", outInfo));
        Assert.assertEquals("", outInfo.toString());

        Assert.assertFalse(Validator.validateHouseNumber("", outInfo));
        Assert.assertTrue(outInfo.toString().contains("Pole 'Nr domu' jest wymagane!"));

        Assert.assertFalse(Validator.validateHouseNumber("123!", outInfo));
        Assert.assertTrue(outInfo.toString().contains("Pole 'Nr domu' zawiera nieprawidłowe znaki!"));
    }

    @Test
    public void testValidatePostalCode() {
        StringBuilder outInfo = new StringBuilder();
        Assert.assertTrue(Validator.validatePostalCode("12-345", outInfo));
        Assert.assertEquals("", outInfo.toString());

        Assert.assertFalse(Validator.validatePostalCode("", outInfo));
        Assert.assertTrue(outInfo.toString().contains("Pole 'Kod pocztowy' jest wymagane!"));

        Assert.assertFalse(Validator.validatePostalCode("ABC-DEF", outInfo));
        Assert.assertTrue(outInfo.toString().contains("Nieprawidłowy format pola 'Kod pocztowy'!"));
    }

    @Test
    public void testValidatePhoneNumber() {
        StringBuilder outInfo = new StringBuilder();
        Assert.assertTrue(Validator.validatePhoneNumber("123456789", outInfo));
        Assert.assertEquals("", outInfo.toString());

        Assert.assertFalse(Validator.validatePhoneNumber("", outInfo));
        Assert.assertTrue(outInfo.toString().contains("Pole 'Nr tel.' jest wymagane!"));

        Assert.assertFalse(Validator.validatePhoneNumber("123-456-789", outInfo));
        Assert.assertTrue(outInfo.toString().contains("Nieprawidłowy format numeru telefonu!"));
    }

    @Test
    public void testValidateAlbumName() {
        StringBuilder outInfo = new StringBuilder();
        Assert.assertTrue(Validator.validateAlbumName("Album Name", outInfo));
        Assert.assertEquals("", outInfo.toString());

        Assert.assertFalse(Validator.validateAlbumName("", outInfo));
        Assert.assertTrue(outInfo.toString().contains("Pole 'Nazwa płyty' jest wymagane!"));

        Assert.assertFalse(Validator.validateAlbumName("Very Long Album Name That Exceeds Maximum Length", outInfo));
        Assert.assertTrue(outInfo.toString().contains("Pole 'Nazwa płyty' przekracza maksymalną długość (255 znaków)!"));
    }

    @Test
    public void testValidateAlbumGenre() {
        StringBuilder outInfo = new StringBuilder();
        Assert.assertTrue(Validator.validateAlbumGenre("Rock", outInfo));
        Assert.assertEquals("", outInfo.toString());

        Assert.assertFalse(Validator.validateAlbumGenre("", outInfo));
        Assert.assertTrue(outInfo.toString().contains("Pole 'Gatunek płyty' jest wymagane!"));

        Assert.assertFalse(Validator.validateAlbumGenre("Rock123!", outInfo));
        Assert.assertTrue(outInfo.toString().contains("Pole 'Gatunek płyty' zawiera nieprawidłowe znaki!"));
        Assert.assertFalse(Validator.validateAlbumGenre("Very Long Genre Name That Exceeds Maximum Length", outInfo));
        Assert.assertTrue(outInfo.toString().contains("Pole 'Gatunek płyty' przekracza maksymalną długość (255 znaków)!"));
    }

    @Test
    public void testValidatePrice() {
        StringBuilder outInfo = new StringBuilder();
        Assert.assertTrue(Validator.validatePrice("10.99", outInfo));
        Assert.assertEquals("", outInfo.toString());

        Assert.assertFalse(Validator.validatePrice("", outInfo));
        Assert.assertTrue(outInfo.toString().contains("Pole 'cena' jest wymagane!"));

        Assert.assertFalse(Validator.validatePrice("0.50", outInfo));
        Assert.assertTrue(outInfo.toString().contains("Pole 'Cena' powinno być w zakresie od 1.00 do 999.99!"));

        Assert.assertFalse(Validator.validatePrice("1000.00", outInfo));
        Assert.assertTrue(outInfo.toString().contains("Pole 'Cena' powinno być w zakresie od 1.00 do 999.99!"));

        Assert.assertFalse(Validator.validatePrice("ABC", outInfo));
        Assert.assertTrue(outInfo.toString().contains("Pole 'Cena' zawiera nieprawidłową wartość liczbową!"));
    }

    @Test
    public void testValidateQuantity() {
        StringBuilder outInfo = new StringBuilder();
        Assert.assertTrue(Validator.validateQuantity("5", outInfo));
        Assert.assertEquals("", outInfo.toString());

        Assert.assertFalse(Validator.validateQuantity("", outInfo));
        Assert.assertTrue(outInfo.toString().contains("Pole 'Ilość sztuk' jest wymagane!"));

        Assert.assertFalse(Validator.validateQuantity("0", outInfo));
        Assert.assertTrue(outInfo.toString().contains("Pole 'Ilość sztuk' powinno być w zakresie od 1 do 9999!"));

        Assert.assertFalse(Validator.validateQuantity("10000", outInfo));
        Assert.assertTrue(outInfo.toString().contains("Pole 'Ilość sztuk' powinno być w zakresie od 1 do 9999!"));

        Assert.assertFalse(Validator.validateQuantity("ABC", outInfo));
        Assert.assertTrue(outInfo.toString().contains("Pole 'Ilość sztuk' zawiera nieprawidłową wartość liczbową!"));
    }

}
