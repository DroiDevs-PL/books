package pl.droidevs.books.repository;

import android.arch.lifecycle.LiveData;

import java.util.List;

import javax.inject.Inject;

import pl.droidevs.books.app.BookDataBase;
import pl.droidevs.books.entity.Book;

public class BookRepository {

    @Inject
    BookDataBase bookDataBase;

    public BookRepository(BookDataBase bookDataBase) {
        this.bookDataBase = bookDataBase;
    }

    public void addBook(Book book) {
        bookDataBase.bookDao().addBook(book);
    }

    public void removeBook(Book book) {
        bookDataBase.bookDao().removeBook(book);
    }

    public LiveData<List<Book>> getBooks() {
        return bookDataBase.bookDao().getAllBooks();
    }
}
