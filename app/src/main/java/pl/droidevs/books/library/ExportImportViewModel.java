package pl.droidevs.books.library;

import android.arch.lifecycle.ViewModel;
import android.os.Environment;

import java.io.File;

import javax.inject.Inject;

import pl.droidevs.books.repository.BookRepository;

public class ExportImportViewModel extends ViewModel {

    private static final String FILE_NAME = "Books.csv";
    private BookRepository bookRepository;

    @Inject
    public ExportImportViewModel(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public void exportBooks() {
        String baseDirectoryPath = Environment
                .getExternalStorageDirectory()
                .getAbsolutePath();
        String filePath = baseDirectoryPath + File.separator + FILE_NAME;
        File file = new File(filePath);
    }
}
