package pl.droidevs.books.library;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import javax.inject.Inject;

import pl.droidevs.books.Resource;
import pl.droidevs.books.domain.Book;
import pl.droidevs.books.domain.BookId;
import pl.droidevs.books.repository.database.DatabaseBookRepository;
import pl.droidevs.books.ui.RxViewModel;

public final class BookViewModel extends RxViewModel {

    private final DatabaseBookRepository bookRepository;
    private final MutableLiveData<Resource<Book>> bookLiveData = new MutableLiveData<>();

    @Inject
    BookViewModel(@NonNull final DatabaseBookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    LiveData<Resource<Book>> getBook(BookId bookId) {
        add(bookRepository.fetchById(bookId)
                .doOnSubscribe(it -> bookLiveData.setValue(Resource.loading()))
                .subscribe(
                        book -> bookLiveData.setValue(Resource.success(book)),
                        throwable -> bookLiveData.setValue(Resource.error(throwable))
                )
        );

        return bookLiveData;
    }
}
