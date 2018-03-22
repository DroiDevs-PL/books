package pl.droidevs.books.dao;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import static pl.droidevs.books.dao.BookEntity.TABLE_NAME;

@Entity(tableName = TABLE_NAME)
public class BookEntity {
    static final String TABLE_NAME = "books";

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String title;
    private String author;
    private String description;
    private String category;
    private String imageUrl;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
