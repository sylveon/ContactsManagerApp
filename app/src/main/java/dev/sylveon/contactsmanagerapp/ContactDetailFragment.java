package dev.sylveon.contactsmanagerapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.ImageRequest;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.List;
import java.util.function.Consumer;

import dev.sylveon.contactsmanagerapp.adapters.ContactInfoListAdapter;
import dev.sylveon.contactsmanagerapp.databinding.ContactDetailFragmentBinding;
import dev.sylveon.contactsmanagerapp.model.EmailAddress;
import dev.sylveon.contactsmanagerapp.model.IContactInfo;
import dev.sylveon.contactsmanagerapp.model.PhoneNumber;
import dev.sylveon.contactsmanagerapp.utils.GravatarImageLoader;
import dev.sylveon.contactsmanagerapp.viewmodels.ContactViewModel;

/**
 * A fragment representing a single Contact detail screen.
 * This fragment is either contained in a {@link ContactListActivity}
 * in two-pane mode (on tablets) or a {@link ContactDetailActivity}
 * on handsets.
 */
public class ContactDetailFragment extends Fragment {
    /**
     * The fragment argument representing the contact that this fragment
     * represents.
     */
    public static final String ARG_CONTACT = "contact";

    /**
     * The contact this fragment is presenting.
     */
    private ContactViewModel vm;

    private ContactDetailFragmentBinding bindings;
    private ImageRequest request;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ContactDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null && args.containsKey(ARG_CONTACT)) {
            vm = new ViewModelProvider(this).get(ContactViewModel.class);
            vm.setContactId(args.getLong(ARG_CONTACT));
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bindings = ContactDetailFragmentBinding.inflate(inflater, container, false);

        if (vm != null) {
            vm.contact.observe(getViewLifecycleOwner(), c -> {
                if (c != null) {
                    String fullNameStr = c.getFullName();

                    CollapsingToolbarLayout appBarLayout = getParentAppBar();
                    if (appBarLayout != null) {
                        appBarLayout.setTitle(fullNameStr);
                    }

                    if (bindings.contactFullName != null) {
                        bindings.contactFullName.setText(fullNameStr);
                    }
                }
            });

            if (getParentAppBar() == null) {
                vm.emailsList.observe(getViewLifecycleOwner(), emails -> {
                    if (request != null) {
                        // abort the previous request, if any.
                        request.cancel();
                    }

                    if (emails.size() != 0) {
                        assert bindings.avatar != null;
                        request = GravatarImageLoader.loadAvatarForEmail(
                                emails.get(0).getEmail(),
                                1080,
                                bindings.avatar::setImageBitmap,
                                error -> setNoAvatarImage()
                        );
                    } else {
                        setNoAvatarImage();
                    }
                });
            }

            vm.address.observe(getViewLifecycleOwner(), a -> {
                bindings.address.setVisibility(a != null ? View.VISIBLE : View.GONE);
                if (a != null) {
                    bindings.address.setText(a.getFullAddress());
                }
            });

            setupContactInfoRecyclerView(
                    this::sendEmail, R.drawable.ic_baseline_email_24,
                    null, 0,
                    vm.emailsList, bindings.emailHeader, bindings.emailCard, bindings.emailList);
            setupContactInfoRecyclerView(
                    this::callPhone, R.drawable.ic_baseline_phone_24,
                    this::sendSms, R.drawable.ic_baseline_message_24,
                    vm.phonesList, bindings.phoneHeader, bindings.phoneCard, bindings.phoneList);
        }

        return bindings.getRoot();
    }

    private CollapsingToolbarLayout getParentAppBar() {
        Activity activity = getActivity();
        if (activity != null) {
            return activity.findViewById(R.id.toolbar_layout);
        } else {
            return null;
        }
    }

    private <T extends IContactInfo> void setupContactInfoRecyclerView(
            Consumer<T> callback, @DrawableRes int icon,
            Consumer<T> alternativeCallback, @DrawableRes int alternativeIcon,
            LiveData<List<T>> data, TextView header, CardView card, RecyclerView recyclerView) {

        ContactInfoListAdapter<T> adapter = new ContactInfoListAdapter<>(callback, icon, alternativeCallback, alternativeIcon);
        recyclerView.setAdapter(adapter);

        Context context = getContext();
        if (context != null) {
            recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        }

        data.observe(getViewLifecycleOwner(), contactInfo -> {
            int visible = contactInfo.size() > 0 ? View.VISIBLE : View.GONE;
            header.setVisibility(visible);
            card.setVisibility(visible);
            adapter.submitList(contactInfo);
        });
    }

    private void setNoAvatarImage() {
        assert bindings.avatar != null;
        bindings.avatar.setImageResource(R.drawable.default_avatar);
    }

    private void sendEmail(EmailAddress email) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", email.getEmail(), null));
        emailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(Intent.createChooser(emailIntent, getString(R.string.send_email)));
    }

    private void callPhone(PhoneNumber phone) {
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", Long.toString(phone.getPhoneNumber()), null)));
    }

    private void sendSms(PhoneNumber phone) {
        Intent smsIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("smsto", Long.toString(phone.getPhoneNumber()), null));
        smsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(smsIntent);
    }

    public long getContactId() {
        return vm.getContactId();
    }

    public Long getAddressId() { return vm.getAddressId(); }
}