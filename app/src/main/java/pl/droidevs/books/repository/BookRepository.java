package pl.droidevs.books.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.Nullable;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import pl.droidevs.books.dao.BookDao;
import pl.droidevs.books.entity.BookEntity;
import pl.droidevs.books.model.Book;

public final class BookRepository {
    private final BookDao bookDao;

    private final MutableLiveData<Book> selectedBook = new MutableLiveData<>();

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

    public LiveData<List<Book>> getBookById(String id) {
        int iId = Integer.parseInt(id);
        return Transformations.map(bookDao.getBookById(iId), BookMapper.entitiesToBooksFunction);
    }
}
