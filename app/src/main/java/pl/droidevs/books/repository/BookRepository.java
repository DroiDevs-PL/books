package pl.droidevs.books.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import pl.droidevs.books.dao.BookDao;
import pl.droidevs.books.model.Book;
import pl.droidevs.books.model.BookId;

public final class BookRepository {
    private final BookDao bookDao;

    @Inject
    public BookRepository(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    public Completable save(Book book) {
        return Completable.fromAction(() -> bookDao.addBook(BookMapper.getBookEntity(book)));
    }

    public Completable remove(Book book) {
        return Completable.fromAction(() -> bookDao.removeBook(BookMapper.getBookEntity(book)));
    }

    public LiveData<List<Book>> getBooks() {
        return Transformations.map(bookDao.getAllBooks(), BookMapper.entitiesToBooksFunction);
    }

    public LiveData<Book> getBookById(BookId bookId) {
        int bookEntityId = BookMapper.getBookEntityIdFromBookId(bookId);
        
        return Transformations.map(bookDao.getBookById(bookEntityId),
                bookEntity -> BookMapper.getBook(bookEntity));
    }
}
