package dev.sylveon.contactsmanagerapp.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import dev.sylveon.contactsmanagerapp.ContactsManagerApplication;
import dev.sylveon.contactsmanagerapp.model.ContactWithEmails;

public class ContactListViewModel extends AndroidViewModel {
    public final LiveData<List<ContactWithEmails>> contactsList;
    public ContactListViewModel(Application app) {
        super(app);
        contactsList = ((ContactsManagerApplication)app).getDatabase().contactService().getAllByLastName();
    }
}
