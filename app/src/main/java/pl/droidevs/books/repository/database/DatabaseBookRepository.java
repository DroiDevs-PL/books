package pl.droidevs.books.repository.database;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Collection;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import pl.droidevs.books.dao.BookDao;
import pl.droidevs.books.domain.Book;
import pl.droidevs.books.domain.BookId;
import pl.droidevs.books.reactive.Schedulers;
import pl.droidevs.books.repository.BookFilter;
import pl.droidevs.books.repository.BookMapper;
import pl.droidevs.books.repository.BookRepository;
import pl.droidevs.collection.CollectionUtils;

import static pl.droidevs.books.repository.BookMapper.getBookEntityIdFromBookId;
import static pl.droidevs.books.repository.BookMapper.toEntity;

public final class DatabaseBookRepository implements BookRepository {
    private final BookDao bookDao;
    private final Schedulers schedulers;

    @Inject
    public DatabaseBookRepository(@NonNull final BookDao bookDao,
                                  @NonNull final Schedulers schedulers) {
        this.bookDao = bookDao;
        this.schedulers = schedulers;
    }

    @Override
    public Single<Book> save(Book book) {
        return Single.fromCallable(() -> {
            final long id = bookDao.addBook(toEntity(book));
            book.setId(BookId.of(id));
            return book;
        }).observeOn(schedulers.getObserver()).subscribeOn(schedulers.getSubscriber());
    }

    public Single<Collection<Book>> saveAll(Collection<Book> books) {
        return Single.fromCallable(() -> {
            for (Book book : books) {
                final long id = bookDao.addBook(toEntity(book));
                book.setId(BookId.of(id));
            }

            return books;
        }).observeOn(schedulers.getObserver()).subscribeOn(schedulers.getSubscriber());
    }

    @Override
    public Completable delete(Book book) {
        return Completable.fromAction(() -> bookDao.removeBook(toEntity(book)))
                .subscribeOn(schedulers.getSubscriber())
                .observeOn(schedulers.getObserver());
    }

    @Override
    public Completable deleteAll(Collection<? extends Book> books) {
        return Completable.fromAction(() -> {
            final Collection<Long> ids = CollectionUtils.transform(books, book -> getBookEntityIdFromBookId(book.getId()));
            bookDao.removeBooks(ids.toArray(new Long[ids.size()]));
        }).subscribeOn(schedulers.getSubscriber()).observeOn(schedulers.getObserver());
    }

    public Flowable<Collection<Book>> fetchAll() {
        return bookDao.getAllBooks()
                .map(BookMapper::toBooks)
                .observeOn(schedulers.getObserver())
                .subscribeOn(schedulers.getSubscriber());
    }

    @NonNull
    @Override
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
    @Override
    public Maybe<Book> fetchById(BookId bookId) {
        long bookEntityId = getBookEntityIdFromBookId(bookId);

        return bookDao.getBookById(bookEntityId)
                .map(BookMapper::toBook)
                .observeOn(schedulers.getObserver())
                .subscribeOn(schedulers.getSubscriber());
    }
}
