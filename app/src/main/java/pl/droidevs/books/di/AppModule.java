package pl.droidevs.books.di;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import pl.droidevs.books.app.BookApplication;
import pl.droidevs.books.login.LoginService;

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
