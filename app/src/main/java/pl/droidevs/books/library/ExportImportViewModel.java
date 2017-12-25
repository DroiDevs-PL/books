package pl.droidevs.books.library;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;

import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.File;
import java.io.FileReader;
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

    public void exportBooks() {
        File file = getFile();
        LiveData<List<BookEntity>> booksLiveData = this.bookCsvRepository.getBookEntities();

        booksLiveData.observeForever(new Observer<List<BookEntity>>() {

            @Override
            public void onChanged(@Nullable List<BookEntity> books) {
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

    private void writeToFile(File file, List<BookEntity> books) {

        try {
            FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
            CsvBeanWriter csvBeanWriter = new CsvBeanWriter(fileWriter, CsvPreference.STANDARD_PREFERENCE);

            CellProcessor[] cellProcessor = CSVHelper.getProcessors();
//            csvBeanWriter.write(CSVHelper.getBookEntityCsvHeaders());

            for (BookEntity bookEntity : books) {
                csvBeanWriter.write(bookEntity, CSVHelper.getBookEntityCsvHeaders());
            }

            csvBeanWriter.close();
        } catch (IOException e) {

        }

    }
}
