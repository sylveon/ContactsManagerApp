package dev.sylveon.contactsmanagerapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import dev.sylveon.contactsmanagerapp.databinding.ContactDetailActivityBinding;
import dev.sylveon.contactsmanagerapp.databinding.ContactEditActivityBinding;

public class ContactEditActivity extends AppCompatActivity {

    ContactEditActivityBinding bindings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindings = ContactEditActivityBinding.inflate(getLayoutInflater());
        setContentView(bindings.getRoot());

        boolean isNew = !getIntent().hasExtra(ContactEditFragment.ARG_CONTACT);
        if (isNew) {
            bindings.toolbar.setTitle(R.string.add_contact);
        } else {
            bindings.toolbar.setTitle(R.string.edit_contact);
        }

        // Show the Up button in the action bar.
        setSupportActionBar(bindings.toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        bindings.saveContactFab.setOnClickListener(v -> {
            ContactEditFragment fragment = (ContactEditFragment)getSupportFragmentManager()
                    .findFragmentById(R.id.contact_edit);

            Long insertedId = fragment.save();
            if (insertedId != null) {
                if (isNew) {
                    Intent intent = new Intent(this, ContactDetailActivity.class);
                    intent.putExtra(ContactDetailFragment.ARG_CONTACT, insertedId);

                    startActivity(intent);
                    finish();
                } else {
                    super.onBackPressed();
                }
            }
        });

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            ContactEditFragment fragment = new ContactEditFragment();
            fragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contact_edit, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            goBack();
        } else {
            return super.onOptionsItemSelected(item);
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        goBack();
    }

    private void goBack() {
        new ConfirmationDialogFragment(super::onBackPressed, R.string.go_back_confirmation)
                .show(getSupportFragmentManager(), ConfirmationDialogFragment.TAG);
    }
}