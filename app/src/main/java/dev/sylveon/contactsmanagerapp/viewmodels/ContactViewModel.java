package dev.sylveon.contactsmanagerapp.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;

import java.util.List;

import dev.sylveon.contactsmanagerapp.ContactsManagerApplication;
import dev.sylveon.contactsmanagerapp.model.Address;
import dev.sylveon.contactsmanagerapp.model.Contact;
import dev.sylveon.contactsmanagerapp.model.EmailAddress;
import dev.sylveon.contactsmanagerapp.model.PhoneNumber;
import dev.sylveon.contactsmanagerapp.services.AppDatabase;

public class ContactViewModel extends AndroidViewModel {
    private static final LiveData<Address> NULL_ADDRESS = new MutableLiveData<>(null);
    private final Observer<Contact> addressObserver = c -> addressId = c != null ? c.getAddressId() : null;

    public final LiveData<Contact> contact;
    public final LiveData<List<EmailAddress>> emailsList;
    public final LiveData<List<PhoneNumber>> phonesList;
    public final LiveData<Address> address;
    private final MutableLiveData<Long> contactId = new MutableLiveData<>();
    private Long addressId;

    public ContactViewModel(Application app) {
        super(app);

        AppDatabase db = ((ContactsManagerApplication)app).getDatabase();
        contact = Transformations.switchMap(contactId, id -> db.contactService().findById(id));
        contact.observeForever(addressObserver);

        emailsList = Transformations.switchMap(contactId, id -> db.emailService().getAllForContact(id));
        phonesList = Transformations.switchMap(contactId, id -> db.phoneService().getAllForContact(id));
        address = Transformations.switchMap(contact, c -> {
            if (c != null && c.getAddressId() != null) {
                return db.addressService().findById(c.getAddressId());
            } else {
                return NULL_ADDRESS;
            }
        });
    }

    @Override
    protected void onCleared() {
        contact.removeObserver(addressObserver);
        super.onCleared();
    }

    public long getContactId() {
        return contactId.getValue();
    }

    public void setContactId(long id) {
        contactId.setValue(id);
    }

    public Long getAddressId() {
        return addressId;
    }
}
