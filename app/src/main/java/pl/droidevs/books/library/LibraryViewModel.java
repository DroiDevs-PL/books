package pl.droidevs.books.library;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import pl.droidevs.books.R;
import pl.droidevs.books.model.Book;
import pl.droidevs.books.repository.BookRepository;

public final class LibraryViewModel extends ViewModel {

    private final BookRepository bookRepository;

    @Inject
    public LibraryViewModel(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public LiveData<List<Book>> getBooks() {
        return bookRepository.getBooks();
    }
}
