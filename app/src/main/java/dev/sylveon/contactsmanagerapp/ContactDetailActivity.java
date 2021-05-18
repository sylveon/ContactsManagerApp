package dev.sylveon.contactsmanagerapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.android.volley.toolbox.ImageRequest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBar;
import androidx.lifecycle.ViewModelProvider;

import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import dev.sylveon.contactsmanagerapp.databinding.ContactDetailActivityBinding;
import dev.sylveon.contactsmanagerapp.services.AppDatabase;
import dev.sylveon.contactsmanagerapp.utils.GravatarImageLoader;
import dev.sylveon.contactsmanagerapp.viewmodels.ContactViewModel;

/**
 * An activity representing a single Item detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link ContactListActivity}.
 */
public class ContactDetailActivity extends AppCompatActivity {

    private ContactDetailActivityBinding bindings;
    private Menu menu;
    private ContactViewModel vm;
    private ImageRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindings = ContactDetailActivityBinding.inflate(getLayoutInflater());
        setContentView(bindings.getRoot());

        vm = new ViewModelProvider(this).get(ContactViewModel.class);
        vm.setContactId(getIntent().getExtras().getLong(ContactDetailFragment.ARG_CONTACT));

        setupHeader();

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
            ContactDetailFragment fragment = new ContactDetailFragment();
            fragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contact_details, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details_menu, menu);
        this.menu = menu;

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // forward the click on the back button to the system handler.
            onBackPressed();
        } else if (id == R.id.details_delete) {
            new ConfirmationDialogFragment(this::deleteContact, R.string.delete_confirmation).show(getSupportFragmentManager(), ConfirmationDialogFragment.TAG);
        } else if (id == R.id.details_edit) {
            editContact();
        } else {
            return super.onOptionsItemSelected(item);
        }

        return true;
    }

    private void setupHeader() {
        setSupportActionBar(bindings.detailToolbar);

        bindings.appBar.addOnOffsetChangedListener((layout, verticalOffset) -> {
            boolean isCollapsed = Math.abs(verticalOffset) == layout.getTotalScrollRange();
            if (isCollapsed) {
                bindings.fab.hide();
            } else {
                bindings.fab.show();
            }

            if (menu != null) {
                menu.findItem(R.id.details_edit).setVisible(isCollapsed);
            }
        });
        bindings.fab.setOnClickListener(view -> editContact());

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        vm.emailsList.observe(this, emails -> {
            if (request != null) {
                // abort the previous request, if any.
                request.cancel();
            }

            if (emails.size() != 0) {
                request = GravatarImageLoader.loadAvatarForEmail(
                        emails.get(0).getEmail(),
                        1080,
                        b -> {
                            getWindow().setStatusBarColor(Color.TRANSPARENT);
                            bindings.avatarBackground.setImageBitmap(b);
                            bindings.avatarBackground.setVisibility(View.VISIBLE);
                            bindings.toolbarLayout.setExpandedTitleTextAppearance(R.style.DropShadowTitle);
                        },
                        error -> setNoAvatarHeader()
                );
            } else {
                setNoAvatarHeader();
            }
        });
    }

    private void editContact() {
        Intent intent = new Intent(this, ContactEditActivity.class);
        intent.putExtra(ContactEditFragment.ARG_CONTACT, vm.getContactId());

        startActivity(intent);
    }

    private void deleteContact() {
        AppDatabase db = ContactsManagerApplication.getInstance().getDatabase();
        if (vm.getAddressId() != null) {
            db.addressService().delete(vm.getAddressId());
        }

        db.contactService().delete(vm.getContactId());

        // to get a go back animation to play
        onBackPressed();
    }

    private void setNoAvatarHeader() {
        TypedValue val = new TypedValue();
        // load the normal status bar color
        if (getTheme().resolveAttribute(android.R.attr.statusBarColor, val, true)) {
            getWindow().setStatusBarColor(val.data);
        }

        bindings.avatarBackground.setVisibility(View.GONE);
        bindings.toolbarLayout.setExpandedTitleTextAppearance(0);
    }
}