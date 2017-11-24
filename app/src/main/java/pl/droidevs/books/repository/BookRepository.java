package pl.droidevs.books.repository;

import android.arch.lifecycle.LiveData;

import java.util.List;

import javax.inject.Inject;

import pl.droidevs.books.dao.BookDao;
import pl.droidevs.books.entity.BookEntity;
import pl.droidevs.books.model.Book;

public class BookRepository {

    private BookDao bookDao;

    @Inject
    public BookRepository(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    public void save(BookEntity book) {
        bookDao.addBook(book);
    }

    public void remove(BookEntity book) {
        bookDao.removeBook(book);
    }

    public LiveData<List<Book>> getBooks() {
        return bookDao.getAllBooks();
    }
}
