package dev.sylveon.contactsmanagerapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.PointerIcon;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.toolbox.ImageRequest;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import dev.sylveon.contactsmanagerapp.adapters.ContactInfoEditListAdapter;
import dev.sylveon.contactsmanagerapp.adapters.ContactInfoListAdapter;
import dev.sylveon.contactsmanagerapp.databinding.ContactDetailFragmentBinding;
import dev.sylveon.contactsmanagerapp.databinding.ContactEditFragmentBinding;
import dev.sylveon.contactsmanagerapp.model.Address;
import dev.sylveon.contactsmanagerapp.model.Contact;
import dev.sylveon.contactsmanagerapp.model.EmailAddress;
import dev.sylveon.contactsmanagerapp.model.IContactInfo;
import dev.sylveon.contactsmanagerapp.model.PhoneNumber;
import dev.sylveon.contactsmanagerapp.model.ProvinceCode;
import dev.sylveon.contactsmanagerapp.services.AppDatabase;
import dev.sylveon.contactsmanagerapp.viewmodels.ContactInfoViewModel;
import dev.sylveon.contactsmanagerapp.viewmodels.ContactViewModel;

public class ContactEditFragment extends Fragment {

    /**
     * The (optional) fragment argument representing the contact that this fragment
     * represents.
     */
    public static final String ARG_CONTACT = "contact";

    private ContactEditFragmentBinding bindings;

    /**
     * The contact that this edit view is showing, if any.
     * We don't want observable data here because the user is editing it.
     */
    private Contact contact;
    private Address address;
    private ContactInfoViewModel<EmailAddress> emails;
    private ContactInfoViewModel<PhoneNumber> phones;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ContactEditFragment() {
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        emails = new ViewModelProvider(this).get(EmailAddress.class.getSimpleName(), ContactInfoViewModel.class);
        phones = new ViewModelProvider(this).get(PhoneNumber.class.getSimpleName(), ContactInfoViewModel.class);

        Bundle args = getArguments();
        if (args != null && args.containsKey(ARG_CONTACT)) {
            long contactId = args.getLong(ARG_CONTACT);
            AppDatabase db = ContactsManagerApplication.getInstance().getDatabase();
            contact = db.contactService().findByIdImmediate(contactId);

            Long addressId = contact.getAddressId();
            if (addressId != null) {
                address = db.addressService().findByIdImmediate(addressId);
            }

            emails.setData(db.emailService().getAllForContactImmediate(contactId));
            phones.setData(db.phoneService().getAllForContactImmediate(contactId));
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        bindings = ContactEditFragmentBinding.inflate(inflater, container, false);

        setupContactInfoRecyclerView(emails, EmailAddress.class, bindings.getRoot().findViewById(R.id.email_list), InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        setupContactInfoRecyclerView(phones, PhoneNumber.class, bindings.getRoot().findViewById(R.id.phone_list), InputType.TYPE_CLASS_PHONE);

        clearErrorOnType(bindings.firstNameField);
        clearErrorOnType(bindings.lastNameField);
        clearErrorOnType(bindings.streetNumberField);
        clearErrorOnType(bindings.streetNameField);
        clearErrorOnType(bindings.cityField);
        clearErrorOnType(bindings.provinceField);
        clearErrorOnType(bindings.postalCodeField);

        if (contact != null) {
            bindings.firstNameEditText.setText(contact.getFirstName());
            bindings.lastNameEditText.setText(contact.getLastName());
        }

        if (address != null) {
            bindings.streetNumberEditText.setText(Integer.toString(address.getStreetNumber()));
            bindings.streetNameEditText.setText(address.getStreetName());
            bindings.cityEditText.setText(address.getCity());
            bindings.provinceEditText.setText(address.getProvince().toString());
            bindings.postalCodeEditText.setText(address.getPostalCode().getFormatted());
        }

        ArrayList<String> values = Arrays.stream(ProvinceCode.values())
                .map(Enum::toString)
                .collect(Collectors.toCollection(ArrayList::new));
        values.add(0, "");
        bindings.provinceEditText.setAdapter(
                new ArrayAdapter<>(requireContext(), R.layout.province_dropdown_content, values));

        return bindings.getRoot();
    }

    public Long getContactId() {
        if (contact != null) {
            return contact.getId();
        } else {
            return null;
        }
    }

    private <T extends IContactInfo> void setupContactInfoRecyclerView(
            ContactInfoViewModel<T> data, Class<T> thing, RecyclerView recyclerView, int inputType) {

        ContactInfoEditListAdapter<T> adapter = new ContactInfoEditListAdapter<>(data::addValue, data::removeValue, thing, inputType);
        recyclerView.setAdapter(adapter);

        Context context = getContext();
        if (context != null) {
            recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        }

        data.getLiveData().observe(getViewLifecycleOwner(), adapter::submitList);
    }

    private void clearErrorOnType(TextInputLayout layout) {
        layout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                layout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private boolean hasAddress() {
        return bindings.streetNumberEditText.getText().length() != 0 ||
                bindings.streetNameEditText.getText().length() != 0 ||
                bindings.cityEditText.getText().length() != 0 ||
                bindings.provinceEditText.getText().length() != 0 ||
                bindings.postalCodeEditText.getText().length() != 0;
    }

    private String loadFromField(TextInputLayout layout) {
        if (layout.getEditText().getText().length() < 1) {
            layout.setError(getString(R.string.empty_field));
            return null;
        } else {
            layout.setError(null);
            return layout.getEditText().getText().toString();
        }
    }

    private boolean loadContact(Contact newContact) {
        if (contact != null) {
            newContact.setId(contact.getId());
        }

        boolean valid = true;
        newContact.setFirstName(loadFromField(bindings.firstNameField));
        if (newContact.getFirstName() == null) {
            valid = false;
        }

        newContact.setLastName(loadFromField(bindings.lastNameField));
        if (newContact.getLastName() == null) {
            valid = false;
        }

        return valid;
    }

    private boolean loadAddress(Address newAddress) {
        if (address != null) {
            newAddress.setId(address.getId());
        }

        boolean valid = true;
        String streetNumber = loadFromField(bindings.streetNumberField);
        if (streetNumber == null) {
            valid = false;
        } else {
            try {
                newAddress.setStreetNumber(Integer.parseInt(streetNumber));
            } catch (NumberFormatException e) {
                bindings.streetNumberField.setError(getString(R.string.invalid_input));
                valid = false;
            }
        }

        newAddress.setStreetName(loadFromField(bindings.streetNameField));
        if (newAddress.getStreetName() == null) {
            valid = false;
        }

        newAddress.setCity(loadFromField(bindings.cityField));
        if (newAddress.getCity() == null) {
            valid = false;
        }

        String provinceCode = loadFromField(bindings.provinceField);
        if (provinceCode == null) {
            valid = false;
        } else {
            try {
                newAddress.setProvince(ProvinceCode.valueOf(provinceCode));
            } catch (IllegalArgumentException e) {
                bindings.provinceField.setError(getString(R.string.invalid_input));
                valid = false;
            }
        }

        String postalCode = loadFromField(bindings.postalCodeField);
        if (postalCode == null) {
            valid = false;
        } else {
            if (!newAddress.getPostalCode().validateAndSetCode(postalCode)) {
                bindings.postalCodeField.setError(getString(R.string.invalid_input));
                valid = false;
            }
        }

        return valid;
    }

    // returns null if there was an error validating the data
    public Long save() {
        boolean valid = true;

        Address newAddress = null;
        if (hasAddress()) {
            newAddress = new Address();
            if (!loadAddress(newAddress)) {
                valid = false;
            }
        } else {
            // clear all errors
            bindings.streetNumberField.setError(null);
            bindings.streetNameField.setError(null);
            bindings.cityField.setError(null);
            bindings.provinceField.setError(null);
            bindings.postalCodeField.setError(null);
        }

        Contact newContact = new Contact();
        if (!loadContact(newContact)) {
            valid = false;
        }

        if (valid) {
            if (newAddress != null) {
                newContact.setAddressId(newAddress.getId());
            }

            AppDatabase db = ContactsManagerApplication.getInstance().getDatabase();
            if (address == null && newAddress != null) {
                newContact.setAddressId(db.addressService().insert(newAddress));
            } else if (address != null && newAddress != null) {
                db.addressService().update(newAddress);
            } else if (address != null && newAddress == null) {
                db.addressService().delete(address.getId());
            }

            if (contact == null) {
                newContact.setId(db.contactService().insert(newContact));
            } else {
                db.contactService().update(newContact);
                db.emailService().deleteAllForContact(newContact.getId());
                db.phoneService().deleteAllForContact(newContact.getId());
            }

            // stupid code
            // but hey
            // it works

            db.emailService().insert(emails.getLiveData().getValue().stream().map(email -> {
                EmailAddress newEmail = new EmailAddress();
                newEmail.setContactId(newContact.getId());
                newEmail.setEmail(email.getEmail());
                return newEmail;
            }).collect(Collectors.toList()));

            db.phoneService().insert(phones.getLiveData().getValue().stream().map(phone -> {
                PhoneNumber newPhone = new PhoneNumber();
                newPhone.setContactId(newContact.getId());
                newPhone.setPhoneNumber(phone.getPhoneNumber());
                return newPhone;
            }).collect(Collectors.toList()));

            return newContact.getId();
        } else {
            return null;
        }
    }
}