package pl.droidevs.books.library;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import pl.droidevs.books.Resource;
import pl.droidevs.books.model.Book;
import pl.droidevs.books.repository.BookFilter;
import pl.droidevs.books.repository.BookRepository;

public final class LibraryViewModel extends ViewModel {
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final MutableLiveData<String> filterInput = new MutableLiveData<>();
    private final MutableLiveData<Resource<List<Book>>> books = new MutableLiveData<>();
    private final BookRepository bookRepository;

    @Inject
    LibraryViewModel(@NonNull final BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    void refresh() {
        final String query = filterInput.getValue();

        disposables.add(bookRepository.fetchBy(BookFilter.withTitleAndAuthor(query, query))
                .doOnSubscribe(it -> this.books.setValue(Resource.loading()))
                .subscribe(
                        result -> this.books.setValue(Resource.success(result)),
                        throwable -> this.books.setValue(Resource.error(throwable))
                )
        );
    }

    void setQuery(@Nullable final String query) {
        filterInput.setValue(query);
        disposables.add(bookRepository.fetchBy(BookFilter.withTitleAndAuthor(query, query))
                .doOnNext(result -> this.books.setValue(Resource.success(result)))
                .doOnError(throwable -> this.books.setValue(Resource.error(throwable)))
                .subscribe(
                        result -> this.books.setValue(Resource.success(result)),
                        throwable -> this.books.setValue(Resource.error(throwable))
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

    @NonNull
    public LiveData<Resource<List<Book>>> getBooks() {
        return books;
    }

    @Override
    protected void onCleared() {
        disposables.clear();
    }
}
