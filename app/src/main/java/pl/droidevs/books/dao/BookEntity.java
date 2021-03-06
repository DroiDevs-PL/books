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
    private String year;
    private String publisher;
    private String description;
    private String category;
    private String imageUrl;
    private float rating;


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

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public float getRating() { return rating;}

    public void setRating(float rating) {
        this.rating = rating;
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
