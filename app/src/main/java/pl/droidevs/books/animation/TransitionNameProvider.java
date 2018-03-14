package pl.droidevs.books.animation;

import android.support.annotation.NonNull;

import pl.droidevs.books.domain.BookId;

public final class TransitionNameProvider {
    private final BookId bookId;

    public TransitionNameProvider(@NonNull final BookId bookId) {
        this.bookId = bookId;
    }

    public String getImageTransition() {
        return "image-" + bookId;
    }

    public String getTitleTransition() {
        return "title-" + bookId;
    }

    public String getAuthorTransition() {
        return "author-" + bookId;
    }
}
