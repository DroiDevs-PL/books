package pl.droidevs.books.repository;

import android.arch.core.util.Function;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import pl.droidevs.books.entity.BookEntity;
import pl.droidevs.books.model.Book;
import pl.droidevs.books.model.BookId;

class BookMapper {

    static final Function<List<BookEntity>, List<Book>> entitiesToBooksFunction =
            input -> {
                List<Book> books = new ArrayList<>(input.size());
                for (BookEntity bookEntity : input) {
                    books.add(BookMapper.toBook(bookEntity));
                }

                return books;
            };

    private BookMapper() {
    }

    static Book toBook(@Nullable final BookEntity entity) {
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

    static BookEntity toEntity(@NonNull final Book book) {
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
