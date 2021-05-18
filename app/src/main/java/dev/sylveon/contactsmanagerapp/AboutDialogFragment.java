package dev.sylveon.contactsmanagerapp;

import android.app.Dialog;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import dev.sylveon.contactsmanagerapp.databinding.AboutAppViewBinding;

public class AboutDialogFragment extends DialogFragment {
    public static final String TAG = AboutDialogFragment.class.getSimpleName();

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AboutAppViewBinding aboutView = AboutAppViewBinding.inflate(getActivity().getLayoutInflater());
        aboutView.author.setMovementMethod(LinkMovementMethod.getInstance());
        return new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.about_this_app)
                .setView(aboutView.getRoot())
                .setPositiveButton(R.string.close, null)
                .setCancelable(true)
                .create();
    }
}
