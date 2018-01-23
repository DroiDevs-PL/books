package pl.droidevs.books.removebook;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import javax.inject.Inject;

import pl.droidevs.books.R;
import pl.droidevs.books.model.BookId;
import pl.droidevs.books.savebook.SaveBookActivity;

public class RemoveBookDialogFragment extends DialogFragment {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    static public RemoveBookDialogFragment newInstance(BookId bookId) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(SaveBookActivity.BOOK_ID_EXTRA, bookId);

        RemoveBookDialogFragment fragment = new RemoveBookDialogFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

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

    private BookId getBookId() {
       return (BookId) getArguments().getSerializable(SaveBookActivity.BOOK_ID_EXTRA);
    }

    public interface OnRemoveListener {
        void removeChosen();
    }
}
