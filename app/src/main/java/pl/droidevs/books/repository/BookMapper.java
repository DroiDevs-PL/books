package pl.droidevs.books.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import pl.droidevs.books.dao.BookEntity;
import pl.droidevs.books.domain.Book;
import pl.droidevs.books.domain.BookId;

public class BookMapper {
    private BookMapper() {
    }

    @NonNull
    public static Collection<Book> toBooks(@Nullable final Collection<BookEntity> entities) {
        if (entities == null) {
            return Collections.emptyList();
        }

        final Collection<Book> books = new ArrayList<>(entities.size());
        for (BookEntity entity : entities) books.add(toBook(entity));

        return books;
    }

    @Nullable
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
        book.setYear(entity.getYear());
        book.setRating(entity.getRating());
        book.setPublisher(entity.getPublisher());

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
        entity.setYear(book.getYear());
        entity.setPublisher(book.getPublisher());
        entity.setRating(book.getRating());

        if (book.getId() != null) {
            entity.setId(getBookEntityIdFromBookId(book.getId()));
        }

        return entity;
    }

    @NonNull
    public static Collection<BookEntity> toEntities(@Nullable final Collection<Book> books) {
        if (books == null) {
            return Collections.emptyList();
        }

        final Collection<BookEntity> entities = new ArrayList<>(books.size());
        for (Book book : books) entities.add(toEntity(book));

        return entities;
    }

    public static long getBookEntityIdFromBookId(BookId bookId) {
        return Long.parseLong(bookId.getValue());
    }
}
