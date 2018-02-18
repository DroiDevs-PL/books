package pl.droidevs.books.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import pl.droidevs.books.dao.BookDao;
import pl.droidevs.books.entity.BookEntity;
import pl.droidevs.books.model.Book;
import pl.droidevs.books.model.BookId;

import static pl.droidevs.books.repository.BookMapper.toEntity;

public final class BookRepository {
    private final BookDao bookDao;

    @Inject
    public BookRepository(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    public Completable save(Book book) {
        return Completable.fromAction(() -> bookDao.addBook(toEntity(book)));
    }

    public Completable remove(Book book) {
        return Completable.fromAction(() -> bookDao.removeBook(toEntity(book)));
    }

    public LiveData<List<Book>> fetchAll() {
        final LiveData<List<BookEntity>> books = bookDao.getAllBooks();

        return Transformations.map(books, BookMapper.entitiesToBooksFunction);
    }

    @NonNull
    public LiveData<List<Book>> fetchBy(@Nullable final BookFilter filter) {
        if (filter == null || filter.isEmpty()) {
            return fetchAll();
        }

        final LiveData<List<BookEntity>> books = bookDao.getByTitleOrAuthor(filter.getTitle(), filter.getAuthor());

        return Transformations.map(books, BookMapper.entitiesToBooksFunction);
    }

    @NonNull
    public LiveData<Book> fetchBy(BookId bookId) {
        long bookEntityId = BookMapper.getBookEntityIdFromBookId(bookId);

        return Transformations.map(bookDao.getBookById(bookEntityId), BookMapper::toBook);
    }
}
