package pl.droidevs.books.model;

import android.support.annotation.NonNull;

import java.io.Serializable;

public class BookId implements Serializable {
    private final String id;

    public BookId(@NonNull final String id) {
        this.id = id;
    }

    // ToDo: private/public???
    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BookId bookId = (BookId) o;

        return id.equals(bookId.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
