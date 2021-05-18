package dev.sylveon.contactsmanagerapp.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.function.Consumer;

import dev.sylveon.contactsmanagerapp.R;
import dev.sylveon.contactsmanagerapp.databinding.ContactInfoEditListFooterBinding;
import dev.sylveon.contactsmanagerapp.databinding.ContactInfoListContentBinding;
import dev.sylveon.contactsmanagerapp.model.IContactInfo;
import dev.sylveon.contactsmanagerapp.viewholders.ContactInfoEditFooterViewHolder;
import dev.sylveon.contactsmanagerapp.viewholders.ContactInfoViewHolder;

public class ContactInfoEditListAdapter<T extends IContactInfo> extends FooterListAdapter<T, RecyclerView.ViewHolder> {

    private static final int FOOTER_VIEW = Integer.MIN_VALUE;
    private final Consumer<T> addListener, removeListener;
    private final Class<T> thing;
    private final int inputType;

    @SuppressWarnings("unchecked")
    public ContactInfoEditListAdapter(Consumer<T> addListener, Consumer<T> removeListener, Class<T> thing, int inputType) {
        // reuse the item callback from ContactInfoListAdapter
        super((DiffUtil.ItemCallback<T>)ContactInfoListAdapter.ITEM_CALLBACK);
        this.addListener = addListener;
        this.removeListener = removeListener;
        this.thing = thing;
        this.inputType = inputType;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == FOOTER_VIEW) {
            return new ContactInfoEditFooterViewHolder<>(
                    ContactInfoEditListFooterBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false),
                    thing,
                    addListener,
                    inputType
            );
        } else {
            return new ContactInfoViewHolder<>(
                    ContactInfoListContentBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false),
                    removeListener, R.drawable.ic_baseline_delete_24, R.color.material_red, null, 0
            );
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position + 1 < getItemCount()) {
            ((ContactInfoViewHolder<T>)holder).bind(getItem(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return FOOTER_VIEW;
        } else {
            return super.getItemViewType(position);
        }
    }
}
