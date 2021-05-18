package dev.sylveon.contactsmanagerapp.services;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import dev.sylveon.contactsmanagerapp.model.Address;

@Dao
public interface AddressService {
    @Insert
    long insert(Address addresses);

    @Update
    void update(Address addresses);

    @Query("DELETE FROM address WHERE id = :id")
    void delete(long id);

    @Query("SELECT * FROM address WHERE id = :id")
    LiveData<Address> findById(long id);

    @Query("SELECT * FROM address WHERE id = :id")
    Address findByIdImmediate(long id);
}
