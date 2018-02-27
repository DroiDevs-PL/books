package pl.droidevs.books.repository.csv;

import android.os.Environment;

import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import pl.droidevs.books.dao.BookEntity;
import pl.droidevs.books.domain.Book;
import pl.droidevs.books.domain.BookId;
import pl.droidevs.books.reactive.Schedulers;
import pl.droidevs.books.repository.BookFilter;
import pl.droidevs.books.repository.BookMapper;
import pl.droidevs.books.repository.BookRepository;

public final class CsvBookRepository implements BookRepository {
    // TODO: There should be an external file path provider.
    private static final String FILE_NAME = "Books.csv";

    private final Schedulers schedulers;

    @Inject
    public CsvBookRepository(@NonNull final Schedulers schedulers) {
        this.schedulers = schedulers;
    }

    @Override
    public Completable delete(Book entity) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public Completable deleteAll(Collection<? extends Book> entities) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public Flowable<Collection<Book>> fetchAll() {
        try (final FileReader fileReader = new FileReader(createDefault().getAbsoluteFile());
             final CsvBeanReader reader = new CsvBeanReader(fileReader, CsvPreference.STANDARD_PREFERENCE)) {
            final List<BookEntity> entitiesFromFile = readFromCsv(reader);
            return Flowable.just(BookMapper.toBooks(entitiesFromFile))
                    .observeOn(schedulers.getObserver())
                    .subscribeOn(schedulers.getSubscriber());
        } catch (IOException e) {
            return Flowable.empty();
        }
    }

    @Override
    public Maybe<Book> fetchById(BookId id) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public Single<Book> save(Book entity) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    private File createDefault() throws IOException {
        final String baseDirectoryPath = Environment
                .getExternalStorageDirectory()
                .getAbsolutePath();
        final String filePath = String.format("%s%s%s", baseDirectoryPath, File.separator, FILE_NAME);

        return new File(filePath);
    }

    private List<BookEntity> readFromCsv(final CsvBeanReader csvBeanReader) throws IOException {
        List<BookEntity> books = new ArrayList<>();
        BookEntity book;

        while ((book = csvBeanReader.read(BookEntity.class, CsvHelper.getBookEntityCsvHeaders(), CsvHelper.getProcessors())) != null) {
            books.add(book);
        }

        return books;
    }

    public Single<Collection<Book>> saveAll(final Collection<Book> books) {
        return Single.fromCallable(() -> {
            final File file = createFile();
            writeToFile(file, BookMapper.toEntities(books));

            return books;
        })
                .subscribeOn(schedulers.getSubscriber())
                .observeOn(schedulers.getObserver());
    }

    private File createFile() throws IOException {
        final File file = createDefault();

        if (file.exists()) {
            file.delete();
        }

        file.createNewFile();

        return file;
    }

    private void writeToFile(File file, Collection<BookEntity> bookEntities) throws IOException {
        try (final FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
             final CsvBeanWriter csvBeanWriter = new CsvBeanWriter(fileWriter, CsvPreference.STANDARD_PREFERENCE)) {

            for (BookEntity bookEntity : bookEntities) {
                csvBeanWriter.write(bookEntity, CsvHelper.getBookEntityCsvHeaders());
            }
        }
    }

    @Override
    public Flowable<Collection<Book>> fetchBy(BookFilter filter) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
