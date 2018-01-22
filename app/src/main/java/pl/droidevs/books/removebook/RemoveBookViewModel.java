package pl.droidevs.books.removebook;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import javax.inject.Inject;

import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import pl.droidevs.books.R;
import pl.droidevs.books.model.Book;
import pl.droidevs.books.repository.BookRepository;

public class RemoveBookViewModel extends ViewModel{

    private BookRepository bookRepository;
    private MutableLiveData<Integer> errorMessageResource = new MutableLiveData<>();

    @Inject
    public RemoveBookViewModel(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public void removeBook(Book book) {
        bookRepository.remove(book)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {

                    @Override
                    public void onSubscribe(Disposable d) {}

                    @Override
                    public void onComplete() {}

                    @Override
                    public void onError(Throwable e) {
                        errorMessageResource.postValue(R.string.remove_book_error);
                    }
                });
    }

    public LiveData<Integer> wasAnError() {
        return errorMessageResource;
    }
}
