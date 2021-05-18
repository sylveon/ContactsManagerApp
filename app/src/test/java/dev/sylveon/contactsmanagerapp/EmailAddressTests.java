package dev.sylveon.contactsmanagerapp;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import dev.sylveon.contactsmanagerapp.model.EmailAddress;

import static org.junit.jupiter.api.Assertions.*;

public class EmailAddressTests {
    @ParameterizedTest
    @ValueSource(strings = {
        "charles.milette@gmail.com",
        "foo+test@example.ca",
        "bar_email@google.co.uk",
        "______@example.com",
        "user@foo-bar.com"
    })
    public void acceptsValidEmails(String email) {
        EmailAddress emailAddress = new EmailAddress();

        assertTrue(emailAddress.setInfo(email));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "",
        "@gmail.com",
        "foo+test@example",
        "bar@test.",
        "buz@-test.com",
        "#@%^%#$@#$@#.com",
        "SHOUTY@LOUD.COM",
        "test myemail@mydomain.com",
        "myemail@mydomain.com test",
        "test myemail@mydomain.com test"
    })
    public void rejectsInvalidEmails(String email) {
        EmailAddress emailAddress = new EmailAddress();

        assertFalse(emailAddress.setInfo(email));
    }
}
