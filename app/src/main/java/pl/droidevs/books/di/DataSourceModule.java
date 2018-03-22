package pl.droidevs.books.di;

import android.arch.persistence.room.Room;
import android.content.Context;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import pl.droidevs.books.app.BookDataBase;
import pl.droidevs.books.dao.BookDao;
import pl.droidevs.books.reactive.Schedulers;
import pl.droidevs.books.repository.BookRepository;
import pl.droidevs.books.repository.csv.CsvBookRepository;
import pl.droidevs.books.repository.database.DatabaseBookRepository;

@Module(includes = AppModule.class)
class DataSourceModule {
    @Singleton
    @Provides
    BookDataBase bookDataBase(Context context) {
        return Room
                .databaseBuilder(context, BookDataBase.class, BookDataBase.BOOK_DATA_BASE_NAME)
                .fallbackToDestructiveMigration()
                .build();
    }

    @Singleton
    @Provides
    BookDao bookDao(Context context) {
        return bookDataBase(context).bookDao();
    }

    @Singleton
    @Provides
    BookRepository bookRepository(BookDao bookDao, Schedulers schedulers) {
        return new DatabaseBookRepository(bookDao, schedulers);
    }

    @Singleton
    @Provides
    @Named("CSV")
    BookRepository csvBookRepository(Schedulers schedulers) {
        return new CsvBookRepository(schedulers);
    }
}
