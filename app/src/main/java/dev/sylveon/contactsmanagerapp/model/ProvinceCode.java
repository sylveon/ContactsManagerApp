package dev.sylveon.contactsmanagerapp.model;

import androidx.room.TypeConverter;

public enum ProvinceCode {
    ON, QC, NS, NB, MB, BC, PE, SK, AB, NL, NT, YT, NU;

    public static class Converter {
        @TypeConverter
        public static String toShortString(ProvinceCode value) {
            return value.name();
        }

        @TypeConverter
        public static ProvinceCode fromShortString(String value) {
            return ProvinceCode.valueOf(value);
        }
    }
}
