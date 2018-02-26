package pl.droidevs.books.savebook;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import javax.inject.Inject;

import pl.droidevs.books.model.Book;
import pl.droidevs.books.model.BookId;
import pl.droidevs.books.repository.DatabaseBookRepository;
import pl.droidevs.books.ui.RxViewModel;
import pl.droidevs.books.validators.BookInputValidator;

public final class SaveBookViewModel extends RxViewModel {

    private BookId bookId;
    private String imageUrl;
    private String title;
    private String author;
    private String description;
    private String category;

    private final DatabaseBookRepository bookRepository;

    private MutableLiveData<Boolean> successWithSaving = new MutableLiveData<>();

    @Inject
    public SaveBookViewModel(DatabaseBookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public LiveData<Book> getBook() {
        return bookRepository.fetchBy(this.bookId);
    }

    public LiveData<Boolean> wasSavingSuccessful() {
        return successWithSaving;
    }

    public void setBookId(BookId bookId) {
        this.bookId = bookId;
    }

    public boolean isCoverUrlValid(String imageUrl) {
        return BookInputValidator.isCoverUrlValid(imageUrl);
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

    public boolean isDataValid() {
        return BookInputValidator.isAuthorValid(author) &&
                BookInputValidator.isTitleValid(title);
    }

    public void saveBook() {
        add(bookRepository.save(createBook())
                .subscribe(() -> successWithSaving.setValue(true),
                        throwable -> successWithSaving.setValue(false)
                ));
    }

    public Book createBook() {
        Book book = new Book(bookId, this.title, this.author, Book.Category.valueOf(this.category));
        book.setImageUrl(this.imageUrl);
        book.setDescription(this.description);

        return book;
    }
}
