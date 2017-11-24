package pl.droidevs.books.mappers;

import android.arch.core.util.Function;

import java.util.ArrayList;
import java.util.List;

import pl.droidevs.books.entity.BookEntity;
import pl.droidevs.books.model.Book;

public class BookMapper {

    public static final Function<List<BookEntity>, List<Book>> entitiesToBooksFunction =
            input -> {
                List<Book> books = new ArrayList<>(input.size());

                for (BookEntity bookEntity : input) {
                    books.add(BookMapper.getBook(bookEntity));
                }

                return books;
            };

    private BookMapper() {
    }

    public static Book getBook(BookEntity bookEntity) {
        final Book book = new Book(bookEntity.getTitle(),
                bookEntity.getAuthor(),
                Book.Category.valueOf(bookEntity.getCategory()));
        book.setDescription(book.getDescription());

        return book;
    }

    public static BookEntity getBookEntity(Book book) {
        final BookEntity bookEntity = new BookEntity();
        bookEntity.setAuthor(book.getAuthor());
        bookEntity.setCategory(book.getCategory().toString());
        bookEntity.setDescription(book.getDescription());
        bookEntity.setTitle(book.getTitle());

        return bookEntity;
    }
}
