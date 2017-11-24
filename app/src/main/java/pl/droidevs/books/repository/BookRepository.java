package pl.droidevs.books.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;

import java.util.List;

import javax.inject.Inject;

import pl.droidevs.books.app.BookDataBase;
import pl.droidevs.books.mappers.BookMapper;
import pl.droidevs.books.model.Book;

public class BookRepository {

    private BookDataBase bookDataBase;

    @Inject
    public BookRepository(BookDataBase bookDataBase) {
        this.bookDataBase = bookDataBase;
    }

    public void save(Book book) {
        bookDataBase.bookDao().addBook(BookMapper.getBookEntity(book));
    }

    public void remove(Book book) {
        bookDataBase.bookDao().removeBook(BookMapper.getBookEntity(book));
    }

    public LiveData<List<Book>> getBooks() {
        return Transformations.map(bookDataBase.bookDao().getAllBooks(), BookMapper.entitiesToBooksFunction);
    }
}
