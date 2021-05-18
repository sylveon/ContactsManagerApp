package dev.sylveon.contactsmanagerapp.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import java.util.function.Consumer;

import dev.sylveon.contactsmanagerapp.databinding.ContactInfoListContentBinding;
import dev.sylveon.contactsmanagerapp.model.IContactInfo;
import dev.sylveon.contactsmanagerapp.viewholders.ContactInfoViewHolder;

public class ContactInfoListAdapter<T extends IContactInfo> extends ListAdapter<T, ContactInfoViewHolder<T>> {
    public static final DiffUtil.ItemCallback<IContactInfo> ITEM_CALLBACK = new DiffUtil.ItemCallback<IContactInfo>() {
        @Override
        public boolean areItemsTheSame(@NonNull IContactInfo oldItem, @NonNull IContactInfo newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull IContactInfo oldItem, @NonNull IContactInfo newItem) {
            return oldItem.getInfo().equals(newItem.getInfo());
        }
    };

    private final Consumer<T> listener, alternativeListener;

    @DrawableRes
    private final int icon, alternativeIcon;

    @SuppressWarnings("unchecked")
    public ContactInfoListAdapter(Consumer<T> listener, @DrawableRes int icon,
                                  Consumer<T> alternativeListener, @DrawableRes int alternativeIcon) {
        super((DiffUtil.ItemCallback<T>)ITEM_CALLBACK);
        this.listener = listener;
        this.icon = icon;
        this.alternativeListener = alternativeListener;
        this.alternativeIcon = alternativeIcon;
    }

    @NonNull
    @Override
    public ContactInfoViewHolder<T> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContactInfoViewHolder<>(
                ContactInfoListContentBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false),
                listener, icon, 0, alternativeListener, alternativeIcon
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ContactInfoViewHolder<T> holder, int position) {
        holder.bind(getItem(position));
    }
}
