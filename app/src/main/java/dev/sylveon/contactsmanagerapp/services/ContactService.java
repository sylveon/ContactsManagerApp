package dev.sylveon.contactsmanagerapp.services;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import dev.sylveon.contactsmanagerapp.model.Contact;
import dev.sylveon.contactsmanagerapp.model.ContactWithEmails;

@Dao
public interface ContactService {
    @Insert
    long insert(Contact contacts);

    @Update
    void update(Contact contacts);

    @Query("DELETE FROM contact WHERE id = :id")
    void delete(long id);

    @Query("SELECT * FROM contact WHERE id = :id")
    LiveData<Contact> findById(long id);

    @Query("SELECT * FROM contact WHERE id = :id")
    Contact findByIdImmediate(long id);

    @Transaction
    @Query("SELECT * FROM contact ORDER BY last_name COLLATE NOCASE ASC")
    LiveData<List<ContactWithEmails>> getAllByLastName();
}
