package pl.droidevs.books.savebook;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import javax.inject.Inject;

import pl.droidevs.books.Resource;
import pl.droidevs.books.domain.Book;
import pl.droidevs.books.domain.BookId;
import pl.droidevs.books.repository.BookRepository;
import pl.droidevs.books.ui.RxViewModel;
import pl.droidevs.books.validators.BookInputValidator;

public final class SaveBookViewModel extends RxViewModel {

    private BookId bookId;
    private String imageUrl;
    private String title;
    private String author;
    private String year;
    private String publisher;
    private float rating;
    private String description;
    private String category;

    private final BookRepository bookRepository;

    private MutableLiveData<Resource<Book>> bookLiveData = new MutableLiveData<>();

    @Inject
    SaveBookViewModel(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public LiveData<Resource<Book>> getBook() {
        add(bookRepository.fetchById(bookId).subscribe(
                book -> bookLiveData.setValue(Resource.success(book)),
                throwable -> bookLiveData.setValue(Resource.error(throwable))
        ));

        return bookLiveData;
    }

    public void setBookId(BookId bookId) {
        this.bookId = bookId;
    }

    boolean isCoverUrlValid(String imageUrl) {
        return BookInputValidator.isCoverUrlValid(imageUrl);
    }

    void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setRating(float rating) {
        this.rating = rating;
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
                BookInputValidator.isTitleValid(title) &&
                BookInputValidator.isYearValid(year);

    }

    LiveData<Resource<Book>> saveBook() {
        add(bookRepository.save(createBook())
                .subscribe(
                        book -> bookLiveData.setValue(Resource.success()),
                        throwable -> bookLiveData.setValue(Resource.error(throwable))
                )
        );

        return bookLiveData;
    }

    Book createBook() {
        Book book = new Book(bookId, this.title, this.author, Book.Category.valueOf(this.category));
        book.setImageUrl(this.imageUrl);
        book.setDescription(this.description);
        book.setYear(this.year);
        book.setPublisher(this.publisher);
        book.setRating(this.rating);


        return book;
    }
}
