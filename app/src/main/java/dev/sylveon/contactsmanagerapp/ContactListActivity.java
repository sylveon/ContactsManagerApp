package dev.sylveon.contactsmanagerapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import dev.sylveon.contactsmanagerapp.adapters.ContactListAdapter;
import dev.sylveon.contactsmanagerapp.databinding.ContactListActivityBinding;
import dev.sylveon.contactsmanagerapp.model.ContactWithEmails;
import dev.sylveon.contactsmanagerapp.services.AppDatabase;
import dev.sylveon.contactsmanagerapp.viewmodels.ContactListViewModel;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ContactDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ContactListActivity extends AppCompatActivity {

    private static final int VIEW_DETAILS = 1;
    private static final int VIEW_EDIT = 2;
    private static final int VIEW_EMPTY = 3;

    private ContactListActivityBinding bindings;

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private boolean menuFabOpen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int nightMode = AppCompatDelegate.getDefaultNightMode();
        if (nightMode != AppCompatDelegate.MODE_NIGHT_YES &&
                nightMode != AppCompatDelegate.MODE_NIGHT_NO) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        bindings = ContactListActivityBinding.inflate(getLayoutInflater());
        setContentView(bindings.getRoot());

        setSupportActionBar(bindings.toolbar);
        bindings.toolbar.setTitle(getTitle());

        if (findViewById(R.id.contact_details) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        setupFabs();
        setupRecyclerView(findViewById(R.id.contact_list));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        int nightMode = AppCompatDelegate.getDefaultNightMode();
        if (nightMode == AppCompatDelegate.MODE_NIGHT_NO) {
            menu.findItem(R.id.menu_night_mode).setTitle(R.string.switch_to_night_mode);
        } else if (nightMode == AppCompatDelegate.MODE_NIGHT_YES) {
            menu.findItem(R.id.menu_night_mode).setTitle(R.string.switch_to_light_mode);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_night_mode) {
            int nightMode = AppCompatDelegate.getDefaultNightMode();
            if (nightMode == AppCompatDelegate.MODE_NIGHT_NO) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else if (nightMode == AppCompatDelegate.MODE_NIGHT_YES) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        } else if (id == R.id.menu_about) {
            new AboutDialogFragment().show(getSupportFragmentManager(), AboutDialogFragment.TAG);
        } else {
            return super.onOptionsItemSelected(item);
        }

        return true;
    }

    private void showMenuFab(boolean opened) {
        menuFabOpen = opened;

        if (menuFabOpen) {
            bindings.deleteContactMiniFab.show();
            bindings.editContactMiniFab.show();
            bindings.addContactMiniFab.show();
        } else {
            bindings.deleteContactMiniFab.hide();
            bindings.editContactMiniFab.hide();
            bindings.addContactMiniFab.hide();
        }

        bindings.menuFab.setImageResource(menuFabOpen
                ? R.drawable.ic_baseline_close_24
                : R.drawable.ic_baseline_menu_24);
    }

    private void updateFabVisibility(int viewType) {
        if (mTwoPane && viewType == VIEW_DETAILS) {
            bindings.addContactFab.setVisibility(GONE);
            bindings.saveContactFab.setVisibility(GONE);
            bindings.menuFab.setVisibility(VISIBLE);
        } else if (!mTwoPane || viewType == VIEW_EMPTY) {
            bindings.addContactFab.setVisibility(VISIBLE);
            bindings.saveContactFab.setVisibility(GONE);
            bindings.menuFab.setVisibility(GONE);
        } else if (viewType == VIEW_EDIT) {
            bindings.addContactFab.setVisibility(GONE);
            bindings.saveContactFab.setVisibility(VISIBLE);
            bindings.menuFab.setVisibility(GONE);
        } else {
            bindings.addContactFab.setVisibility(GONE);
            bindings.saveContactFab.setVisibility(GONE);
            bindings.menuFab.setVisibility(GONE);
        }
    }

    private void setupFabs() {
        Fragment current = getSupportFragmentManager().findFragmentById(R.id.contact_details);
        if (current instanceof ContactDetailFragment) {
            updateFabVisibility(VIEW_DETAILS);
        } else if (current instanceof ContactEditFragment) {
            updateFabVisibility(VIEW_EDIT);
        } else {
            updateFabVisibility(VIEW_EMPTY);
        }

        bindings.menuFab.setOnClickListener(v -> showMenuFab(!menuFabOpen));

        bindings.saveContactFab.setOnClickListener(v -> {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.contact_details);

            if (fragment instanceof ContactEditFragment) {
                Long insertedId = ((ContactEditFragment)fragment).save();
                if (insertedId != null) {
                    openContactPane(insertedId);
                }
            }
        });

        bindings.deleteContactMiniFab.setOnClickListener(v -> {
            showMenuFab(false);
            new ConfirmationDialogFragment(this::deleteContact, R.string.delete_confirmation).show(getSupportFragmentManager(), ConfirmationDialogFragment.TAG);
        });

        bindings.editContactMiniFab.setOnClickListener(v -> {
            showMenuFab(false);
            Fragment detailsFragment = getSupportFragmentManager().findFragmentById(R.id.contact_details);
            if (detailsFragment instanceof ContactDetailFragment) {
                Bundle arguments = new Bundle();
                arguments.putLong(ContactEditFragment.ARG_CONTACT, ((ContactDetailFragment)detailsFragment).getContactId());
                ContactEditFragment fragment = new ContactEditFragment();
                fragment.setArguments(arguments);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.contact_details, fragment)
                        .commit();
                updateFabVisibility(VIEW_EDIT);
            }
        });

        View.OnClickListener addContactListener = v -> {
            if (mTwoPane) {
                showMenuFab(false);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.contact_details, new ContactEditFragment())
                        .commit();
                updateFabVisibility(VIEW_EDIT);
            } else {
                startActivity(new Intent(this, ContactEditActivity.class));
            }
        };

        bindings.addContactFab.setOnClickListener(addContactListener);
        bindings.addContactMiniFab.setOnClickListener(addContactListener);
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        ContactListViewModel vm = new ViewModelProvider(this).get(ContactListViewModel.class);
        ContactListAdapter adapter = new ContactListAdapter(this::onContactClick);
        vm.contactsList.observe(this, adapter::submitList);

        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);
    }

    private void onContactClick(ContactWithEmails contact) {
        if (mTwoPane) {
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.contact_details);
            if (currentFragment instanceof ContactDetailFragment &&
                    ((ContactDetailFragment)currentFragment).getContactId() == contact.getId()) {
                return;
            } else if (currentFragment instanceof ContactEditFragment) {
                Long contactId = ((ContactEditFragment)currentFragment).getContactId();
                if (contactId == null || contactId != contact.getId()) {
                    new ConfirmationDialogFragment(() -> openContactPane(contact.getId()), R.string.view_confirmation)
                            .show(getSupportFragmentManager(), ConfirmationDialogFragment.TAG);
                }

                return;
            }

            openContactPane(contact.getId());
        } else {
            Intent intent = new Intent(this, ContactDetailActivity.class);
            intent.putExtra(ContactDetailFragment.ARG_CONTACT, contact.getId());

            startActivity(intent);
        }
    }

    private void openContactPane(long contactId) {
        Bundle arguments = new Bundle();
        arguments.putLong(ContactDetailFragment.ARG_CONTACT, contactId);
        ContactDetailFragment fragment = new ContactDetailFragment();
        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contact_details, fragment)
                .commit();
        updateFabVisibility(VIEW_DETAILS);
    }

    private void deleteContact() {
        Fragment detailsFragment = getSupportFragmentManager().findFragmentById(R.id.contact_details);
        if (detailsFragment instanceof ContactDetailFragment) {
            ContactDetailFragment fragment = (ContactDetailFragment)detailsFragment;
            getSupportFragmentManager().beginTransaction()
                    .remove(detailsFragment)
                    .commit();

            updateFabVisibility(VIEW_EMPTY);

            AppDatabase db = ContactsManagerApplication.getInstance().getDatabase();
            Long addressId = fragment.getAddressId();
            db.contactService().delete(fragment.getContactId());
            if (fragment.getAddressId() != null) {
                db.addressService().delete(addressId);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (mTwoPane) {
            Fragment detailsFragment = getSupportFragmentManager().findFragmentById(R.id.contact_details);
            if (detailsFragment instanceof ContactEditFragment) {
                new ConfirmationDialogFragment(super::onBackPressed, R.string.exit_confirmation)
                        .show(getSupportFragmentManager(), ConfirmationDialogFragment.TAG);

                return;
            }
        }

        super.onBackPressed();
    }
}