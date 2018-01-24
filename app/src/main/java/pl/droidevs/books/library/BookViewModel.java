package pl.droidevs.books.library;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import pl.droidevs.books.model.Book;
import pl.droidevs.books.repository.BookRepository;

/**
 * Created by micha on 29.11.2017.
 */

public class BookViewModel extends ViewModel {

    private final BookRepository bookRepository;

    @Inject
    public BookViewModel(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public LiveData<List<Book>> getBooks() {
        return bookRepository.getBooks();
    }

    public LiveData<List<Book>>getBookById(String id){
        return bookRepository.getBookById(id);
    }
}
