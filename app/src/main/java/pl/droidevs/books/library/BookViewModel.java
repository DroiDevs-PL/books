package pl.droidevs.books.library;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import javax.inject.Inject;

import pl.droidevs.books.model.Book;
import pl.droidevs.books.repository.BookRepository;

/**
 * Created by micha on 29.11.2017.
 */

public class BookViewModel extends ViewModel {
//    private MutableLiveData<Book> book;

    private final BookRepository bookRepository;


    @Inject
    public BookViewModel(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /*public LiveData<Book> getBook() {
        return book;
    }*/

}
