package dev.sylveon.contactsmanagerapp.model;

public interface IContactInfo {
    long getId();
    void setId(long id);

    /**
     * Gets a formatted representation of the data
     */
    String getInfo();

    /**
     * Parses user input
     * @param info The user input to parse
     * @return true if parsing was successful
     */
    boolean setInfo(String info);
}
