package pl.droidevs.books.domain;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public final class Book {
    private BookId id;
    private final String title;
    private final String author;
    private final Category category;
    private String year;
    private String description;
    private String imageUrl;

    public Book(@NonNull String title, @NonNull String author, @NonNull Category category) {
        this(null, title, author, category);
    }

    public Book(@Nullable BookId id, @NonNull String title, @NonNull String author, @NonNull Category category) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.category = category;
    }

    @Nullable
    public BookId getId() {
        return id;
    }

    public void setId(final BookId id) {
        this.id = id;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    @NonNull
    public String getAuthor() {
        return author;
    }

    @NonNull
    public Category getCategory() {
        return category;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    public void setDescription(@Nullable String description) {
        this.description = description;
    }

    @Nullable
    public String getYear() {
        return year;
    }

    public void setYear(@Nullable String year) {
        this.year = year;
    }

    @Nullable
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(@Nullable String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public enum Category {
        BIOGRAPHY, BUSINESS, KIDS, COMPUTERS, COOKING, FANTASY, HEALTH, HISTORY, HORROR, ENTERTAINMENT,
        MYSTERY, ROMANCE, SCIENCE_FICTION, SPORT, TRAVEL, THEATRE, POETRY, CINEMA, PHILOSOPHY, PHYCHOLOGY, COMICS
    }
}