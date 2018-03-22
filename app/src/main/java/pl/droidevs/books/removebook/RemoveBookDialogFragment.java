package pl.droidevs.books.removebook;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import pl.droidevs.books.R;

public class RemoveBookDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.removing_book)
                .setMessage(R.string.removing_book_dialog_frag)
                .setPositiveButton(R.string.delete, (dialog, which) -> {
                    ((OnRemoveListener) getActivity()).removeChosen();
                })
                .setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                    dismiss();
                }).create();
    }

    public interface OnRemoveListener {
        void removeChosen();
    }
}
