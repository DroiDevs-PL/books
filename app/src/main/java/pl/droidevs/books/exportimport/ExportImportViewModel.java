package pl.droidevs.books.exportimport;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;

import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import pl.droidevs.books.entity.BookEntity;
import pl.droidevs.books.repository.BookCsvRepository;
import pl.droidevs.books.repository.BookRepository;

public class ExportImportViewModel extends ViewModel {

    private static final String FILE_NAME = "Books.csv";
    private BookCsvRepository bookCsvRepository;

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
        String filePath = baseDirectoryPath + File.separator + FILE_NAME;

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

            BookEntity bookEntity;

            while ((bookEntity = reader.read(BookEntity.class, CSVHelper.getBookEntityCsvHeaders(), CSVHelper.getProcessors())) != null) {
                saveBook(bookEntity);
            }

        } catch (IOException e) {
            throw new ImportFailedException(e);
        }
    }

    void saveBook(BookEntity bookEntity){
        bookCsvRepository.save(bookEntity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {

                    @Override
                    public void onSubscribe(Disposable d) {}

                    @Override
                    public void onComplete() {}

                    @Override
                    public void onError(Throwable e) {}
                });
    }
}
