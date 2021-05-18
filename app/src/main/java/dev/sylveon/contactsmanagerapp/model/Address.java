package dev.sylveon.contactsmanagerapp.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Locale;

@Entity
public class Address {
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "street_number")
    private int streetNumber;

    @ColumnInfo(name = "street_name")
    @NonNull
    private String streetName = "";

    @ColumnInfo(name = "city")
    @NonNull
    private String city = "";

    @ColumnInfo(name = "province")
    @NonNull
    private ProvinceCode province = ProvinceCode.ON;

    @Embedded
    @NonNull
    private PostalCode postalCode = new PostalCode();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(int streetNumber) {
        this.streetNumber = streetNumber;
    }

    @NonNull
    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(@NonNull String streetName) {
        this.streetName = streetName;
    }

    @NonNull
    public String getCity() {
        return city;
    }

    public void setCity(@NonNull String city) {
        this.city = city;
    }

    @NonNull
    public ProvinceCode getProvince() {
        return province;
    }

    public void setProvince(@NonNull ProvinceCode province) {
        this.province = province;
    }

    @NonNull
    public PostalCode getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(@NonNull PostalCode postalCode) {
        this.postalCode = postalCode;
    }

    public String getFullAddress() {
        return String.format(Locale.US, "%d %s\n%s %s %s\nCanada", streetNumber, streetName, city, province.name(), postalCode.getFormatted());
    }
}
