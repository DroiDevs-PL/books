package pl.droidevs.books.di;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.persistence.room.Room;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import pl.droidevs.books.app.BookApplication;
import pl.droidevs.books.app.BookDataBase;
import pl.droidevs.books.library.LibraryViewModel;
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
        // TODO: Remove - just for testing DI
    LoginService loginService() {
        return () -> "I'm a DI user";
    }
}
