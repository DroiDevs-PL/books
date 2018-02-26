package pl.droidevs.books.library;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Collection;

import javax.inject.Inject;

import pl.droidevs.books.Resource;
import pl.droidevs.books.model.Book;
import pl.droidevs.books.repository.BookFilter;
import pl.droidevs.books.repository.DatabaseBookRepository;
import pl.droidevs.books.ui.RxViewModel;

import static android.text.TextUtils.isEmpty;

public final class LibraryViewModel extends RxViewModel {
    private final MutableLiveData<String> filterInput = new MutableLiveData<>();
    private final MutableLiveData<Resource<Collection<Book>>> books = new MutableLiveData<>();
    private final DatabaseBookRepository bookRepository;

    @Inject
    LibraryViewModel(@NonNull final DatabaseBookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    void refresh() {
        final String query = filterInput.getValue();

        add(bookRepository.fetchBy(BookFilter.withTitleAndAuthor(query, query))
                .doOnSubscribe(it -> books.setValue(Resource.loading()))
                .subscribe(
                        result -> books.setValue(Resource.success(result)),
                        throwable -> books.setValue(Resource.error(throwable))
                )
        );
    }

    void setQuery(@Nullable final String query) {
        filterInput.setValue(query);
        add(bookRepository.fetchBy(BookFilter.withTitleAndAuthor(query, query))
                .subscribe(
                        result -> books.setValue(Resource.success(result)),
                        throwable -> books.setValue(Resource.error(throwable))
                )
        );
    }

    void clearQuery() {
        setQuery(null);
    }

    @Nullable
    String getQuery() {
        return filterInput.getValue();
    }

    boolean isQuery() {
        return !isEmpty(getQuery());
    }

    @NonNull
    public LiveData<Resource<Collection<Book>>> getBooks() {
        return books;
    }
}
