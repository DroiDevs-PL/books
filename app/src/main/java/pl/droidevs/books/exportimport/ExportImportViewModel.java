package pl.droidevs.books.exportimport;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.Environment;

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

import io.reactivex.Observable;
import pl.droidevs.books.Resource;
import pl.droidevs.books.entity.BookEntity;
import pl.droidevs.books.model.Book;
import pl.droidevs.books.repository.BookMapper;
import pl.droidevs.books.repository.BookRepository;

public class ExportImportViewModel extends ViewModel {

    private static final String FILE_NAME = "Books.csv";

    private BookRepository bookRepository;
    private MutableLiveData<Resource<Void>> importedSuccessful = new MutableLiveData<>();

    @Inject
    public ExportImportViewModel(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public void exportBooks() {
        File file;

        try {
            file = createFile();
        } catch (IOException e) {
            throw new ExportFailedException(e);
        }

        bookRepository.fetchAll()
                .doOnSubscribe(it -> importedSuccessful.setValue(Resource.loading()))
                .subscribe(result -> {
                            writeToFile(file, Observable.just(result)
                                    .flatMapIterable(list -> list)
                                    .map(BookMapper::toEntity)
                                    .toList()
                                    .blockingGet());
                            importedSuccessful.setValue(Resource.success());
                        },
                        throwable -> importedSuccessful.setValue(Resource.error(new ExportFailedException(throwable)))
                );
    }

    private File createFile() throws IOException {
        final File file = createDefault();

        if (file.exists()) {
            file.delete();
        }

        file.createNewFile();

        return file;
    }

    private File createDefault() throws IOException {
        final String baseDirectoryPath = Environment
                .getExternalStorageDirectory()
                .getAbsolutePath();
        final String filePath = String.format("%s%s%s", baseDirectoryPath, File.separator, FILE_NAME);

        return new File(filePath);
    }

    private void writeToFile(File file, List<BookEntity> bookEntities) throws IOException {
        try (final FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
             final CsvBeanWriter csvBeanWriter = new CsvBeanWriter(fileWriter, CsvPreference.STANDARD_PREFERENCE)) {

            for (BookEntity bookEntity : bookEntities) {
                csvBeanWriter.write(bookEntity, CSVHelper.getBookEntityCsvHeaders());
            }
        }
    }

    public void importBooks() {
        importedSuccessful.setValue(Resource.loading());

        try (final FileReader fileReader = new FileReader(createDefault().getAbsoluteFile());
             final CsvBeanReader reader = new CsvBeanReader(fileReader, CsvPreference.STANDARD_PREFERENCE)) {
            final List<BookEntity> entitiesFromFile = readFrom(reader);
            saveBooks(entitiesFromFile);
        } catch (IOException e) {
            importedSuccessful.setValue(Resource.error(new ImportFailedException(e)));
        }
    }

    private List<BookEntity> readFrom(CsvBeanReader csvBeanReader) throws IOException {
        List<BookEntity> books = new ArrayList<>();
        BookEntity book;

        while ((book = csvBeanReader.read(BookEntity.class, CSVHelper.getBookEntityCsvHeaders(), CSVHelper.getProcessors())) != null) {
            books.add(book);
        }

        return books;
    }

    private void saveBooks(List<BookEntity> bookEntities) {
        final List<Book> books = Observable.just(bookEntities)
                .flatMapIterable(list -> list)
                .map(BookMapper::toBook)
                .toList()
                .blockingGet();

        bookRepository.save(books).subscribe(
                () -> importedSuccessful.postValue(Resource.success()),
                throwable -> importedSuccessful.postValue(Resource.error(throwable))
        );
    }

    public LiveData<Resource<Void>> getResult() {
        return importedSuccessful;
    }
}
