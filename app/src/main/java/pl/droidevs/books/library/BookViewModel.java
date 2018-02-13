package pl.droidevs.books.library;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import javax.inject.Inject;

import pl.droidevs.books.model.Book;
import pl.droidevs.books.model.BookId;
import pl.droidevs.books.repository.BookRepository;

public class BookViewModel extends ViewModel {

    private final BookRepository bookRepository;

    @Inject
    BookViewModel(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    LiveData<Book> getBook(BookId bookId) {
        return bookRepository.fetchBy(bookId);
    }
}
