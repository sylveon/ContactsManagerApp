package dev.sylveon.contactsmanagerapp.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static androidx.room.ForeignKey.CASCADE;

@Entity(
        foreignKeys = @ForeignKey(entity = Contact.class, parentColumns = "id", childColumns = "contact_id", onDelete = CASCADE)
)
public class PhoneNumber implements IContactInfo {
    // this one made by me
    private static final Pattern PHONE_REGEX = Pattern.compile("^\\(?(\\d{3})\\)?[ \\-]?(\\d{3})[ \\-]?(\\d{4})$");

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "contact_id", index = true)
    private long contactId;

    private long phoneNumber;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getContactId() {
        return contactId;
    }

    public void setContactId(long contactId) {
        this.contactId = contactId;
    }

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String getInfo() {
        return String.format(Locale.US, "(%03d) %03d-%04d", (int)(phoneNumber / 10000000 % 1000), (int)(phoneNumber / 10000 % 1000), phoneNumber % 10000);
    }

    @Override
    public boolean setInfo(String info) {
        Matcher match = PHONE_REGEX.matcher(info);
        if (match.matches()) {
            phoneNumber = (Long.parseLong(match.group(1)) * 10000000) +
                    (Long.parseLong(match.group(2)) * 10000) +
                    Long.parseLong(match.group(3));
            return true;
        } else {
            return false;
        }
    }
}
