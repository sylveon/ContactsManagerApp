package dev.sylveon.contactsmanagerapp.services;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import dev.sylveon.contactsmanagerapp.model.PhoneNumber;

@Dao
public interface PhoneService {
    @Insert
    void insert(List<PhoneNumber> phones);

    @Query("SELECT * FROM phonenumber WHERE contact_id = :contactId")
    LiveData<List<PhoneNumber>> getAllForContact(long contactId);

    @Query("SELECT * FROM phonenumber WHERE contact_id = :contactId")
    List<PhoneNumber> getAllForContactImmediate(long contactId);

    @Query("DELETE FROM phonenumber WHERE contact_id = :contactId")
    void deleteAllForContact(long contactId);
}
