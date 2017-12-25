package pl.droidevs.books.library;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.os.Environment;
import android.support.annotation.Nullable;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import pl.droidevs.books.entity.BookEntity;
import pl.droidevs.books.repository.BookCsvRepository;

public class ExportImportViewModel extends ViewModel {

    private static final String FILE_NAME = "Books.csv";
    private BookCsvRepository bookCsvRepository;

    @Inject
    public ExportImportViewModel(BookCsvRepository bookRepository) {
        this.bookCsvRepository = bookRepository;
    }

    public boolean exportBooks() {
        File file;

        try {
            file = getFile();
        } catch (IOException e) {
            return false;
        }

        LiveData<List<BookEntity>> booksLiveData = this.bookCsvRepository.getBookEntities();
        final boolean[] wasSavingSuccessful = {true};
        booksLiveData.observeForever(new Observer<List<BookEntity>>() {

            @Override
            public void onChanged(@Nullable List<BookEntity> books) {

                try {
                    writeToFile(file, books);
                } catch (IOException e) {
                    wasSavingSuccessful[0] = false;
                } finally {
                    booksLiveData.removeObserver(this);
                }
            }
        });

        return wasSavingSuccessful[0];
    }

    private File getFile() throws IOException {
        String baseDirectoryPath = Environment
                .getExternalStorageDirectory()
                .getAbsolutePath();
        String filePath = baseDirectoryPath + File.separator + FILE_NAME;

        File file = new File(filePath);

        if (!file.exists()) {
            file.createNewFile();
        } else {
            file.delete();
            file.createNewFile();
        }

        return file;
    }

    private void writeToFile(File file, List<BookEntity> books) throws IOException {
        CsvBeanWriter csvBeanWriter = null;

        try {
            FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
            csvBeanWriter = new CsvBeanWriter(fileWriter, CsvPreference.STANDARD_PREFERENCE);

            csvBeanWriter.write(CSVHelper.getBookEntityCsvHeaders());

            for (BookEntity bookEntity : books) {
                csvBeanWriter.write(bookEntity, CSVHelper.getBookEntityCsvHeaders());
            }

        } finally {

            if (csvBeanWriter != null) {
                csvBeanWriter.close();
            }
        }
    }
}
