package dev.sylveon.contactsmanagerapp.viewholders;

import android.view.View;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.recyclerview.widget.RecyclerView;

import java.util.function.Consumer;

import dev.sylveon.contactsmanagerapp.databinding.ContactInfoListContentBinding;
import dev.sylveon.contactsmanagerapp.model.IContactInfo;

public class ContactInfoViewHolder<T extends IContactInfo> extends RecyclerView.ViewHolder {
    private final ContactInfoListContentBinding bindings;
    private T info;

    public ContactInfoViewHolder(ContactInfoListContentBinding bindings,
                                 Consumer<T> listener, @DrawableRes int icon, @ColorRes int iconColor,
                                 Consumer<T> alternativeListener, @DrawableRes int alternativeIcon) {
        super(bindings.getRoot());
        this.bindings = bindings;

        if (iconColor != 0) {
            bindings.infoActionButton.setIconTintResource(iconColor);
            bindings.infoActionButton.setRippleColorResource(iconColor);
        }
        bindings.infoActionButton.setIconResource(icon);
        bindings.infoActionButton.setOnClickListener(v -> listener.accept(info));

        if (alternativeListener != null) {
            bindings.infoAlternativeActionButton.setIconResource(alternativeIcon);
            bindings.infoAlternativeActionButton.setOnClickListener(v -> alternativeListener.accept(info));
        } else {
            bindings.infoAlternativeActionButton.setVisibility(View.GONE);
        }
    }

    public void bind(T info) {
        this.info = info;
        bindings.infoText.setText(info.getInfo());
    }
}
