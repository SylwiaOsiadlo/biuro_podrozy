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

}
