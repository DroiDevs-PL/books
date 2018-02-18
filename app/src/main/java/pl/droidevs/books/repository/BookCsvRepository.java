package pl.droidevs.books.repository;

import android.arch.lifecycle.LiveData;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
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

    public Completable save(List<BookEntity> bookEntities) {
        return Completable.fromAction(() -> {

            for (BookEntity bookEntity : bookEntities) {
                bookDao.addBook(bookEntity);
            }
        });
    }
}
