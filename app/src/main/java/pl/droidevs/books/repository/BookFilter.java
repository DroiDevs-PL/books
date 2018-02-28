package pl.droidevs.books.repository;

import android.support.annotation.Nullable;
import android.text.TextUtils;

public final class BookFilter {
    private final String title;
    private final String author;

    private BookFilter(@Nullable final String title, @Nullable final String author) {
        this.title = TextUtils.isEmpty(title) ? "" : title.toLowerCase();
        this.author = TextUtils.isEmpty(author) ? "" : author.toLowerCase();
    }

    @Nullable
    public String getTitle() {
        return title;
    }

    @Nullable
    public String getAuthor() {
        return author;
    }

    public boolean isEmpty() {
        return TextUtils.isEmpty(title) && TextUtils.isEmpty(author);
    }

    public static BookFilter empty() {
        return new BookFilter(null, null);
    }

    public static BookFilter withTitle(final String title) {
        return new BookFilter(title, null);
    }

    public static BookFilter withAuthor(final String author) {
        return new BookFilter(null, author);
    }

    public static BookFilter withTitleAndAuthor(final String title, final String author) {
        return new BookFilter(title, author);
    }
}
