package pl.droidevs.books.mappers;

import android.arch.core.util.Function;

import java.util.ArrayList;
import java.util.List;

import pl.droidevs.books.entity.BookEntity;
import pl.droidevs.books.model.Book;

public class BookMapper {

    public static Book getBook(BookEntity bookEntity) {
        Book book = new Book(bookEntity.getTitle());
        book.setAuthor(book.getAuthor());
        book.setDescription(book.getDescription());

        return book;
    }

    public static BookEntity getBookEntity(Book book) {
        BookEntity bookEntity = new BookEntity(book.getTitle());
        book.setAuthor(book.getAuthor());
        book.setDescription(book.getDescription());

        return bookEntity;
    }

    public static Function<List<BookEntity>, List<Book>> entitiesToBooksFunction =
            input -> {
                List<Book> books = new ArrayList<>(input.size());

                for (BookEntity bookEntity : input) {
                    books.add(BookMapper.getBook(bookEntity));
                }

                return books;
            };
}
