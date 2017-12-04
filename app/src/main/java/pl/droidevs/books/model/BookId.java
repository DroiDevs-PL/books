package pl.droidevs.books.model;

import android.support.annotation.NonNull;

import java.io.Serializable;

public class BookId implements Serializable {
    private final String id;

    public BookId(@NonNull final String id) {
        this.id = id;
    }
}
