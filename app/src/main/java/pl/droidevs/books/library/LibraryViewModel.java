package pl.droidevs.books.library;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;

import java.util.List;

import javax.inject.Inject;

import pl.droidevs.books.entity.BookEntity;
import pl.droidevs.books.repository.BookRepository;

public class LibraryViewModel extends ViewModel {

    private BookRepository bookRepository;

    @Inject
    public LibraryViewModel(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
        putTestData();
    }

    //TODO Remove
    private void putTestData() {
        new PopulateDbAsyncTask().execute("");
    }

    public LiveData<List<BookEntity>> getBooks() {
        return bookRepository.getBooks();
    }

    //TODO Remove
    class PopulateDbAsyncTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            bookRepository.save(new BookEntity("Pippi Pończoszanka"));
            bookRepository.save(new BookEntity("Kubuś Puchatek"));
            bookRepository.save(new BookEntity("Clean Code"));

            return null;
        }
    }
}
