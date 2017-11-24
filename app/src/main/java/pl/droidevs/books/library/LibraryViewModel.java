package pl.droidevs.books.library;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;

import java.util.List;

import javax.inject.Inject;

import pl.droidevs.books.model.Book;
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

    public LiveData<List<Book>> getBooks() {
        return bookRepository.getBooks();
    }

    //TODO Remove
    class PopulateDbAsyncTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            bookRepository.save(new Book("Pippi Pończoszanka"));
            bookRepository.save(new Book("Kubuś Puchatek"));
            bookRepository.save(new Book("Clean Code"));

            return null;
        }
    }
}
