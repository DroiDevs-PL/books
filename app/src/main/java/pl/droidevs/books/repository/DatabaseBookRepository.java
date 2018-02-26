package pl.droidevs.books.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Collection;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import pl.droidevs.books.dao.BookDao;
import pl.droidevs.books.model.Book;
import pl.droidevs.books.model.BookId;
import pl.droidevs.books.reactive.Schedulers;

import static pl.droidevs.books.repository.BookMapper.toEntity;

public final class DatabaseBookRepository {
    private final BookDao bookDao;
    private final Schedulers schedulers;

    @Inject
    public DatabaseBookRepository(@NonNull final BookDao bookDao,
                                  @NonNull final Schedulers schedulers) {
        this.bookDao = bookDao;
        this.schedulers = schedulers;
    }

    public Completable saveAll(Collection<Book> books) {
        return Completable.fromAction(() -> {
            for (Book book : books) {
                bookDao.addBook(toEntity(book));
            }
        }).observeOn(schedulers.getObserver()).subscribeOn(schedulers.getSubscriber());
    }

    public Completable save(Book book) {
//        return Single.fromCallable(() -> {
//            bookDao.addBook(toEntity(book));
//            return book;
//        });

        return Completable.fromAction(() -> bookDao.addBook(toEntity(book)))
                .subscribeOn(schedulers.getSubscriber())
                .observeOn(schedulers.getObserver());
    }

    public Completable remove(Book book) {
        return Completable.fromAction(() -> bookDao.removeBook(toEntity(book)))
                .subscribeOn(schedulers.getSubscriber())
                .observeOn(schedulers.getObserver());
    }

    public Flowable<Collection<Book>> fetchAll() {
        return bookDao.getAllBooks()
                .map(BookMapper::toBooks)
                .observeOn(schedulers.getObserver())
                .subscribeOn(schedulers.getSubscriber());
    }

    @NonNull
    public Flowable<Collection<Book>> fetchBy(@Nullable final BookFilter filter) {
        if (filter == null || filter.isEmpty()) {
            return fetchAll();
        }

        return bookDao.getByTitleOrAuthor(filter.getTitle(), filter.getAuthor())
                .map(BookMapper::toBooks)
                .observeOn(schedulers.getObserver())
                .subscribeOn(schedulers.getSubscriber());
    }

    @NonNull
    public Maybe<Book> fetchBy(BookId bookId) {
        long bookEntityId = BookMapper.getBookEntityIdFromBookId(bookId);

        return bookDao.getBookById(bookEntityId)
                .map(BookMapper::toBook)
                .observeOn(schedulers.getObserver())
                .subscribeOn(schedulers.getSubscriber());
    }
}