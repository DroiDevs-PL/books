package pl.droidevs.books.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;

import java.util.List;

import javax.inject.Inject;

import pl.droidevs.books.dao.BookDao;
import pl.droidevs.books.model.Book;

public final class BookRepository {
    private final BookDao bookDao;

    @Inject
    public BookRepository(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    public void save(Book book) {
        bookDao.addBook(BookMapper.getBookEntity(book));
    }

    public void remove(Book book) {
        bookDao.removeBook(BookMapper.getBookEntity(book));
    }

    public LiveData<List<Book>> getBooks() {
        return Transformations.map(bookDao.getAllBooks(), BookMapper.entitiesToBooksFunction);
    }
}
