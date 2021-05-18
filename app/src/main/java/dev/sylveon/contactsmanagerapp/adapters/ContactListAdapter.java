package dev.sylveon.contactsmanagerapp.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import java.util.List;
import java.util.function.Consumer;

import dev.sylveon.contactsmanagerapp.viewholders.ContactViewHolder;
import dev.sylveon.contactsmanagerapp.databinding.ContactListContentBinding;
import dev.sylveon.contactsmanagerapp.model.ContactWithEmails;

public class ContactListAdapter extends ListAdapter<ContactWithEmails, ContactViewHolder> {
    private static final DiffUtil.ItemCallback<ContactWithEmails> itemCallback = new DiffUtil.ItemCallback<ContactWithEmails>() {
        @Override
        public boolean areItemsTheSame(@NonNull ContactWithEmails oldItem, @NonNull ContactWithEmails newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull ContactWithEmails oldItem, @NonNull ContactWithEmails newItem) {
            List<String> oldEmails = oldItem.getEmails();
            List<String> newEmails = newItem.getEmails();

            // check that the first & last name are the same and
            // that both email lists are empty, or if they aren't that the first email is the same
            return oldItem.getFirstName().equals(newItem.getFirstName()) &&
                    oldItem.getLastName().equals(newItem.getLastName()) &&
                        ((oldEmails.size() == 0 && newEmails.size() == 0) ||
                            (oldEmails.size() != 0 && newEmails.size() != 0 && oldEmails.get(0).equals(newEmails.get(0))));
        }
    };

    private static final String TAG = "ContactListAdapter";
    private final Consumer<ContactWithEmails> listener;

    public ContactListAdapter(Consumer<ContactWithEmails> listener) {
        super(itemCallback);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContactViewHolder(
                ContactListContentBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false),
                listener
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        holder.bind(getItem(position));
    }
}

