package pl.droidevs.books.addBook;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import javax.inject.Inject;

import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import pl.droidevs.books.model.Book;
import pl.droidevs.books.repository.BookRepository;
import pl.droidevs.books.validators.BookInputValidator;

public final class AddBookViewModel extends ViewModel {

    private final BookRepository bookRepository;

    private String imageUrl;
    private String title;
    private String author;
    private String description;
    private String category;

    private MutableLiveData<Boolean> successWithAdding = new MutableLiveData<>();

    @Inject
    public AddBookViewModel(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public LiveData<Boolean> wasAddingSuccessful() {
        return successWithAdding;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isCoverUrlValid(String imageUrl) {
        return BookInputValidator.isCoverUrlValid(imageUrl);
    }

    public boolean isDataValid() {
        return BookInputValidator.isAuthorValid(author) &&
                BookInputValidator.isTitleValid(title);
    }

    public void saveBook() {
        this.bookRepository.save(createBook())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {

                    @Override
                    public void onSubscribe(Disposable d) {}

                    @Override
                    public void onComplete() {
                        successWithAdding.postValue(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        successWithAdding.postValue(false);
                    }
                });
    }

    public Book createBook() {
        Book book = new Book(null,
                this.title,
                this.author,
                Book.Category.valueOf(this.category));
        book.setImageUrl(this.imageUrl);
        book.setDescription(this.description);

        return book;
    }
}
