package dev.sylveon.contactsmanagerapp.services;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import dev.sylveon.contactsmanagerapp.model.EmailAddress;

@Dao
public interface EmailService {
    @Insert
    void insert(List<EmailAddress> emails);

    @Query("SELECT * FROM emailaddress WHERE contact_id = :contactId")
    LiveData<List<EmailAddress>> getAllForContact(long contactId);

    @Query("SELECT * FROM emailaddress WHERE contact_id = :contactId")
    List<EmailAddress> getAllForContactImmediate(long contactId);

    @Query("DELETE FROM emailaddress WHERE contact_id = :contactId")
    void deleteAllForContact(long contactId);
}
