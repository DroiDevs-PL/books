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

@Dao
public interface BookDao {

    @Query("SELECT * FROM " + BookEntity.TABLE_NAME)
    LiveData<List<BookEntity>> getAllBooks();

    @Insert(onConflict = REPLACE)
    void addBook(BookEntity book);

    @Delete
    void removeBook(BookEntity book);

    @Update(onConflict = REPLACE)
    void updateBook(BookEntity book);
}
