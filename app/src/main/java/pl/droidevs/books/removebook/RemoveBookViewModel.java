package pl.droidevs.books.removebook;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import javax.inject.Inject;

import pl.droidevs.books.Resource;
import pl.droidevs.books.domain.Book;
import pl.droidevs.books.repository.BookRepository;
import pl.droidevs.books.ui.RxViewModel;

public final class RemoveBookViewModel extends RxViewModel {

    private BookRepository bookRepository;
    private MutableLiveData<Resource<Void>> removalResult = new MutableLiveData<>();

    @Inject
    RemoveBookViewModel(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public LiveData<Resource<Void>> removeBook(Book book) {
        add(bookRepository.delete(book)
                .subscribe(
                        () -> removalResult.setValue(Resource.success()),
                        throwable -> removalResult.setValue(Resource.error(throwable))
                )
        );

        return removalResult;
    }
}
