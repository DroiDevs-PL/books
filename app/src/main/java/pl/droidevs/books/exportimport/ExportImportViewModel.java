package pl.droidevs.books.exportimport;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.os.Environment;
import android.support.annotation.Nullable;

import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import pl.droidevs.books.entity.BookEntity;
import pl.droidevs.books.repository.BookCsvRepository;

public class ExportImportViewModel extends ViewModel {

    private static final String FILE_NAME = "Books.csv";
    private BookCsvRepository bookCsvRepository;
    private MutableLiveData<Boolean> importedSuccessful = new MutableLiveData<>();

    @Inject
    public ExportImportViewModel(BookCsvRepository bookCsvRepository) {
        this.bookCsvRepository = bookCsvRepository;
    }

    public void exportBooks() throws ExportFailedException {
        File file;

        try {
            file = createFile();
        } catch (IOException e) {
            throw new ExportFailedException(e);
        }

        LiveData<List<BookEntity>> booksLiveData = this.bookCsvRepository.getBookEntities();
        booksLiveData.observeForever(new Observer<List<BookEntity>>() {

            @Override
            public void onChanged(@Nullable List<BookEntity> books) {

                try {
                    writeToFile(file, books);
                } catch (IOException e) {
                    throw new ExportFailedException(e);
                } finally {
                    booksLiveData.removeObserver(this);
                }
            }
        });
    }

    private File createFile() throws IOException {
        File file = getFile();

        if (file.exists()) {
            file.delete();
            file.createNewFile();
        } else {
            file.createNewFile();
        }

        return file;
    }

    private File getFile() throws IOException {
        String baseDirectoryPath = Environment
                .getExternalStorageDirectory()
                .getAbsolutePath();
        String filePath = String.format("%s%s%s", baseDirectoryPath, File.separator, FILE_NAME);

        return new File(filePath);
    }

    private void writeToFile(File file, List<BookEntity> books) throws IOException {
        CsvBeanWriter csvBeanWriter = null;

        try {
            FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
            csvBeanWriter = new CsvBeanWriter(fileWriter, CsvPreference.STANDARD_PREFERENCE);

            for (BookEntity bookEntity : books) {
                csvBeanWriter.write(bookEntity, CSVHelper.getBookEntityCsvHeaders());
            }

        } finally {

            if (csvBeanWriter != null) {
                csvBeanWriter.close();
            }
        }
    }

    public void importBooks() throws ImportFailedException {

        try {
            File file = getFile();
            FileReader fileReader = new FileReader(file.getAbsoluteFile());
            CsvBeanReader reader = new CsvBeanReader(fileReader, CsvPreference.STANDARD_PREFERENCE);
            saveBooks(getBookEntities(reader));
        } catch (IOException e) {
            throw new ImportFailedException(e);
        }
    }

    private List<BookEntity> getBookEntities(CsvBeanReader csvBeanReader) throws IOException {
        List<BookEntity> bookEntities = new ArrayList<>();
        BookEntity bookEntity;

        while ((bookEntity = csvBeanReader.read(BookEntity.class, CSVHelper.getBookEntityCsvHeaders(), CSVHelper.getProcessors())) != null) {
            bookEntities.add(bookEntity);
        }

        return bookEntities;
    }

    void saveBooks(List<BookEntity> bookEntities) {
        bookCsvRepository.save(bookEntities)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> importedSuccessful.postValue(true),
                        e -> importedSuccessful.postValue(false));
    }

    public LiveData<Boolean> wasImportSuccesfull() {
        return importedSuccessful;
    }
}
