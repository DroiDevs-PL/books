package pl.droidevs.books.library;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.os.Environment;
import android.support.annotation.Nullable;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import pl.droidevs.books.model.Book;
import pl.droidevs.books.repository.BookRepository;

public class ExportImportViewModel extends ViewModel {

    private static final String FILE_NAME = "Books.csv";
    private BookRepository bookRepository;

    @Inject
    public ExportImportViewModel(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public void exportBooks() {
        File file = getFile();
        LiveData<List<Book>> booksLiveData = this.bookRepository.getBooks();

        booksLiveData.observeForever(new Observer<List<Book>>() {

            @Override
            public void onChanged(@Nullable List<Book> books) {
                writeToFile(file, books);
                booksLiveData.removeObserver(this);
            }});

    }

    private File getFile() {
        String baseDirectoryPath = Environment
                .getExternalStorageDirectory()
                .getAbsolutePath();
        String filePath = baseDirectoryPath + File.separator + FILE_NAME;

        File file = new File(filePath);

        try {

            if (!file.exists()) {
                file.createNewFile();
            } else {
                file.delete();
                file.createNewFile();
            }

            return file;
        } catch (IOException e) {
            return null;
        }
    }

    private void writeToFile(File file, List<Book> books) {

        try {
            FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(CSVHelper.getCSVContentFromBooksList(books));
            bufferedWriter.close();
        } catch (IOException e) {

        }
    }
}
