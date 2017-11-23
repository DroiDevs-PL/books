package pl.droidevs.books.di;

import android.arch.persistence.room.Room;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import pl.droidevs.books.app.BookApplication;
import pl.droidevs.books.app.BookDataBase;
import pl.droidevs.books.login.LoginService;
import pl.droidevs.books.repository.BookRepository;

/**
 * Module for application-wide dependencies.
 */
@Module
public class AppModule {

    @Provides
    Context context(BookApplication application) {
        return application.getApplicationContext();
    }

    @Singleton
    @Provides
    BookRepository bookRepository(BookDataBase bookDataBase) {
        return new BookRepository(bookDataBase);
    }

    @Singleton
    @Provides
    BookDataBase bookDataBase(Context context) {
        return Room.databaseBuilder(context.getApplicationContext(), BookDataBase.class, BookDataBase.BOOK_DATA_BASE_NAME).build();
    }

    @Singleton
    @Provides
        // TODO: Remove - just for testing DI
    LoginService loginService() {
        return () -> "I'm a DI user";
    }
}
