package dev.sylveon.contactsmanagerapp;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import dev.sylveon.contactsmanagerapp.model.PhoneNumber;

import static org.junit.jupiter.api.Assertions.*;

public class PhoneNumberTests {
    @ParameterizedTest
    @CsvSource({
        "0, (000) 000-0000",
        "1234567890, (123) 456-7890",
        "9999999999, (999) 999-9999",
        "1234567, (000) 123-4567"
    })
    public void correctlyPrintsPhoneNumber(long number, String expected) {
        PhoneNumber phoneNumber = new PhoneNumber();
        phoneNumber.setPhoneNumber(number);

        assertEquals(expected, phoneNumber.getInfo());
    }

    @ParameterizedTest
    @CsvSource({
        "0, (000) 000-0000",
        "1234567890, 1234567890",
        "9999999999, 999-999-9999",
        "4324543567, (432) 454 3567",
        "4324544567, (432) 4544567",
        "4324554567, 432455 4567"
    })
    public void acceptsValidPhoneNumbers(long expected, String validNumber) {
        PhoneNumber phoneNumber = new PhoneNumber();

        assertTrue(phoneNumber.setInfo(validNumber));
        assertEquals(expected, phoneNumber.getPhoneNumber());
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "",
        "(123) 7897-0000",
        "(123) 789-000",
        "1234-789-0000",
        "12378970000",
        "test(123) 456-7890",
        "(123) 456-7890test",
        "test(123) 456-7890test"
    })
    public void rejectsInvalidPhoneNumbers(String invalidNumber) {
        PhoneNumber phoneNumber = new PhoneNumber();

        assertFalse(phoneNumber.setInfo(invalidNumber));
    }
}