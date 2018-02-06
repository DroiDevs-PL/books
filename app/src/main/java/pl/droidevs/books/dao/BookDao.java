package pl.droidevs.books.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import pl.droidevs.books.entity.BookEntity;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;
import static pl.droidevs.books.entity.BookEntity.TABLE_NAME;

@Dao
public interface BookDao {

    @Query("SELECT * FROM " + TABLE_NAME)
    LiveData<List<BookEntity>> getAllBooks();

    @Query("SELECT * FROM " + TABLE_NAME + " WHERE id = :bookId")
    LiveData<BookEntity> getBookById(int bookId);

    @Query("SELECT * FROM " + TABLE_NAME + " WHERE title LIKE :title OR author LIKE :author")
    LiveData<List<BookEntity>> getByTitleOrAuthor(final String title, final String author);

    @Insert(onConflict = REPLACE)
    void addBook(BookEntity book);

    @Delete
    void removeBook(BookEntity book);

    @Update(onConflict = REPLACE)
    void updateBook(BookEntity book);
}
