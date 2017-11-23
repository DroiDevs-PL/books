package pl.droidevs.books.app;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import pl.droidevs.books.dao.BookDao;
import pl.droidevs.books.entity.Book;

@Database(entities = {Book.class}, version = 1)
public abstract class BookDataBase extends RoomDatabase{
    public abstract BookDao bookDao();
}
