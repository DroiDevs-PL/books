package pl.droidevs.books.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public final class Book {
    private final String title;
    private final String author;
    private final Category category;
    private String description;
    private String imageUrl;

    public Book(@NonNull String title, @NonNull String author, @NonNull Category category) {
        this.title = title;
        this.author = author;
        this.category = category;
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
}
