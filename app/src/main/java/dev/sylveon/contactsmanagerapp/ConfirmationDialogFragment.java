package dev.sylveon.contactsmanagerapp;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class ConfirmationDialogFragment extends DialogFragment {
    public static final String TAG = ConfirmationDialogFragment.class.getSimpleName();
    private final Runnable callback;

    @StringRes
    private final int message;

    public ConfirmationDialogFragment(Runnable callback, @StringRes int message) {
        this.callback = callback;
        this.message = message;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new MaterialAlertDialogBuilder(requireContext())
                .setMessage(message)
                // intentionally inverted to get Yes to show to the left of No
                // there's no functional difference between negative and positive buttons
                .setNegativeButton(R.string.yes, (dialog, which) -> callback.run())
                .setPositiveButton(R.string.no, (dialog, which) -> {/* do nothing */})
                .setCancelable(true)
                .create();
    }
}
