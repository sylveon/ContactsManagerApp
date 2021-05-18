package dev.sylveon.contactsmanagerapp;

import org.junit.jupiter.api.Test;

import dev.sylveon.contactsmanagerapp.model.Address;
import dev.sylveon.contactsmanagerapp.model.ProvinceCode;

import static org.junit.jupiter.api.Assertions.*;

public class AddressTests {
    @Test
    public void correctlyPrintsAddress() {
        Address address = new Address();
        address.setStreetNumber(123);
        address.setStreetName("Test Street");
        address.setCity("Test City");
        address.setProvince(ProvinceCode.QC);
        address.getPostalCode().setCode("A1A1A1");

        assertEquals("123 Test Street\nTest City QC A1A 1A1\nCanada", address.getFullAddress());
    }
}
