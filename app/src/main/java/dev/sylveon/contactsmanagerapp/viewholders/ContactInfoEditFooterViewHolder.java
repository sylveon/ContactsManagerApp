package dev.sylveon.contactsmanagerapp.viewholders;

import android.text.Editable;
import android.text.TextWatcher;

import androidx.recyclerview.widget.RecyclerView;

import java.util.function.Consumer;

import dev.sylveon.contactsmanagerapp.R;
import dev.sylveon.contactsmanagerapp.databinding.ContactInfoEditListFooterBinding;
import dev.sylveon.contactsmanagerapp.model.IContactInfo;

public class ContactInfoEditFooterViewHolder<T extends IContactInfo> extends RecyclerView.ViewHolder {
    // give each a unique but negative ID for RecyclerView animations to work
    private long nextId = -1;

    public ContactInfoEditFooterViewHolder(ContactInfoEditListFooterBinding bindings, Class<T> thing, Consumer<T> listener, int inputType) {
        super(bindings.getRoot());

        bindings.infoText.setInputType(inputType);
        bindings.infoText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                bindings.infoText.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        bindings.infoActionButton.setOnClickListener(v -> {
            T thingie;
            try {
                thingie = thing.newInstance();
            } catch (Exception e) {
                throw new RuntimeException("Failed to create contact info instance", e);
            }

            thingie.setId(nextId);
            if (thingie.setInfo(bindings.infoText.getText().toString())) {
                nextId--; // only decrement if the input was actually valid
                bindings.infoText.setText("");
                listener.accept(thingie);
            } else {
                // I cannot be bothered manually carrying a context into this.
                // so just grab it from the bindings
                bindings.infoText.setError(bindings.getRoot().getContext().getString(R.string.invalid_input));
            }
        });
    }
}
