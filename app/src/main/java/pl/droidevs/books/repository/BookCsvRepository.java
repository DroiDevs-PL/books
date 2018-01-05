package pl.droidevs.books.repository;

import android.arch.lifecycle.LiveData;

import java.util.List;

import javax.inject.Inject;

import pl.droidevs.books.dao.BookDao;
import pl.droidevs.books.entity.BookEntity;

public class BookCsvRepository {

    private final BookDao bookDao;

    @Inject
    public BookCsvRepository(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    public LiveData<List<BookEntity>> getBookEntities() {
        return bookDao.getAllBooks();
    }
}
