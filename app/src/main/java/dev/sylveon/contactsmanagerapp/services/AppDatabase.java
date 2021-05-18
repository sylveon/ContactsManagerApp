package dev.sylveon.contactsmanagerapp.services;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import dev.sylveon.contactsmanagerapp.model.Address;
import dev.sylveon.contactsmanagerapp.model.Contact;
import dev.sylveon.contactsmanagerapp.model.EmailAddress;
import dev.sylveon.contactsmanagerapp.model.PhoneNumber;
import dev.sylveon.contactsmanagerapp.model.ProvinceCode;

@Database(entities = {Address.class, Contact.class, EmailAddress.class, PhoneNumber.class}, version = 1, exportSchema = false)
@TypeConverters({ProvinceCode.Converter.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract AddressService addressService();
    public abstract ContactService contactService();
    public abstract EmailService emailService();
    public abstract PhoneService phoneService();
}
