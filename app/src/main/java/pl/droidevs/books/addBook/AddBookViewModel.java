package pl.droidevs.books.addBook;

import android.arch.lifecycle.ViewModel;

import javax.inject.Inject;

import pl.droidevs.books.repository.BookRepository;

public final class AddBookViewModel extends ViewModel {

    private final BookRepository bookRepository;

    @Inject
    public AddBookViewModel(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

}
