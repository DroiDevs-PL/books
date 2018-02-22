package pl.droidevs.books.repository;

import android.support.annotation.Nullable;

import pl.droidevs.books.entity.BookEntity;
import pl.droidevs.books.model.Book;
import pl.droidevs.books.model.BookId;

public class BookMapper {
    private BookMapper() {
    }

    public static Book toBook(@Nullable final BookEntity entity) {
        if (entity == null) {
            return null;
        }

        final Book book = new Book(
                BookId.of(entity.getId()),
                entity.getTitle(),
                entity.getAuthor(),
                Book.Category.valueOf(entity.getCategory()));
        book.setDescription(entity.getDescription());
        book.setImageUrl(entity.getImageUrl());

        return book;
    }

    public static BookEntity toEntity(@Nullable final Book book) {
        if (book == null) {
            return null;
        }

        final BookEntity entity = new BookEntity();
        entity.setAuthor(book.getAuthor());
        entity.setCategory(book.getCategory().toString());
        entity.setDescription(book.getDescription());
        entity.setTitle(book.getTitle());
        entity.setImageUrl(book.getImageUrl());

        if (book.getId() != null) {
            entity.setId(getBookEntityIdFromBookId(book.getId()));
        }

        return entity;
    }

    static long getBookEntityIdFromBookId(BookId bookId) {
        return Long.parseLong(bookId.getValue());
    }
}
