package pl.droidevs.books.app;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import pl.droidevs.books.dao.BookDao;
import pl.droidevs.books.entity.BookEntity;

@Database(entities = {BookEntity.class}, version = 2)
public abstract class BookDataBase extends RoomDatabase{
    public static final String BOOK_DATA_BASE_NAME = "book_db";

    public abstract BookDao bookDao();
}
