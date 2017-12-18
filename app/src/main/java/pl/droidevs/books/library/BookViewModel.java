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
//    private MutableLiveData<Book> book;

    private final BookRepository bookRepository;


    @Inject
    public BookViewModel(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /*public LiveData<Book> getBook() {
        return book;
    }*/
    public Book getBook(String bookId) {
        LiveData<List<Book>> liveBooks = bookRepository.getBooks();
        List<Book> books = liveBooks.getValue();
        if(books !=null){
            for (Book book : books) {
                if (book.getId().getId() == bookId) {
                    return book;
                }
            }
        }
        Book testBook =new Book("Oient Express", "Agatka", Book.Category.TRAVEL);
        testBook.setImageUrl("");
        return null;
    }

    public LiveData<List<Book>> getBooks() {
        return bookRepository.getBooks();
    }
}
