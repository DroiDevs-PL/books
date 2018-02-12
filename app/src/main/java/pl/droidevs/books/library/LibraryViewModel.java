package pl.droidevs.books.library;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import javax.inject.Inject;

import pl.droidevs.books.model.Book;
import pl.droidevs.books.repository.BookFilter;
import pl.droidevs.books.repository.BookRepository;

public final class LibraryViewModel extends ViewModel {
    private final MutableLiveData<String> filterInput = new MutableLiveData<>();
    private final LiveData<List<Book>> books;

    @Inject
    LibraryViewModel(@NonNull BookRepository bookRepository) {
        books = Transformations.switchMap(filterInput, filter ->
                bookRepository.fetchBy(BookFilter.withTitleAndAuthor(filter, filter)));
        filterInput.setValue(null);
    }

    void setQuery(@Nullable final String query) {
        filterInput.setValue(query);
    }

    void clearQuery() {
        setQuery(null);
    }

    @Nullable
    public String getQuery() {
        return filterInput.getValue();
    }

    public LiveData<List<Book>> getBooks() {
        return books;
    }
}
