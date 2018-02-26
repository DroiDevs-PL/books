package pl.droidevs.books.savebook;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import javax.inject.Inject;

import pl.droidevs.books.Resource;
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

    private MutableLiveData<Resource<Book>> bookLiveData = new MutableLiveData<>();
    private MutableLiveData<Resource<Void>> saveLiveData = new MutableLiveData<>();

    @Inject
    SaveBookViewModel(DatabaseBookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public LiveData<Resource<Book>> getBook() {
        add(bookRepository.fetchBy(this.bookId).subscribe(
                book -> bookLiveData.setValue(Resource.success(book)),
                throwable -> bookLiveData.setValue(Resource.error(throwable))
        ));

        return bookLiveData;
    }

    public void setBookId(BookId bookId) {
        this.bookId = bookId;
    }

    public boolean isCoverUrlValid(String imageUrl) {
        return BookInputValidator.isCoverUrlValid(imageUrl);
    }

    void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    boolean isDataValid() {
        return BookInputValidator.isAuthorValid(author) &&
                BookInputValidator.isTitleValid(title);
    }

    LiveData<Resource<Void>> saveBook() {
        add(bookRepository.save(createBook())
                .subscribe(() -> saveLiveData.setValue(Resource.success()),
                        throwable -> saveLiveData.setValue(Resource.error(throwable))
                )
        );

        return saveLiveData;
    }

    Book createBook() {
        Book book = new Book(bookId, this.title, this.author, Book.Category.valueOf(this.category));
        book.setImageUrl(this.imageUrl);
        book.setDescription(this.description);

        return book;
    }
}
