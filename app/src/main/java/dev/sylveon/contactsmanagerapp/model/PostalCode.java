package dev.sylveon.contactsmanagerapp.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PostalCode {
    // this one made by me
    private static final Pattern POSTAL_CODE_REGEX = Pattern.compile("^([a-zA-Z]\\d[a-zA-Z])[ \\-]?(\\d[a-zA-Z]\\d)$");

    @ColumnInfo(name = "postal_code")
    @NonNull
    private String code = "";

    @NonNull
    public String getCode() {
        return code;
    }

    public void setCode(@NonNull String code) {
        this.code = code;
    }

    public boolean validateAndSetCode(String code) {
        Matcher match = POSTAL_CODE_REGEX.matcher(code);
        if (match.matches()) {
            this.code = match.group(1).toUpperCase(Locale.US) + match.group(2).toUpperCase(Locale.US);
            return true;
        } else {
            return false;
        }
    }

    public String getFormatted() {
        return String.format(Locale.US, "%s %s",
                code.substring(0, 3).toUpperCase(Locale.US), code.substring(3).toUpperCase(Locale.US));
    }
}
