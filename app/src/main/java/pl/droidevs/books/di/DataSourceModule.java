package pl.droidevs.books.di;

import android.arch.persistence.room.Room;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import pl.droidevs.books.app.BookDataBase;
import pl.droidevs.books.dao.BookDao;

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
}
