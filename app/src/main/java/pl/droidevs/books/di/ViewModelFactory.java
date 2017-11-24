package pl.droidevs.books.di;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import javax.inject.Inject;

import pl.droidevs.books.library.LibraryViewModel;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private LibraryViewModel viewModel;

    @Inject
    public ViewModelFactory(LibraryViewModel libraryViewModel) {
        this.viewModel = libraryViewModel;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        if (modelClass.isAssignableFrom(LibraryViewModel.class)) {
            return (T) viewModel;
        }

        throw new IllegalArgumentException("Unknown class name");
    }
}
