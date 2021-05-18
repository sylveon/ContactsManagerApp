package dev.sylveon.contactsmanagerapp.model;

import androidx.room.Relation;

import java.io.Serializable;
import java.util.List;

public class ContactWithEmails extends Contact implements Serializable {
    @Relation(parentColumn = "id", entityColumn = "contact_id", entity = EmailAddress.class, projection = {"email"})
    private List<String> emails;

    public List<String> getEmails() {
        return emails;
    }

    public void setEmails(List<String> emails) {
        this.emails = emails;
    }
}
