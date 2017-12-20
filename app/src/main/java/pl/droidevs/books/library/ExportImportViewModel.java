package pl.droidevs.books.library;

import android.arch.lifecycle.ViewModel;

import javax.inject.Inject;

import pl.droidevs.books.repository.BookRepository;

public class ExportImportViewModel extends ViewModel {

    private BookRepository bookRepository;

    @Inject
    public ExportImportViewModel(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public void exportBooks() {
    }
}
