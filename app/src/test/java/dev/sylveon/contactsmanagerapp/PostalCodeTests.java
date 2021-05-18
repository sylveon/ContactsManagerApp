package dev.sylveon.contactsmanagerapp;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import dev.sylveon.contactsmanagerapp.model.PostalCode;

import static org.junit.jupiter.api.Assertions.*;

public class PostalCodeTests {
    @ParameterizedTest
    @CsvSource({
        "a1a1a1, A1A 1A1",
        "h0h0h0, H0H 0H0",
        "Z9Z9Z9, Z9Z 9Z9"
    })
    public void correctlyPrintsPostalCode(String code, String expected) {
        PostalCode postalCode = new PostalCode();
        postalCode.setCode(code);

        assertEquals(expected, postalCode.getFormatted());
    }

    @ParameterizedTest
    @CsvSource({
        "A1A1A1, a1A1a1",
        "H0H0H0, H0h 0H0",
        "Z9Z9Z9, z9z9z9",
        "Z9Z9Z9, Z9Z9Z9",
    })
    public void acceptsValidPostalCodes(String expected, String validCode) {
        PostalCode postalCode = new PostalCode();

        assertTrue(postalCode.validateAndSetCode(validCode));
        assertEquals(expected, postalCode.getCode());
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "",
        "aaA1a1",
        "11A1a1",
        "z9z9z9z",
        "!4432$",
        "testA1A1A1",
        "A1A1A1test",
        "testA1A1A1test"
    })
    public void rejectsInvalidPostalCodes(String invalidCode) {
        PostalCode postalCode = new PostalCode();

        assertFalse(postalCode.validateAndSetCode(invalidCode));
    }
}
