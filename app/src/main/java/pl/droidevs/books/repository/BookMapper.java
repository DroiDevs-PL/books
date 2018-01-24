package pl.droidevs.books.repository;

import android.arch.core.util.Function;

import java.util.ArrayList;
import java.util.List;

import pl.droidevs.books.entity.BookEntity;
import pl.droidevs.books.model.Book;
import pl.droidevs.books.model.BookId;

class BookMapper {

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

    public static Book getBook(BookEntity entity) {
        final Book book = new Book(
                new BookId(String.valueOf(entity.getId())),
                entity.getTitle(),
                entity.getAuthor(),
                Book.Category.valueOf(entity.getCategory()));
        book.setDescription(entity.getDescription());
        book.setImageUrl(entity.getImageUrl());

        return book;
    }

    public static BookEntity getBookEntity(Book book) {
        final BookEntity entity = new BookEntity();
        entity.setAuthor(book.getAuthor());
        entity.setCategory(book.getCategory().toString());
        entity.setDescription(book.getDescription());
        entity.setTitle(book.getTitle());
        entity.setImageUrl(book.getImageUrl());

        if (book.getId() != null) {
            entity.setId(Integer.parseInt(book.getId().getId()));
        }

        return entity;
    }

    public static int getBookEntityIdFromBookId(BookId bookId) {
        return Integer.parseInt(bookId.getId());
    }
}
