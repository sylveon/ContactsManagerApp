package dev.sylveon.contactsmanagerapp.viewholders;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.ImageRequest;

import java.util.function.Consumer;

import dev.sylveon.contactsmanagerapp.R;
import dev.sylveon.contactsmanagerapp.databinding.ContactListContentBinding;
import dev.sylveon.contactsmanagerapp.model.ContactWithEmails;
import dev.sylveon.contactsmanagerapp.utils.GravatarImageLoader;

public class ContactViewHolder extends RecyclerView.ViewHolder {
    private final ContactListContentBinding bindings;
    private ContactWithEmails contact;
    private ImageRequest currentRequest;

    public ContactViewHolder(ContactListContentBinding bindings, Consumer<ContactWithEmails> listener) {
        super(bindings.getRoot());
        this.bindings = bindings;

        bindings.getRoot().setOnClickListener(v -> listener.accept(contact));
    }

    public void bind(ContactWithEmails contactWithEmails) {
        this.contact = contactWithEmails;
        bindings.name.setText(contactWithEmails.getFullName());

        String email = null;
        if (contactWithEmails.getEmails().size() >= 1) {
            email = contactWithEmails.getEmails().get(0);
        }

        bindings.email.setText(email);
        if (email != null) {
            bindings.email.setVisibility(View.VISIBLE);

            if (currentRequest != null) {
                // abort the previous request, if any.
                currentRequest.cancel();
            }
            currentRequest = GravatarImageLoader.loadAvatarForEmail(
                    email,
                    512,
                    bindings.avatar::setImageBitmap,
                    error -> setNoAvatarImage()
            );
        } else {
            setNoAvatarImage();
            bindings.email.setVisibility(View.GONE);
        }
    }

    private void setNoAvatarImage() {
        bindings.avatar.setImageResource(R.drawable.default_avatar);
    }
}
