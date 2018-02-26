package pl.droidevs.books.model;

import android.support.annotation.NonNull;

import java.io.Serializable;

public final class BookId implements Serializable {
    private final String value;

    private BookId(@NonNull final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BookId bookId = (BookId) o;

        return value.equals(bookId.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value;
    }

    public static BookId of(@NonNull final String value) {
        return new BookId(value);
    }

    public static BookId of(final long value) {
        return new BookId(Long.toString(value));
    }
}
