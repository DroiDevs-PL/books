package pl.droidevs.books.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import static pl.droidevs.books.entity.Book.TABLE_NAME;

@Entity(tableName = TABLE_NAME)
public class Book {
    public static final String TABLE_NAME = "books";

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String author;
    private String description;

    public Book(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
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
}
