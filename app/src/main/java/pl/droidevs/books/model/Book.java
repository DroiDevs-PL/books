package pl.droidevs.books.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public final class Book  {
    private final BookId id;
    private final String title;
    private final String author;
    private final Category category;
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
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(@Nullable String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public enum Category {
        BIOGRAPHY, BUSINESS, KIDS, COMPUTERS, COOKING, FANTASY, HEALTH, HISTORY, HORROR, ENTERTAINEMENT,
        MYSTERY, ROMANCE, SCIENCE_FICTION, SPORT, TRAVEL
    }

    @Override
    public String toString() {
        getId().getId();
        return super.toString();

    }
}
