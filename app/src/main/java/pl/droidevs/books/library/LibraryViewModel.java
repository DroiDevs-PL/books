package pl.droidevs.books.library;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import pl.droidevs.books.model.Book;
import pl.droidevs.books.repository.BookRepository;

public final class LibraryViewModel extends ViewModel {

    private final BookRepository bookRepository;

    @Inject
    public LibraryViewModel(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public LiveData<List<Book>> getBooks() {
        return bookRepository.fetchAll();
    }
}
